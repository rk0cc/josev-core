package xyz.rk0cc.josev;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An immutable Java object for Semantic Versioning 2.0.0.
 * <br/>
 * Semantic Versioning must be followed this versioning method:
 * <br/><br/>
 * &emsp;&emsp;<code>Major.Minor.Patch(-Pre release)(+build tag)</code>
 * <br/><br/>
 * Major, minor and patch number fields are mandatory and non-negative number. It may optionally provide pre-release or
 * build tag if needed.
 * <br/>
 * In additions, it implemented {@link Comparable} which following version ordering policy.
 *
 * @apiNote The number of major, minor and patch version are unlimited according to official document. However, due to
 *          Java {@link Long} is signed with <code>9223372036854775807</code> as maximum value, applying exceeded value
 *          causes {@link NumberFormatException} in JVM. Even though the versioning is following the standard.
 *
 * @since 1.0.0
 *
 * @see <a href="https://semver.org/spec/v2.0.0.html">Semantic Versioning 2.0.0 documentation</a>
 */
public final class SemVer implements Comparable<SemVer>, Serializable {
    /**
     * A {@link String} of Semantic Versioning regex which will be applied on {@link Pattern#compile(String)}.
     */
    public static final String SEMVER_REGEX = "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)"
            + "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?"
            + "(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?";

    /**
     * Compiling a {@link Pattern} to validate {@link SemVer}.
     *
     * @return Compiled {@link Pattern} from {@link #SEMVER_REGEX} including start(<code>^</code>) and
     *         end(<code>$</code>) anchor.
     */
    @Nonnull
    private static Pattern compiledSemverRegex() {
        return Pattern.compile("^" + SEMVER_REGEX + "$");
    }

    /**
     * Mandatory {@link Nonnegative} of {@link Long} number of versioning.
     */
    private final long major, minor, patch;
    /**
     * Optional {@link String} of version tag which is {@link Nullable}.
     */
    private final String preRelease, build;

    /**
     * Create new versioning data.
     *
     * @param major Non-negative number of major release.
     * @param minor Non-negative number of minor release.
     * @param patch Non-negative number of patch release.
     * @param preRelease Pre-release tag (if applied).
     * @param build Build tag (if applied).
     *
     * @throws NonStandardSemVerException If any {@link Throwable} thrown when calling {@link #value()} that does not
     *                                    meet {@link #SEMVER_REGEX the regex of Semantic Versioning}.
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(
            @Nonnegative long major,
            @Nonnegative long minor,
            @Nonnegative long patch,
            @Nullable String preRelease,
            @Nullable String build
    ) throws NonStandardSemVerException {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = preRelease;
        this.build = build;

        // Ensure is validate SemVer string
        try {
            value();
        } catch (AssertionError e) {
            throw new NonStandardSemVerException($value(), e);
        }
    }

    /**
     * Create new versioning data without additional tag applied.
     *
     * @param major Non-negative number of major release.
     * @param minor Non-negative number of minor release.
     * @param patch Non-negative number of patch release.
     *
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(@Nonnegative long major, @Nonnegative long minor, @Nonnegative long patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.preRelease = null;
        this.build = null;
    }

    /**
     * Create new versioning data.
     *
     * @param major Non-negative number of major release.
     * @param minor Non-negative number of minor release.
     * @param preRelease Pre-release tag (if applied).
     * @param build Build tag (if applied).
     *
     * @throws NonStandardSemVerException If any {@link Throwable} thrown when calling {@link #value()} that does not
     *                                    meet {@link #SEMVER_REGEX the regex of Semantic Versioning}.
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(
            @Nonnegative long major,
            @Nonnegative long minor,
            @Nullable String preRelease,
            @Nullable String build
    ) throws NonStandardSemVerException {
        this(major, minor, 0, preRelease, build);
    }

    /**
     * Create new versioning data without additional tag applied.
     *
     * @param major Non-negative number of major release.
     * @param minor Non-negative number of minor release.
     *
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(@Nonnegative long major, @Nonnegative long minor) {
        this(major, minor, 0);
    }

    /**
     * Create new versioning data.
     *
     * @param major Non-negative number of major release.
     * @param preRelease Pre-release tag (if applied).
     * @param build Build tag (if applied).
     *
     * @throws NonStandardSemVerException If any {@link Throwable} thrown when calling {@link #value()} that does not
     *                                    meet {@link #SEMVER_REGEX the regex of Semantic Versioning}.
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(@Nonnegative long major, @Nullable String preRelease, @Nullable String build)
            throws NonStandardSemVerException {
        this(major, 0, preRelease, build);
    }

    /**
     * Create new versioning data without additional tag applied.
     *
     * @param major Non-negative number of major release.
     *
     * @throws NumberFormatException When parsing <code>major</code>, <code>minor</code> and <code>patch</code> with
     *                               exceeding {@link Long}'s maximum (signed) value even it valid to parse.
     */
    public SemVer(@Nonnegative long major) {
        this(major, 0);
    }

    /**
     * Get major release of this versioning.
     *
     * @return Non-negative {@link Long} value that representing major release.
     */
    @Nonnegative
    public long major() {
        return major;
    }

    /**
     * Get minor release of this versioning.
     *
     * @return Non-negative {@link Long} value that representing minor release.
     */
    @Nonnegative
    public long minor() {
        return minor;
    }

    /**
     * Get patch release of this versioning.
     *
     * @return Non-negative {@link Long} value that representing patch release.
     */
    @Nonnegative
    public long patch() {
        return patch;
    }

    /**
     * Get pre-release tag of this versioning.
     *
     * @return A {@link String} of pre-release, <code>null</code> if not applied.
     */
    @Nullable
    public String preRelease() {
        return preRelease;
    }

    /**
     * Get build tag of this versioning.
     *
     * @return A {@link String} of build, <code>null</code> if not applied.
     */
    @Nullable
    public String build() {
        return build;
    }

    /**
     * Determine this versioning is still under development.
     *
     * @return Check this versioning is applied non-null {@link #preRelease()} or set {@link #major()} as
     *         <code>0</code>.
     */
    public boolean isPreRelease() {
        return preRelease != null || major == 0;
    }

    /**
     * Check this version is greater than <code>compare</code>
     *
     * @param compare Another {@link SemVer} which using to compare with this.
     *
     * @return <code>true</code> if {@link #compareTo(SemVer)} return greater than <code>0</code>.
     */
    public boolean isGreater(@Nonnull SemVer compare) {
        return this.compareTo(compare) > 0;
    }

    /**
     * Check this version is lower than <code>compare</code>
     *
     * @param compare Another {@link SemVer} which using to compare with this.
     *
     * @return <code>true</code> if {@link #compareTo(SemVer)} return lowerer than <code>0</code>.
     */
    public boolean isLower(@Nonnull SemVer compare) {
        return this.compareTo(compare) < 0;
    }

    /**
     * Check this version is greater or equals with <code>compare</code>
     *
     * @param compare Another {@link SemVer} which using to compare with this.
     *
     * @return <code>true</code> if {@link #compareTo(SemVer)} return greater or equals with <code>0</code>.
     */
    public boolean isGreaterOrEquals(@Nonnull SemVer compare) {
        return this.isGreater(compare) || this.equals(compare);
    }

    /**
     * Check this version is lowerer or equals with <code>compare</code>
     *
     * @param compare Another {@link SemVer} which using to compare with this.
     *
     * @return <code>true</code> if {@link #compareTo(SemVer)} return lower or equals with <code>0</code>.
     */
    public boolean isLowerOrEquals(@Nonnull SemVer compare) {
        return this.isLower(compare) || this.equals(compare);
    }

    /**
     * Assemble {@link #major}, {@link #minor}, {@link #patch}, {@link #preRelease} and {@link #build} to
     * {@link String} without validate with {@link #SEMVER_REGEX}.
     *
     * @return An unchecked {@link String} of semver value.
     */
    @Nonnull
    private String $value() {
        StringBuilder builder = new StringBuilder()
                .append(major)
                .append('.')
                .append(minor)
                .append('.')
                .append(patch);
        if (preRelease != null) builder.append('-').append(preRelease);
        if (build != null) builder.append('+').append(build);
        return builder.toString();
    }

    /**
     * Return a {@link String} of version which follows Semantic Versioning standard.
     *
     * @return An assembled {@link String} of Semantic Versioning.
     */
    @SuppressWarnings("UnusedReturnValue")
    @Nonnull
    public String value() {
        final String v = this.$value();
        assert compiledSemverRegex().matcher(v).matches();
        return v;
    }

    /**
     * {@inheritDoc}
     *
     * @apiNote The comparing order is following with this:
     *          <ol>
     *              <li>{@link #major() Major version}</li>
     *              <li>{@link #minor() Minor version}</li>
     *              <li>{@link #patch() Patch version}</li>
     *              <li>{@link #build() Build tag}</li>
     *              <li>{@link #preRelease() Pre-release tag} (reversed)</li>
     *          </ol>
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public int compareTo(@Nonnull SemVer o) {
        // Compare major
        if (major > o.major) return 1;
        else if (major < o.major) return -1;

        // Compare minor
        if (minor > o.minor) return 1;
        else if (minor < o.minor) return -1;

        // Compare patch
        if (patch > o.patch) return 1;
        else if (patch < o.patch) return -1;

        // Compare build
        if (build != null && o.build != null) {
            final long bdiff = build.compareTo(o.build);
            if (bdiff != 0) return bdiff > 0 ? 1 : -1;
        }
        else if (build != null && o.build == null) return 1;
        else if (build == null && o.build != null) return -1;

        // Compare pre-release (Reversed condition of 'build' one)
        if (preRelease != null && o.preRelease != null) {
            final long pdiff = preRelease.compareTo(o.preRelease);
            if (pdiff != 0) return pdiff > 0 ? -1 : 1;
        }
        else if (preRelease != null && o.preRelease == null) return -1;
        else if (preRelease == null && o.preRelease != null) return 1;
        
        // Meaning this semver is exact same version.
        return 0;
    }

    /**
     * Compare another {@link SemVer} which has equals data of versioning.
     *
     * @param o Another {@link Object} that using to compare.
     *
     * @return <code>true</code> if <code>o</code> contains exact same data of versioning.
     *
     * @apiNote Since it is inherited from {@link Object#equals(Object)}, the parameter type can not be specified and
     *          remain {@link Object}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemVer semVer = (SemVer) o;
        return this.compareTo(semVer) == 0;
    }

    /**
     * Calculating hash code form all provided versioning data.
     *
     * @return Hashed {@link #major()}, {@link #minor()}, {@link #patch()}, {@link #preRelease()} and {@link #build()}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, preRelease, build);
    }

    /**
     * Return a {@link String} of object-like context for logging purpose.
     *
     * @apiNote It does not return a {@link String} of {@link #value() versioning}.
     *
     * @return Completed information of versioning value.
     */
    @Nonnull
    @Override
    public String toString() {
        return "SemVer{" +
                "major=" + major +
                ", minor=" + minor +
                ", patch=" + patch +
                ", preRelease='" + preRelease + '\'' +
                ", build='" + build + '\'' +
                '}';
    }

    /**
     * Generating a new {@link SemVer} object by a {@link String}.
     *
     * @param version A {@link String} of Semantic Versioning.
     *
     * @return A Java object of {@link SemVer Semantic Versioning}.
     *
     * @throws NonStandardSemVerException If <code>version</code> does not follows the standard of Semantic Versioning.
     *                                    Or encounter a problem when handling data in JVM.
     *
     * @see #tryParse(String)
     */
    @SuppressWarnings("AssertWithSideEffects")
    @Nonnull
    public static SemVer parse(@Nonnull String version) throws NonStandardSemVerException {
        final String v = version.charAt(0) == 'v' ? version.substring(1) : version;
        try {
            final Matcher vm = compiledSemverRegex().matcher(v);
            assert vm.matches();

            // Extract string from group
            ArrayList<String> matchedGroup = new ArrayList<>();
            int g = 1; // Must be started from 1, 0 is entire version string
            while (true) {
                try {
                    matchedGroup.add(vm.group(g++));
                } catch (IndexOutOfBoundsException ioobe) {
                    // Stop append if reached maximum
                    break;
                }
            }

            // 3 mandatory part
            final long major, minor, patch;
            try {
                // It should be decimal, otherwise it has been thrown already.
                major = Long.parseLong(matchedGroup.get(0), 10);
                minor = Long.parseLong(matchedGroup.get(1), 10);
                patch = Long.parseLong(matchedGroup.get(2), 10);
            } catch (IndexOutOfBoundsException isvd) {
                throw new AssertionError(isvd);
            }

            switch (matchedGroup.size()) {
                case 3:
                    return new SemVer(major, minor, patch);
                case 4:
                    final String pob = matchedGroup.get(3);
                    if (v.contains("-")) return new SemVer(major, minor, patch, pob, null);
                    else if (v.contains("+")) return new SemVer(major, minor, patch, null, pob);
                    else throw new AssertionError("Unable to define pre-release or build tag");
                case 5:
                    return new SemVer(major, minor, patch, matchedGroup.get(3), matchedGroup.get(4));
                default:
                    throw new AssertionError("Unexpected number of semver pattern group found");
            }
        } catch (Throwable t) {
            throw (t instanceof NonStandardSemVerException nt) ? nt : new NonStandardSemVerException(version, t);
        }
    }

    /**
     * Generating a new {@link SemVer} object by a {@link String}. However, when {@link NonStandardSemVerException}
     * throw on {@link #parse(String)}, it returns <code>null</code>/
     *
     * @param version A {@link String} of version.
     *
     * @return A Java object of {@link SemVer Semantic Versioning}. Or <code>null</code> if <code>version</code> causes
     *         {@link #parse(String)} throws {@link NonStandardSemVerException}.
     */
    @Nullable
    public static SemVer tryParse(@Nonnull String version) {
        try {
            return parse(version);
        } catch (NonStandardSemVerException e) {
            return null;
        }
    }
}
