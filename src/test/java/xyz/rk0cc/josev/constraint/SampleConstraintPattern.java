package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public enum SampleConstraintPattern implements ConstraintPattern<SampleConstraintPattern> {
    TRADITIONAL(Pattern.compile("^("
            + "(>=?" + SemVer.SEMVER_REGEX + ")"
            + "|(<=?" + SemVer.SEMVER_REGEX + ")"
            + "|((>=?" + SemVer.SEMVER_REGEX + ")\\s(<=?" + SemVer.SEMVER_REGEX + "))"
            + ")$")),
    CARET(Pattern.compile("^\\^" + SemVer.SEMVER_REGEX + "$"));

    SampleConstraintPattern(@Nonnull Pattern constraintPattern) {
        this.constraintPattern = constraintPattern;
    }

    private final Pattern constraintPattern;

    @Nonnull
    @Override
    public Pattern constraintPattern() {
        return constraintPattern;
    }

}
