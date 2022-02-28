package xyz.rk0cc.josev.constraint;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * This {@link Enum} is designed for {@link SemVerConstraint} which unable to define {@link ConstraintPattern pattern}.
 * <br/>
 * It mostly uses for mixed multiple constraint pattern allows platform like NPM which it becomes complicated when
 * forcing to define {@link ConstraintPattern}. Therefore, if the implemented platform's constraint pattern can be ease
 * to define, do not uses {@link UnsupportedConstraintPattern}.
 *
 * @since 3.2.0
 */
public enum UnsupportedConstraintPattern implements ConstraintPattern<UnsupportedConstraintPattern> {
    /**
     * The one and only {@link Enum} value provides in this {@link ConstraintPattern}.
     * <br/>
     * It declared as no constraint pattern definition support for {@link SemVerConstraint}.
     */
    UNSUPPORTED;

    /**
     * {@inheritDoc}
     *
     * @return A {@link Predicate} interface but throws {@link UnsupportedOperationException} when
     * {@link Predicate#test(Object) invoked}.
     */
    @Override
    public final Predicate<String> conditionFunction() {
        return (s) -> {
            throw new UnsupportedOperationException();
        };
    }

    /**
     * {@inheritDoc}
     *
     * @return Always <code>false</code>, no exception.
     */
    @Override
    public final boolean acceptParseNull() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return Always <code>false</code>, no exception.
     */
    @Override
    public final boolean isValidConstraintMethods(@Nullable String versionConstraint) {
        return false;
    }
}
