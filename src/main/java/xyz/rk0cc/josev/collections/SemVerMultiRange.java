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
    private final Set<NonnullSemVerRange> rangeSets;
    private final boolean unmodifiable;

    /**
     * Create an empty definitions of {@link SemVerMultiRange}.
     */
    public SemVerMultiRange() {
        this.rangeSets = new HashSet<>();
        this.unmodifiable = false;
    }

    /**
     * Create new {@link SemVerMultiRange} from exist data.
     *
     * @param origin Original data of {@link SemVerMultiRange}
     * @param unmodifiable Apply origin's {@link SemVerMultiRange#rangeSets} via {@link Set#copyOf(Collection)} to
     *                     disable modification in this instance.
     */
    private SemVerMultiRange(@Nonnull SemVerMultiRange origin, boolean unmodifiable) {
        Set<NonnullSemVerRange> originRS = new HashSet<>(origin.rangeSets);
        this.rangeSets = unmodifiable
                ? Set.copyOf(originRS)
                : originRS;
        this.unmodifiable = unmodifiable;
    }

    /**
     * {@inheritDoc}
     */
    @Nonnegative
    @Override
    public int size() {
        return rangeSets.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return rangeSets.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return rangeSets.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Iterator<NonnullSemVerRange> iterator() {
        return rangeSets.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Object[] toArray() {
        return rangeSets.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    @Nonnull
    @Override
    public <T> T[] toArray(@Nonnull T[] a) {
        return rangeSets.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(@Nonnull NonnullSemVerRange nonnullSemVerRange) {
        return rangeSets.add(nonnullSemVerRange);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return rangeSets.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return rangeSets.containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(@Nonnull Collection<? extends NonnullSemVerRange> c) {
        return rangeSets.addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        return rangeSets.retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return rangeSets.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        rangeSets.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spliterator<NonnullSemVerRange> spliterator() {
        return rangeSets.spliterator();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return rangeSets.toArray(generator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeIf(Predicate<? super NonnullSemVerRange> filter) {
        return rangeSets.removeIf(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<NonnullSemVerRange> stream() {
        return rangeSets.stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<NonnullSemVerRange> parallelStream() {
        return rangeSets.parallelStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<? super NonnullSemVerRange> action) {
        rangeSets.forEach(action);
    }

    /**
     * {@inheritDoc}
     *
     * @return <code>true</code> if this {@link SemVer} is inside the range.
     */
    @Override
    public boolean isInRange(@Nonnull SemVer semVer) {
        return stream().anyMatch(msv -> msv.isInRange(semVer));
    }

    /**
     * Determine this {@link SemVerMultiRange} is not allowed {@link #add(NonnullSemVerRange)}, {@link #remove(Object)}
     * and any applying new {@link SemVerRange} to changing state of {@link Set}. It only returns <code>true</code> by
     * parsing {@link #disableModification(SemVerMultiRange)}.
     *
     * @return Check this {@link SemVerMultiRange} disabled modification.
     */
    public boolean isUnmodifiable() {
        return unmodifiable;
    }

    /**
     * Perform shallow copy of multi range.
     *
     * @return Cloned {@link Object} of {@link SemVerMultiRange}.
     *
     * @see HashSet#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new SemVerMultiRange(this, unmodifiable);
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

    /**
     * Generate a {@link SemVerMultiRange} that to generate unmodifiable <code>multiRange</code>.
     *
     * @param multiRange An {@link SemVerMultiRange} that want to disable modification.
     *
     * @return A new instance of {@link SemVerMultiRange} which no longer edit {@link Set} data;
     *
     * @throws IllegalStateException If <code>multiRange</code> {@link SemVerMultiRange#isEmpty()} return
     *                               <code>true</code> which means no constraint definitions is set then disable
     *                               modification.
     */
    @Nonnull
    public static SemVerMultiRange disableModification(@Nonnull SemVerMultiRange multiRange) {
        if (multiRange.isEmpty()) throw new IllegalStateException("Applying empty set of multi range is forbidden");
        return new SemVerMultiRange(multiRange, true);
    }
}
