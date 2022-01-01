package xyz.rk0cc.josev.constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.regex.Pattern;

public interface ConstraintPattern<E extends Enum<? extends ConstraintPattern<E>>> extends Serializable {
    @Nonnull
    Pattern constraintPattern();

    default boolean acceptParseNull() {
        return false;
    }

    default boolean isValidConstraintMethods(@Nullable String versionConstraint) {
        if (versionConstraint == null) return acceptParseNull();
        return constraintPattern().matcher(versionConstraint).matches();
    }
}
