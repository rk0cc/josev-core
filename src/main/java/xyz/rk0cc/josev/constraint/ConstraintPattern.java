package xyz.rk0cc.josev.constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * An interface to indicate implemented {@link Enum} as constraint pattern in {@link SemVerConstraint}.
 * <br/>
 * There are various of constraint pattern format on different system. Currently, no standard version constraint pattern
 * has been released that it's very rely on {@link Enum} with {@link ConstraintPattern} implemented to find which
 * pattern is used when calling {@link SemVerConstraint#parse(Class, String) parser}.
 * 
 * @param <E> An {@link Enum} which implemented {@link ConstraintPattern}
 *           
 * @since 1.0.0
 */
public interface ConstraintPattern<E extends Enum<? extends ConstraintPattern<E>>> extends Serializable {
    /**
     * A {@link Function} providing a {@link String} is follow the syntax of version constraint. The {@link String}
     * parameter may be <code>null</code>.
     *
     * @return A {@link Function} to validate version constraint.
     *
     * @since 2.0.0
     */
    Function<String, Boolean> conditionFunction();

    /**
     * Some format of constraint pattern allows parse <code>null</code>. If this method return <code>true</code>, it
     * means, this constraint pattern is accepted <code>null</code>.
     * <br/>
     * By default, it returns <code>false</code> since it rarely to allows to do it. And no duplicated
     * {@link Enum#valueOf(Class, String) Enum's value} can be <code>null</code>.
     *
     * @return Allowing parse <code>null</code> for this constraint pattern format.
     */
    default boolean acceptParseNull() {
        return false;
    }

    /**
     * Determine a {@link String} of version constraint is following current {@link Enum} of {@link ConstraintPattern}
     * or finding which {@link ConstraintPattern} uses when {@link SemVerConstraint#parse(Class, String) parsing} the
     * constraint by {@link java.util.stream.Stream#filter(Predicate) filtering} {@link Enum}.
     *
     * @param versionConstraint A {@link String} (including <code>null</code>) of version constraint.
     *
     * @return Does this <code>versionConstraint</code> matched the constraint pattern.
     */
    default boolean isValidConstraintMethods(@Nullable String versionConstraint) {
        if (versionConstraint == null) return acceptParseNull();
        return conditionFunction().apply(versionConstraint);
    }
}
