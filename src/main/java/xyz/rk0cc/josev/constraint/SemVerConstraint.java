package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;
import xyz.rk0cc.josev.SemVerRange;
import xyz.rk0cc.josev.SemVerRangeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Version constraint is a definition of {@link SemVer} range which bounding the version to ensure compatibility. This,
 * class in extended from {@link SemVerRange} with more complicated detection to determine which {@link SemVer} can be
 * accepted depending on different rules.
 *
 * @param <E> Definition of {@link ConstraintPattern constraint pattern} that available in the subclass.
 *
 * @since 1.0.0
 */
public abstract class SemVerConstraint<E extends ConstraintPattern<? extends Enum<?>>> extends SemVerRange {
    /**
     * Version constraint that inserted by user.
     */
    private final String rawConstraint;

    /**
     * Pattern uses in the version constraint.
     */
    private final E constraintPattern;

    /**
     * Create version constraint of {@link SemVer}.
     * <br/>
     * All implemented subclass should keep this constructor as private scope.
     *
     * @param constraintPattern An {@link ConstraintPattern} that applied from parser.
     * @param rawConstraint The version constraint that inserted by user.
     * @param start Start of the version range.
     * @param end End of the version range.
     */
    protected SemVerConstraint(
            @Nonnull E constraintPattern,
            @Nullable String rawConstraint,
            @Nullable SemVerRangeNode start,
            @Nullable SemVerRangeNode end
    ) {
        super(start, end);
        this.rawConstraint = rawConstraint;
        this.constraintPattern = constraintPattern;
    }

    /**
     * Detect which pattern is used for this constraint.
     *
     * @return An {@link Enum} which implemented {@link ConstraintPattern} that indicating the constraint syntax
     *         pattern used in this constraint.
     */
    @Nonnull
    public final E constraintPattern() {
        return constraintPattern;
    }

    /**
     * A {@link String} of constraint that came from {@link #parse(Class, String) parser}.
     *
     * @return User input value of the version constraint.
     */
    @Nullable
    public final String rawConstraint() {
        return rawConstraint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVerConstraint<?> that = (SemVerConstraint<?>) o;
        return Objects.equals(rawConstraint, that.rawConstraint)
                && Objects.equals(start(), that.start())
                && Objects.equals(end(), that.end())
                && constraintPattern.equals(that.constraintPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawConstraint, start(), end(), constraintPattern);
    }

    @Override
    public String toString() {
        return "SemVerConstraint{" +
                "rawConstraint='" + rawConstraint + '\'' +
                ", start=" + start() +
                ", end=" + end() +
                ", constraintPattern=" + constraintPattern +
                '}';
    }

    /**
     * Parser a {@link String} of <code>versionConstraint</code> and specify which {@link Class} that extended from
     * {@link SemVerConstraint} will be exported.
     * <br/>
     * <b style="font-size: 18px;">Not all {@linkplain Class} which inherited from {@linkplain SemVerConstraint} can be
     * parsed by this method unless it meets these requirements:</b>
     * <ul>
     *     <li>The class must be public, non-abstract class</li>
     *     <li>Do not implement any public scope constructor</li>
     *     <li>Has a static method that returns the same {@link Class} of <code>constraintClass</code></li>
     *     <li>
     *         Annotated {@link SemVerConstraintParser} with proper configurations:
     *         <ul>
     *             <li>Set {@link SemVerConstraintParser#enabled() enabled} as <code>true</code></li>
     *             <li>Proper {@link SemVerConstraintParser#parserMethodName() name} of the static method</li>
     *         </ul>
     *     </li>
     * </ul>
     * If one of these requirement is missed, it will throw {@link UnsupportedOperationException}.
     *
     * @param constraintClass A well implemented {@link Class} which inherited from {@link SemVerConstraint}.
     * @param versionConstraint A {@link String} of version constraint.
     * @param <C> Defined which subclass of {@link SemVerConstraint} will be returned.
     *
     * @return An object of {@link C} with corresponded data of {@link SemVerConstraint}.
     *
     * @throws UnsupportedOperationException If the implementation does not meet all requirement on the list.
     *
     * @see SemVerConstraintParser
     */
    @SuppressWarnings("unchecked")
    public static <C extends SemVerConstraint<? extends ConstraintPattern<? extends Enum<?>>>> C parse(
            @Nonnull Class<C> constraintClass,
            @Nullable String versionConstraint
    ) {
        try {
            // Check class itself
            final int classModifier = constraintClass.getModifiers();
            assert Modifier.isPublic(classModifier)
                    && !Modifier.isAbstract(classModifier)
                    && !Modifier.isInterface(classModifier);

            // Check constructor
            assert constraintClass.getConstructors().length == 0; // No public constructor
            assert constraintClass.getDeclaredConstructors().length > 0; // Non-public constructor

            // Check defined annotation and get which parse will be used
            final SemVerConstraintParser svcpa = constraintClass.getAnnotation(SemVerConstraintParser.class);
            assert svcpa != null && svcpa.enabled();
            assert !svcpa.parserMethodName().isBlank() // No empty
                    && Pattern.matches("^[A-Za-z_$][A-Za-z0-9_$]*$", svcpa.parserMethodName()); // Valid naming

            // Get parser
            final Method parser = constraintClass.getMethod(svcpa.parserMethodName(), String.class);
            final int parserModifier = parser.getModifiers();
            assert Modifier.isPublic(parserModifier)
                    && Modifier.isStatic(parserModifier);
            assert parser.getReturnType().equals(constraintClass); // Must be return same type

            return (C) parser.invoke(null, versionConstraint);
        } catch (Exception | AssertionError e) {
            throw new UnsupportedOperationException(
                    "'" + constraintClass.getName() + "' is not allows to use this parser.",
                    e
            );
        }
    }
}
