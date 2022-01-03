package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVerConstraintNode that = (SemVerConstraintNode) o;
        return operator == that.operator && orEquals == that.orEquals && semVer.equals(that.semVer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(semVer, operator, orEquals);
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
