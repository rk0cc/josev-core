package xyz.rk0cc.josev;

import javax.annotation.Nonnull;

/**
 * When {@link SemVer#parse(String)} unable to parse from {@link String} to {@link SemVer} because invalid syntax.
 * <br/>
 * This {@link Exception} can not invoke outside of {@link xyz.rk0cc.josev index package of JOSEV}.
 *
 * @since 1.0.0
 */
public final class NonStandardSemVerException extends Exception {
    /**
     * A {@link String} that does not meet {@link SemVer} format.
     */
    public final String invalidVersion;

    /**
     * Create exception that unable perform parsing {@link SemVer}.
     *
     * @param invalidVersion A {@link String} of invalid version.
     * @param t A {@link Throwable} from {@link SemVer#parse(String)}.
     */
    NonStandardSemVerException(@Nonnull String invalidVersion, @Nonnull Throwable t) {
        super("SemVer found an invalid string of version which it does not follow the standard.", t);
        this.invalidVersion = invalidVersion;
    }

    /**
     * {@inheritDoc}
     * <br/>
     * Finally, it shows {@link #invalidVersion} at the end of line.
     */
    @Nonnull
    @Override
    public String toString() {
        return super.toString()
                + "\n\nApplied version: " + invalidVersion;
    }
}
