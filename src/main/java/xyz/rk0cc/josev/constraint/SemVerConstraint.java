package xyz.rk0cc.josev.constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.*;

public abstract class SemVerConstraint<E extends ConstraintPattern<? extends Enum<?>>> implements Serializable {
    private final String rawConstraint;

    private final SemVerConstraintNode start, end;

    private final E constraintPattern;

    protected SemVerConstraint(
            @Nonnull E constraintPattern,
            @Nullable String rawConstraint,
            @Nullable SemVerConstraintNode start,
            @Nullable SemVerConstraintNode end
    ) {
        assert Enum.class.isAssignableFrom(constraintPattern.getClass());
        assert start == null || start.operator() == '>';
        assert end == null || end.operator() == '<';
        assert start == null || end == null || start.semVer().isLowerOrEquals(end.semVer());

        this.rawConstraint = rawConstraint;
        this.constraintPattern = constraintPattern;
        this.start = start;
        this.end = end;

        if (!isValidConstraint())
            throw new IllegalConstraintException("This constraint can not be applied.", start, end);
    }

    @Nonnull
    public final E constraintPattern() {
        return constraintPattern;
    }

    @Nullable
    public final String rawConstraint() {
        return rawConstraint;
    }

    @Nullable
    public final SemVerConstraintNode start() {
        return start;
    }

    @Nullable
    public final SemVerConstraintNode end() {
        return end;
    }

    protected abstract boolean isValidConstraint();

    /**
     * Parsing version constraint to specific class which inherited from {@link SemVerConstraint}.
     *
     * @param constraintClass Which {@link Class} will be used for parsing <code>versionConstraint</code>.
     * @param versionConstraint A {@link String} of version constraint.
     * @param <C> Specified which {@link Class} will be used that extended with {@link SemVerConstraint}.
     *
     * @apiNote To use this static method, the target of <code>constraintClass</code> must be a non-abstract class with
     *          <code>parse(String)</code> provided.
     *
     * @return An object of {@link C} which representing information of {@link SemVerConstraint}.
     *
     * @throws UnsupportedOperationException If the <code>constraintClass</code> can not be used as return type of
     *                                       {@link SemVerConstraint} which does not meet requirement of API note.
     */
    @SuppressWarnings("unchecked")
    public static <C extends SemVerConstraint<? extends ConstraintPattern<? extends Enum<?>>>> C parse(
            @Nonnull Class<C> constraintClass,
            @Nullable String versionConstraint
    ) {
        try {
            final Method parser = constraintClass.getMethod("parse", String.class);
            final int cModifier = constraintClass.getModifiers();
            final int cPModifier = parser.getModifiers();

            assert  Modifier.isPublic(cModifier)
                    && !Modifier.isAbstract(cModifier)
                    && !Modifier.isInterface(cModifier)
                    && Modifier.isPublic(cPModifier)
                    && Modifier.isStatic(cPModifier);

            return (C) parser.invoke(null, versionConstraint);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | AssertionError e) {
            throw new UnsupportedOperationException(
                    "'" + constraintClass.getName() + "' can not become a parser or no parser found in this class.",
                    e
            );
        }
    }
}
