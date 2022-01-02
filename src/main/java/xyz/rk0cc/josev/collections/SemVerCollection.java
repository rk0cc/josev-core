package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

public sealed abstract class SemVerCollection implements Collection<SemVer>, Cloneable
        permits SemVerList {
    final Collection<SemVer> collection;

    protected SemVerCollection(Collection<SemVer> collection) {
        this.collection = collection;
    }

    @Override
    public final int size() {
        return collection.size();
    }

    @Override
    public final boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public final boolean contains(Object o) {
        return collection.contains(o);
    }

    @Nonnull
    @Override
    public final Iterator<SemVer> iterator() {
        return new Iterator<>() {
            private int cidx = -1;
            private final SemVer[] arr = collection.toArray(new SemVer[]{});

            @Override
            public boolean hasNext() {
                try {
                    SemVer s = arr[cidx + 1];
                    return true;
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }

            @Override
            public SemVer next() {
                return arr[++cidx];
            }
        };
    }

    @Nonnull
    @Override
    public final Object[] toArray() {
        return collection.toArray();
    }


    @SuppressWarnings("SuspiciousToArrayCall")
    @Nonnull
    @Override
    public final <T> T[] toArray(@Nonnull T[] a) {
        return collection.toArray(a);
    }

    @Override
    public final boolean containsAll(@Nonnull Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public final boolean add(SemVer semVer) {
        throw new UnsupportedOperationException("add");
    }

    @Override
    public final boolean remove(Object o) {
        throw new UnsupportedOperationException("remove");
    }

    @Override
    public final boolean addAll(@Nonnull Collection<? extends SemVer> c) {
        throw new UnsupportedOperationException("addAll");
    }

    @Override
    public final boolean removeAll(@Nonnull Collection<?> c) {
        throw new UnsupportedOperationException("removeAll");
    }

    @Override
    public final boolean retainAll(@Nonnull Collection<?> c) {
        throw new UnsupportedOperationException("retainAll");
    }

    @Override
    public final void clear() {
        throw new UnsupportedOperationException("clear");
    }

    @Nonnull
    @Override
    public abstract SemVerCollection clone();
}
