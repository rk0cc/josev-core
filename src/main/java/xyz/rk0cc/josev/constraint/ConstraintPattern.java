package xyz.rk0cc.josev.constraint;

import java.io.Serializable;
import java.util.regex.Pattern;

public interface ConstraintPattern<E extends Enum<? extends ConstraintPattern<E>>> extends Serializable {
    Pattern getConstraintPattern();
}
