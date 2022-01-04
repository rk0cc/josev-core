package xyz.rk0cc.josev;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

public record SemVerRangeNode(@Nonnull SemVer semVer, char operator, boolean orEquals) implements Serializable {
    public SemVerRangeNode(@Nonnull SemVer semVer, char operator, boolean orEquals) {
        assert operator == '<' || operator == '>';
        this.semVer = semVer;
        this.operator = operator;
        this.orEquals = orEquals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVerRangeNode that = (SemVerRangeNode) o;
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
