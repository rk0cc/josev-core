package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.io.Serializable;

public record SemVerConstraintNode(@Nonnull SemVer semVer, char operator, boolean orEquals) implements Serializable {
    public SemVerConstraintNode(@Nonnull SemVer semVer, char operator, boolean orEquals) {
        assert operator == '<' || operator == '>';
        this.semVer = semVer;
        this.operator = operator;
        this.orEquals = orEquals;
    }

    @Nonnull
    public String traditionalExpression() {
        StringBuilder b = new StringBuilder();

        // Compare operator
        b.append(operator);

        // Equals assign
        if (orEquals) b.append('=');

        // String of semver
        b.append(semVer.value());

        return b.toString();
    }

    @Nonnull
    @Override
    public String toString() {
        return "SemVerConstraintNode{" +
                "semVer=" + semVer +
                ", operator=" + operator +
                ", orEquals=" + orEquals +
                '}';
    }
}
