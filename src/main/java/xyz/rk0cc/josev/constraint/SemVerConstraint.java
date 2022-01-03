package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.NonStandardSemVerException;
import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Version constraint is a definition of {@link SemVer} range which bounding the version to ensure compatibility.
 *
 * @param <E> Definition of {@link ConstraintPattern constraint pattern} that available in the subclass.
 *
 * @since 1.0.0
 */
public abstract class SemVerConstraint<E extends ConstraintPattern<? extends Enum<?>>> implements Serializable {
    /**
     * Version constraint that inserted by user.
     */
    private final String rawConstraint;

    /**
     * The constraint range node.
     */
    private final SemVerConstraintNode start, end;

    /**
     * Pattern uses in the version constraint.
     */
    private final E constraintPattern;

    protected SemVerConstraint(
            @Nonnull E constraintPattern,
            @Nullable String rawConstraint,
            @Nullable SemVerConstraintNode start,
            @Nullable SemVerConstraintNode end
    ) {
        assert constraintPattern.getClass().isEnum();
        assert start == null || start.operator() == '>';
        assert end == null || end.operator() == '<';
        assert start == null || end == null || start.semVer().isLowerOrEquals(end.semVer());

        this.rawConstraint = rawConstraint;
        this.constraintPattern = constraintPattern;
        this.start = start;
        this.end = end;
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

    /**
     * A constraint node which specify the minimum version that can be accepted (Assume no version omitted between
     * start and {@link #end() start}).
     *
     * @return A node of version information that can be assembled to traditional syntax.
     *
     * @see #end()
     */
    @Nullable
    public final SemVerConstraintNode start() {
        return start;
    }

    /**
     * A constraint node which specify the maximum version that can be accepted (Assume no version omitted between
     * {@link #start() start} and end).
     *
     * @return A node of version information that can be assembled to traditional syntax.
     *
     * @see #start()
     */
    @Nullable
    public final SemVerConstraintNode end() {
        return end;
    }

    /**
     * Check this <code>semVer</code> is in the constraint.
     *
     * @param semVer A version that to determine is in the constraint range.
     *
     * @return <code>true</code> if in range.
     */
    public boolean isInConstraint(@Nonnull SemVer semVer) {
        final boolean afterStart = start() == null || (start().orEquals()
                ? start().semVer().isLowerOrEquals(semVer)
                : start().semVer().isLower(semVer));

        final boolean beforeEnd = end() == null || (end().orEquals()
                ? end().semVer().isGreaterOrEquals(semVer)
                : end().semVer().isGreater(semVer));

        return afterStart && beforeEnd;
    }

    /**
     * Check this <code>semVer</code> is in the constraint.
     *
     * @param semVer A version that to determine is in the constraint range.
     *
     * @return <code>true</code> if in range.
     *
     * @throws NonStandardSemVerException When <code>semVer</code> {@link String} can not be
     *                                    {@link SemVer#parse(String) parsed}.
     */
    public final boolean isInConstraint(@Nonnull String semVer) throws NonStandardSemVerException {
        return this.isInConstraint(SemVer.parse(semVer));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVerConstraint<?> that = (SemVerConstraint<?>) o;
        return Objects.equals(rawConstraint, that.rawConstraint)
                && Objects.equals(start, that.start)
                && Objects.equals(end, that.end)
                && constraintPattern.equals(that.constraintPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawConstraint, start, end, constraintPattern);
    }

    @Override
    public String toString() {
        return "SemVerConstraint{" +
                "rawConstraint='" + rawConstraint + '\'' +
                ", start=" + start +
                ", end=" + end +
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
