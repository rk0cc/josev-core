package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * A {@link Set} based with {@link SemVerRange} (including {@link xyz.rk0cc.josev.constraint.SemVerConstraint}) for
 * storing multiple condition of version constraints.
 *
 * @param <R> Range of SemVer to contains constraints.
 *
 * @since 3.0.0
 */
public abstract class SemVerMultipleRange<R extends SemVerRange>
        implements Set<R>, SemVerDetermineInRange, Serializable {
    /**
     * Range item.
     */
    private final HashSet<R> ranges;

    /**
     * Create new multiple range from existed {@link Set}.
     *
     * @param ranges Existed set of {@link SemVerRange} or inherited classes.
     */
    public SemVerMultipleRange(@Nonnull Set<R> ranges) {
       this.ranges = new HashSet<>(ranges);
    }

    /**
     * Create empty range of multiple range.
     */
    public SemVerMultipleRange() {
        this.ranges = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnegative
    @Override
    public final int size() {
        return ranges.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isEmpty() {
        return ranges.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean contains(@Nonnull Object o) {
        return ranges.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final Iterator<R> iterator() {
        return ranges.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void forEach(@Nonnull Consumer<? super R> action) {
        ranges.forEach(action);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public final Object[] toArray() {
        return ranges.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    @Nonnull
    @Override
    public final <T> T[] toArray(@Nonnull T[] a) {
        return ranges.toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public final <T> T[] toArray(@Nonnull IntFunction<T[]> generator) {
        return ranges.toArray(generator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean add(@Nonnull R r) {
        return ranges.add(r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean remove(@Nonnull Object o) {
        return ranges.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean containsAll(@Nonnull Collection<?> c) {
        return ranges.containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean addAll(@Nonnull Collection<? extends R> c) {
        return ranges.addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean retainAll(@Nonnull Collection<?> c) {
        return ranges.retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean removeAll(@Nonnull Collection<?> c) {
        return ranges.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean removeIf(@Nonnull Predicate<? super R> filter) {
        return ranges.removeIf(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void clear() {
        ranges.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spliterator<R> spliterator() {
        return ranges.spliterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Stream<R> stream() {
        return ranges.stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Stream<R> parallelStream() {
        return ranges.parallelStream();
    }

    /**
     * Get corresponded index of {@link Set} of {@link R}.
     *
     * @param index Index of {@link Set}.
     *
     * @return A {@link R range} object in the list.
     */
    @Nonnull
    public final R elementAt(@Nonnegative int index) {
        Optional<R> optR = ranges.stream().skip(index).findFirst();
        if (optR.isEmpty()) throw new IndexOutOfBoundsException(index);
        return optR.get();
    }

    /**
     * Get a {@link List} of {@link R} which matching the <code>condition</code>.
     *
     * @param condition Condition of searching.
     *
     * @return A {@link List} that matched the <code>condition</code>.
     */
    @Nonnull
    public final List<R> where(@Nonnull Predicate<R> condition) {
        return ranges.stream().filter(condition).toList();
    }
}
