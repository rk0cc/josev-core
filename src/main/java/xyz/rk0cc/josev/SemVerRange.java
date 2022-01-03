package xyz.rk0cc.josev;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Define range of the {@link SemVer}.
 * <br/>
 * Since {@link SemVer} contains {@link SemVer#preRelease() pre-release} and {@link SemVer#build() build} tag which they
 * are {@link String} and difficult to predict next {@link SemVer} in a {@link java.util.Collection}. As a result,
 * determine is in range with {@link SemVer#isGreater(SemVer)}, {@link SemVer#isLower(SemVer)} or more.
 * <br/>
 * Using to assign version constraint class {@link xyz.rk0cc.josev.constraint.SemVerConstraint} is inherited from this
 * class.
 *
 * @since 1.0.0
 */
public class SemVerRange implements Serializable {
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
    public SemVerRange(@Nullable SemVerRangeNode start, @Nullable SemVerRangeNode end) {
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
    @Nullable
    public final SemVerRangeNode start() {
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
    @Nullable
    public final SemVerRangeNode end() {
        return end;
    }

    /**
     * Check this <code>semVer</code> is in the range.
     *
     * @param semVer A version that to determine is in the constraint range.
     *
     * @return <code>true</code> if in range.
     */
    public boolean isInRange(@Nonnull SemVer semVer) {
        final boolean afterStart = start() == null || (start().orEquals()
                ? start().semVer().isLowerOrEquals(semVer)
                : start().semVer().isLower(semVer));

        final boolean beforeEnd = end() == null || (end().orEquals()
                ? end().semVer().isGreaterOrEquals(semVer)
                : end().semVer().isGreater(semVer));

        return afterStart && beforeEnd;
    }

    /**
     * Check this <code>semVer</code> is in the range.
     *
     * @param semVer A version that to determine is in the constraint range.
     *
     * @return <code>true</code> if in range.
     *
     * @throws NonStandardSemVerException When <code>semVer</code> {@link String} can not be
     *                                    {@link SemVer#parse(String) parsed}.
     */
    public final boolean isInRange(@Nonnull String semVer) throws NonStandardSemVerException {
        return this.isInRange(SemVer.parse(semVer));
    }
}
