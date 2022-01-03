package xyz.rk0cc.josev.collections;

import com.google.common.base.Joiner;
import xyz.rk0cc.josev.*;
import xyz.rk0cc.josev.SemVerRange.NonnullSemVerRange;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;

/**
 * Provides various of {@link xyz.rk0cc.josev.SemVerRange} to determine the {@link SemVer} meet complicated condition.
 *
 * @since 1.0.0
 */
public final class SemVerMultiRange implements Set<NonnullSemVerRange>, SemVerDetermineInRage, Cloneable {
    private final HashSet<NonnullSemVerRange> rangeSets;

    public SemVerMultiRange() {
        this.rangeSets = new HashSet<>();
    }

    private SemVerMultiRange(@Nonnull SemVerMultiRange origin) {
        this.rangeSets = new HashSet<>(origin.rangeSets);
    }

    @Nonnegative
    @Override
    public int size() {
        return rangeSets.size();
    }

    @Override
    public boolean isEmpty() {
        return rangeSets.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Nonnull
    @Override
    public Iterator<NonnullSemVerRange> iterator() {
        return rangeSets.iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return rangeSets.toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        return rangeSets.toArray(a);
    }

    @Override
    public boolean add(@Nonnull NonnullSemVerRange nonnullSemVerRange) {
        return rangeSets.add(nonnullSemVerRange);
    }

    @Override
    public boolean remove(Object o) {
        return rangeSets.remove(o);
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends NonnullSemVerRange> c) {
        return rangeSets.addAll(c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return rangeSets.retainAll(c);
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return rangeSets.removeAll(c);
    }

    @Override
    public void clear() {
        rangeSets.clear();
    }

    @Override
    public boolean isInRange(@Nonnull SemVer semVer) {
        return false;
    }

    @Override
    public boolean isInRange(@Nonnull String semVer) throws NonStandardSemVerException {
        return SemVerDetermineInRage.super.isInRange(semVer);
    }

    /**
     * Perform shallow copy of multi range.
     *
     * @return Cloned {@link Object} of {@link SemVerMultiRange}
     *
     * @see HashSet#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new SemVerMultiRange(this);
        }
    }

    /**
     * Print all added {@link NonnullSemVerRange} to a {@link String}.
     *
     * @return A list of {@link NonnullSemVerRange}.
     */
    @Nonnull
    @Override
    public String toString() {
        return '[' + Joiner.on(", ").join(rangeSets) + ']';
    }
}
