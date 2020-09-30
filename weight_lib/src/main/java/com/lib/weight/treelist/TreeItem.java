package com.lib.weight.treelist;

public abstract class TreeItem implements Tree {

    private boolean isOpen;

    public final boolean isOpen() {
        return isOpen;
    }

    public final void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public final boolean isExpand() {
        return isOpen;
    }
}
