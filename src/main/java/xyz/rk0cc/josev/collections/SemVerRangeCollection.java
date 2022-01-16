package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.SemVerRange;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

/**
 * Enhanced {@link Collection} interface for handling {@link SemVerRange}.
 *
 * @param <R> Applied {@link SemVerRange}.
 *
 * @since 3.1.0
 */
public interface SemVerRangeCollection<R extends SemVerRange> extends Collection<R> {
    /**
     * Get corresponded index of {@link R}'s {@link Collection}.
     *
     * @param index Index of {@link Collection}.
     *
     * @return A {@link R range} object in the list.
     *
     * @throws IndexOutOfBoundsException If index number is out of range.
     */
    @Nonnull
    R elementAt(@Nonnegative int index);

    /**
     * Get a {@link List} of {@link R} which matching the <code>condition</code>.
     *
     * @param condition Condition of searching.
     *
     * @return A {@link List} that matched the <code>condition</code>.
     */
    @Nonnull
    default List<R> where(@Nonnull Predicate<R> condition) {
        return stream().filter(condition).toList();
    }

    /**
     * Parse multiple range to {@link List} based {@link SemVerRangeCollection}.
     *
     * @param ranges Applied ranges.
     * @param <R> Type uses for multiple range.
     *
     * @return {@link ArrayList} based {@link SemVerRangeCollection}.
     */
    @SafeVarargs
    @Nonnull
    static <R extends SemVerRange> SemVerRangeCollection<R> buildList(R... ranges) {
        return new GenericSemVerList<>(Arrays.asList(ranges));
    }

    /**
     * Parse multiple range to {@link List} based {@link SemVerRangeCollection}.
     *
     * @param ranges Applied ranges under a {@link Collection}.
     * @param <R> Type uses for multiple range.
     *
     * @return {@link ArrayList} based {@link SemVerRangeCollection}.
     */
    @Nonnull
    static <R extends SemVerRange> SemVerRangeCollection<R> buildList(Collection<R> ranges) {
        return new GenericSemVerList<>(ranges);
    }

    /**
     * Parse multiple range to {@link Set} based {@link SemVerRangeCollection}.
     *
     * @param ranges Applied ranges.
     * @param <R> Type uses for multiple range.
     *
     * @return {@link LinkedHashSet} based {@link SemVerRangeCollection}.
     */
    @SafeVarargs
    @Nonnull
    static <R extends SemVerRange> SemVerRangeCollection<R> buildSet(R... ranges) {
        return new GenericSemVerSet<>(Arrays.asList(ranges));
    }

    /**
     * Parse multiple range to {@link Set} based {@link SemVerRangeCollection}.
     *
     * @param ranges Applied ranges under a {@link Collection}.
     * @param <R> Type uses for multiple range.
     *
     * @return {@link LinkedHashSet} based {@link SemVerRangeCollection}.
     */
    @Nonnull
    static <R extends SemVerRange> SemVerRangeCollection<R> buildSet(Collection<R> ranges) {
        return new GenericSemVerSet<>(ranges);
    }
}

/**
 * Generic list based for parsing from {@link SemVerRangeCollection#buildList(SemVerRange[])}.
 *
 * @param <R> Applied range.
 *
 * @since 3.1.0
 */
final class GenericSemVerList<R extends SemVerRange> extends ArrayList<R> implements SemVerRangeCollection<R> {
    /**
     * Create new generic list.
     *
     * @param origin Origin collection.
     */
    GenericSemVerList(@Nonnull Collection<R> origin) {
        super(origin);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public R elementAt(@Nonnegative int index) {
        return get(index);
    }
}

/**
 * Generic set based for parsing from {@link SemVerRangeCollection#buildSet(SemVerRange[])}.
 *
 * @param <R> Applied range.
 *
 * @since 3.1.0
 */
final class GenericSemVerSet<R extends SemVerRange> extends LinkedHashSet<R> implements SemVerRangeCollection<R> {
    /**
     * Create new generic set.
     *
     * @param origin Origin collection.
     */
    GenericSemVerSet(@Nonnull Collection<R> origin) {
        super(origin);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public R elementAt(int index) {
        Optional<R> o = stream().skip(index).findFirst();
        if (o.isEmpty()) throw new IndexOutOfBoundsException(index);
        return o.get();
    }
}