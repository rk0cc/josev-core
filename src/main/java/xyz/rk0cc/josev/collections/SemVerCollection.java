package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.util.Collection;

public abstract class SemVerCollection implements Collection<SemVer>, Cloneable {
    protected final Collection<SemVer> collection;

    protected SemVerCollection(Collection<SemVer> collection) {
        this.collection = collection;
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
