package xyz.rk0cc.josev.constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

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
}
