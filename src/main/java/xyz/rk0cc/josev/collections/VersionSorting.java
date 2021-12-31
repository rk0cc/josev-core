package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.SemVer;

import java.util.Comparator;

public enum VersionSorting {
    LOW_TO_HIGH(SemVer::compareTo),
    HIGH_TO_LOW(Comparator.reverseOrder());

    public final Comparator<SemVer> comparator;

    VersionSorting(Comparator<SemVer> comparator) {
        this.comparator = comparator;
    }
}
