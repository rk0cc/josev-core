package xyz.rk0cc.josev;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Define range of the {@link SemVer}.
 * <br/>
 * Since {@link SemVer} contains {@link SemVer#preRelease() pre-release} and {@link SemVer#build() build} tag which they
 * are {@link String} and difficult to predict next {@link SemVer} in a {@link java.util.Collection}. As a result,
 * determine is in range with {@link SemVer#isGreater(SemVer)}, {@link SemVer#isLower(SemVer)} or more.
 * <br/>
 * Using to assign version constraint class {@link xyz.rk0cc.josev.constraint.SemVerConstraint} is inherited from
 * {@link SemVerRange}.
 *
 * @since 1.0.0
 */
public class SemVerRange implements Serializable, SemVerDetermineInRange {
    /**
     * The range node.
     */
    private final SemVerRangeNode start, end;

    /**
     * Create new range definition of {@link SemVer}.
     *
     * @param start The range start from.
     * @param end The range end at.
     */
    public SemVerRange(SemVerRangeNode start, SemVerRangeNode end) {
        assert start == null || start.operator() == '>';
        assert end == null || end.operator() == '<';
        assert start == null || end == null || start.semVer().isLowerOrEquals(end.semVer());

        this.start = start;
        this.end = end;
    }

    /**
     * A node which specify the minimum version that can be accepted (Assume no version omitted between
     * start and {@link #end() start}).
     *
     * @return A node of version information that can be assembled to traditional syntax.
     *
     * @see #end()
     */
    public SemVerRangeNode start() {
        return start;
    }

    /**
     * A node which specify the maximum version that can be accepted (Assume no version omitted between
     * {@link #start() start} and end).
     *
     * @return A node of version information that can be assembled to traditional syntax.
     *
     * @see #start()
     */
    public SemVerRangeNode end() {
        return end;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInRange(@Nonnull SemVer semVer) {
        final boolean afterStart = start() == null || (start().orEquals()
                ? start().semVer().isLowerOrEquals(semVer)
                : start().semVer().isLower(semVer));

        boolean beforeEnd = end() == null || (end().orEquals()
                ? end().semVer().isGreaterOrEquals(semVer)
                : end().semVer().isGreater(semVer));

        if (beforeEnd && end() != null && end().semVer().isSameVersionGroup(semVer))
            beforeEnd = end().semVer().preRelease() != null;

        return afterStart && beforeEnd;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isInRange(@Nonnull String semVer) throws NonStandardSemVerException {
        return SemVerDetermineInRange.super.isInRange(semVer);
    }
}
