package xyz.rk0cc.josev.collections;

import xyz.rk0cc.josev.SemVer;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public final class SemVerList extends SemVerCollection {
    public SemVerList(ArrayList<SemVer> collection) {
        super(collection);
    }

    @Nonnull
    @Override
    public SemVerCollection clone() {
        return new SemVerList(new ArrayList<>(this.collection));
    }
}
