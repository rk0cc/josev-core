package xyz.rk0cc.josev.collections;

import com.google.common.base.Joiner;
import xyz.rk0cc.josev.*;
import xyz.rk0cc.josev.SemVerRange.NonnullSemVerRange;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Provides various of {@link SemVerRange} to determine the {@link SemVer} meet complicated condition.
 * <br/>
 * The {@link SemVerRange} must be {@link NonnullSemVerRange non-nulled}. However, it can not check and merge duplicate
 * {@link SemVerRange} when applying overlapped conditions to ensure the configuration can be revertible.
 *
 * @since 1.0.0
 */
public final class SemVerMultiRange implements Set<NonnullSemVerRange>, SemVerDetermineInRage, Cloneable {
    /**
     * A {@link Set} of applied {@link SemVer}.
     */
    private final HashSet<NonnullSemVerRange> rangeSets;
    private final boolean unmodifiable;

    public SemVerMultiRange() {
        this.rangeSets = new HashSet<>();
        this.unmodifiable = false;
    }

    private SemVerMultiRange(@Nonnull SemVerMultiRange origin, boolean unmodifiable) {
        HashSet<NonnullSemVerRange> originRS = new HashSet<>(origin.rangeSets);
        this.rangeSets = unmodifiable
                ? (HashSet<NonnullSemVerRange>) Set.copyOf(originRS)
                :originRS;
        this.unmodifiable = unmodifiable;
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
        return rangeSets.contains(o);
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
        return rangeSets.containsAll(c);
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
    public Spliterator<NonnullSemVerRange> spliterator() {
        return rangeSets.spliterator();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return rangeSets.toArray(generator);
    }

    @Override
    public boolean removeIf(Predicate<? super NonnullSemVerRange> filter) {
        return rangeSets.removeIf(filter);
    }

    @Override
    public Stream<NonnullSemVerRange> stream() {
        return rangeSets.stream();
    }

    @Override
    public Stream<NonnullSemVerRange> parallelStream() {
        return rangeSets.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super NonnullSemVerRange> action) {
        rangeSets.forEach(action);
    }

    @Override
    public boolean isInRange(@Nonnull SemVer semVer) {
        return stream().anyMatch(msv -> msv.isInRange(semVer));
    }

    public boolean isUnmodifiable() {
        return unmodifiable;
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
            return new SemVerMultiRange(this, false);
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

    @Nonnull
    public static SemVerMultiRange disableModification(@Nonnull SemVerMultiRange multiRange) {
        return new SemVerMultiRange(multiRange, true);
    }
}
