package xyz.rk0cc.josev;

import javax.annotation.Nonnull;

/**
 * This interface allows determining {@link SemVer} is in the range.
 *
 * @since 1.0.0
 */
public interface SemVerDetermineInRage {
    /**
     * Check this <code>semVer</code> is in the range.
     * <br/>
     * Mostly, pre-release version is not consider in the range. It will has proper document if this accepts pre-release
     * of {@link SemVer}.
     *
     * @param semVer A version that to determine is in the constraint range.
     *
     * @return <code>true</code> if in range.
     */
    boolean isInRange(@Nonnull SemVer semVer);

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
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    default boolean isInRange(@Nonnull String semVer) throws NonStandardSemVerException {
        return isInRange(SemVer.parse(semVer));
    }
}