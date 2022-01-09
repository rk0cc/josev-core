package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;
import xyz.rk0cc.josev.SemVerRange;
import xyz.rk0cc.josev.SemVerRangeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Version constraint is a definition of {@link SemVer} range which bounding the version to ensure compatibility. This,
 * class in extended from {@link SemVerRange} with more complicated detection to determine which
 * {@link SemVer} can be accepted depending on different rules.
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
     * Receive annotation on the parser.
     *
     * @param constraintParser A {@link Class} that using for parser.
     *
     * @return {@link SemVerConstraintParser} from parser {@link Class}.
     */
    @Nonnull
    private static SemVerConstraintParser extractParserAnnotation(@Nonnull Class<?> constraintParser) {
        SemVerConstraintParser annotation = constraintParser.getAnnotation(SemVerConstraintParser.class);

        assert annotation != null;
        assert annotation.enabled();
        assert Pattern.matches("^[a-zA-z$_][a-zA-z0-9$_]*$", annotation.parserMethodName());

        return annotation;
    }

    /**
     * Get a {@link Method} which is static and uses to {@link Method#invoke(Object, Object...) invoke} parser.
     *
     * @param constraintParser A {@link Class} of parser with annotated {@link SemVerConstraintParser}.
     *
     * @return Parser method.
     *
     * @throws NoSuchMethodException If this parser does not contains parser.
     */
    @Nonnull
    private static Method getParserMethod(@Nonnull Class<?> constraintParser) throws NoSuchMethodException {
        SemVerConstraintParser parserAnnotation = extractParserAnnotation(constraintParser);
        final Method parser = constraintParser.getMethod(parserAnnotation.parserMethodName(), String.class);
        final int methodModifier = parser.getModifiers();

        assert Modifier.isPublic(methodModifier) && Modifier.isStatic(methodModifier);
        assert parser.getReturnType().equals(constraintParser);

        return parser;
    }

    /**
     * Check current parser is valid to use {@link #parse(Class, String)}.
     *
     * @param constraintParser Targeted {@link Class}.
     * @param withAbstract Include abstract parser or not
     *
     * @return <code>true</code> if this parser {@link Class} is meet the requirement.
     */
    private static boolean checkValidParser(@Nonnull Class<?> constraintParser, boolean withAbstract) {
        final int classModifier = constraintParser.getModifiers();

        // Check class type
        if (!Modifier.isPublic(classModifier) || Modifier.isInterface(classModifier)) return false;

        // Handle abstract
        final boolean absParser = Modifier.isAbstract(classModifier);
        if (!withAbstract && absParser) return false;
        else if (!(absParser ? constraintParser.isSealed() : Modifier.isFinal(classModifier))) return false;

        // Get method to validate it's implemented parser
        try {
            getParserMethod(constraintParser);
        } catch (NoSuchMethodException | AssertionError e) {
            return false;
        }

        // Abstract parser handler
        if (absParser) {
            Stream<Class<?>> permitClassStream = Arrays.asList(constraintParser.getPermittedSubclasses())
                    .parallelStream();

            return permitClassStream.allMatch(pc -> checkValidParser(pc, false));
        }

        return true;
    }

    /**
     * Parser a {@link String} of <code>versionConstraint</code> and specify which {@link Class} that extended from
     * {@link SemVerConstraint} will be exported.
     * <br/>
     * <b style="font-size: 18px;">Not all {@linkplain Class} which inherited from {@linkplain SemVerConstraint} can be
     * parsed by this method unless it meets these requirements:</b>
     * <ul>
     *     <li>
     *         The class must be public.
     *         <ul>
     *             <li>
     *                 If the annotated class is abstracted, it must be sealed and all permitted class should not
     *                 abstract again.
     *             </li>
     *             <li>For implementation class, it must be final.</li>
     *         </ul>
     *     </li>
     *     <li>Do not implement any public scope constructor</li>
     *     <li>
     *         Has a static method that returns the same {@link Class} of <code>constraintClass</code>, it also applied
     *         with abstract parser.
     *     </li>
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
        assert !constraintClass.equals(SemVerConstraint.class); // Do not reference itself
        try {
            assert checkValidParser(constraintClass, true);

            Method parser = getParserMethod(constraintClass);

            return (C) parser.invoke(null, versionConstraint);
        } catch (Exception | AssertionError e) {
            throw new UnsupportedOperationException(
                    "'" + constraintClass.getName() + "' is not allows to use this parser.",
                    e
            );
        }
    }
}
