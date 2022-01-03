package xyz.rk0cc.josev.constraint;

import xyz.rk0cc.josev.NonStandardSemVerException;
import xyz.rk0cc.josev.SemVer;
import xyz.rk0cc.josev.SemVerRangeNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SemVerConstraintParser
public final class SampleConstraint extends SemVerConstraint<SampleConstraintPattern> {
    private SampleConstraint(
            @Nonnull SampleConstraintPattern constraintPattern,
            @Nullable String rawConstraint,
            @Nullable SemVerRangeNode start,
            @Nullable SemVerRangeNode end
    ) {
        super(constraintPattern, rawConstraint, start, end);
    }

    @Nonnull
    public static SampleConstraint parse(@Nullable String versionConstraint) {
        List<SampleConstraintPattern> appliedPattern = Arrays.stream(SampleConstraintPattern.values())
                .filter(p -> p.isValidConstraintMethods(versionConstraint))
                .toList();

        assert appliedPattern.size() == 1;

        final SampleConstraintPattern pattern = appliedPattern.get(0);

        switch (pattern) {
            case TRADITIONAL -> {
                assert versionConstraint != null;
                final String[] spiltedTRange = versionConstraint.split("\\s");
                final Function<String, SemVerRangeNode> tvcNodeMaker = s -> {
                    assert !(s.contains(">") && s.contains("<"));
                    boolean orEquals = s.contains("=");

                    try {
                        return new SemVerRangeNode(
                                SemVer.parse(s.substring(orEquals ? 2 : 1)),
                                s.charAt(0),
                                orEquals
                        );
                    } catch (NonStandardSemVerException nssv) {
                        throw new AssertionError(nssv);
                    }
                };
                switch (spiltedTRange.length) {
                    case 1:
                        final SemVerRangeNode svcn = tvcNodeMaker.apply(spiltedTRange[0]);
                        return svcn.operator() == '>'
                                ? new SampleConstraint(pattern, versionConstraint, svcn, null)
                                : new SampleConstraint(pattern, versionConstraint, null, svcn);
                    case 2:
                        return new SampleConstraint(
                                pattern,
                                versionConstraint,
                                tvcNodeMaker.apply(spiltedTRange[0]),
                                tvcNodeMaker.apply(spiltedTRange[1])
                        );
                    default:
                        throw new AssertionError(
                                "Found illegal traditional constraint '" + versionConstraint + "'"
                        );
                }
            }
            case CARET -> {
                assert versionConstraint != null;
                try {
                    SemVer importedVer = SemVer.parse(versionConstraint.substring(1));
                    return new SampleConstraint(
                            pattern,
                            versionConstraint,
                            new SemVerRangeNode(importedVer, '>', true),
                            new SemVerRangeNode(
                                    new SemVer(importedVer.major() + 1, 0, 0),
                                    '<',
                                    false
                            )
                    );
                } catch (NonStandardSemVerException nssv) {
                    throw new AssertionError(nssv);
                }
            }
            default -> throw new IllegalArgumentException("Found undefined pattern '" + pattern + "'");
        }
    }
}
