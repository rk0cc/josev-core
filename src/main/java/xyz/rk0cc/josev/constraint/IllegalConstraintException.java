package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class IllegalConstraintException extends IllegalArgumentException {
    private final SemVerConstraintNode start, end;

    public IllegalConstraintException(
            @Nonnull String message,
            @Nullable SemVerConstraintNode start,
            @Nullable SemVerConstraintNode end
    ) {
        super(message);
        this.start = start;
        this.end = end;
    }

    @Nullable
    public SemVer startSemVer() {
        return start == null ? null : start.semVer();
    }

    @Nullable
    public SemVer endSemVer() {
        return end == null ? null : end.semVer();
    }

    @Nonnull
    @Override
    public String toString() {
       return super.toString()
               + "\n\nVersion constraint from: " + (start == null ? null : start.traditionalExpression())
               + "\nVersion constraint to: " + (end == null ? null : end.traditionalExpression());
    }
}
