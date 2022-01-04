package xyz.rk0cc.josev;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A node for {@link SemVerRange} to define preference of the range of {@link SemVer}.
 *
 * @since 1.0.0
 */
public record SemVerRangeNode(@Nonnull SemVer semVer, char operator, boolean orEquals) implements Serializable {
    /**
     * Create new node to define a range of {@link SemVer} to be constrained.
     *
     * @param semVer A {@link SemVer} that uses to be constrained.
     * @param operator An operator that uses to express the range which are '<code>&gt;</code>' or '<code>&lt;</code>'.
     * @param orEquals Allowing same {@link SemVer} is justified or not.
     */
    public SemVerRangeNode(@Nonnull SemVer semVer, char operator, boolean orEquals) {
        assert operator == '<' || operator == '>';
        this.semVer = semVer;
        this.operator = operator;
        this.orEquals = orEquals;
    }

    /**
     * Determine current configuration of {@link SemVerRangeNode} is the same or not.
     *
     * @param o Compare object.
     *
     * @return <code>true</code> if the <code>o</code> is exact same with this node.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVerRangeNode that = (SemVerRangeNode) o;
        return operator == that.operator && orEquals == that.orEquals && semVer.equals(that.semVer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(semVer, operator, orEquals);
    }

    /**
     * Generate node object data under {@link String}.
     *
     * @return A {@link String} of {@link SemVerRangeNode} data.
     */
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
