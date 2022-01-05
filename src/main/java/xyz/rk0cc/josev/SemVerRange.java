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
 * Using to assign version constraint class {@link xyz.rk0cc.josev.constraint.SemVerConstraint} is inherited from
 * {@link SemVerRange.NullableSemVerRange}.
 *
 * @since 1.0.0
 */
public sealed abstract class SemVerRange implements Serializable, SemVerDetermineInRage
        permits SemVerRange.NonnullSemVerRange, SemVerRange.NullableSemVerRange {
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
    protected SemVerRange(SemVerRangeNode start, SemVerRangeNode end) {
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
    SemVerRangeNode start() {
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
    SemVerRangeNode end() {
        return end;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isInRange(@Nonnull String semVer) throws NonStandardSemVerException {
        return SemVerDetermineInRage.super.isInRange(semVer);
    }

    /**
     * A {@link SemVerRange} that {@link Nonnull disallowing parsing <code>null</code>}.
     *
     * @since 1.0.0
     */
    public static non-sealed class NonnullSemVerRange extends SemVerRange {
        /**
         * Create new range definition of {@link SemVer} that disallows null.
         *
         * @param start The range start from.
         * @param end The range end at.
         */
        public NonnullSemVerRange(@Nonnull SemVerRangeNode start, @Nonnull SemVerRangeNode end) {
            super(start, end);
        }

        /**
         * A node which specify the minimum version that can be accepted (Assume no version omitted between
         * start and {@link #end() start}).
         *
         * @return A node of version information that can be assembled to traditional syntax.
         *
         * @see #end()
         */
        @Nonnull
        @Override
        public SemVerRangeNode start() {
            return super.start();
        }

        /**
         * A node which specify the maximum version that can be accepted (Assume no version omitted between
         * {@link #start() start} and end).
         *
         * @return A node of version information that can be assembled to traditional syntax.
         *
         * @see #start()
         */
        @Nonnull
        @Override
        public SemVerRangeNode end() {
            return super.end();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isInRange(@Nonnull SemVer semVer) {
            final boolean afterStart = start().orEquals()
                    ? start().semVer().isLowerOrEquals(semVer)
                    : start().semVer().isLower(semVer);

            boolean beforeEnd = end().orEquals()
                    ? end().semVer().isGreaterOrEquals(semVer)
                    : end().semVer().isGreater(semVer);

            if (beforeEnd && end().semVer().isSameVersionGroup(semVer))
                beforeEnd = end().semVer().preRelease() != null;

            return afterStart && beforeEnd;
        }
    }

    /**
     * A {@link SemVerRange} that {@link Nullable allowing parsing <code>null</code>}.
     *
     * @since 1.0.0
     */
    public static non-sealed class NullableSemVerRange extends SemVerRange {

        /**
         * Create new range definition of {@link SemVer} that allows null.
         *
         * @param start The range start from.
         * @param end The range end at.
         */
        public NullableSemVerRange(@Nullable SemVerRangeNode start, @Nullable SemVerRangeNode end) {
            super(start, end);
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
        @Override
        public SemVerRangeNode start() {
            return super.start();
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
        @Override
        public SemVerRangeNode end() {
            return super.end();
        }

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("ConstantConditions")
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
    }
}
