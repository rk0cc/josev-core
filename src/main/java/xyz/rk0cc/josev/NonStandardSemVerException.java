package xyz.rk0cc.josev;

import javax.annotation.Nonnull;

public final class NonStandardSemVerException extends Exception {
    public final String invalidVersion;

    NonStandardSemVerException(@Nonnull String invalidVersion, @Nonnull Throwable t) {
        super("SemVer found an invalid string of version which it does not follow the standard.", t);
        this.invalidVersion = invalidVersion;
    }

    @Nonnull
    @Override
    public String toString() {
        return super.toString()
                + "\n\nApplied version: " + invalidVersion;
    }
}
