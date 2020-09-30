package com.lib.weight.treelist;

import java.util.List;

public interface Tree<T extends Tree> {
    int getLevel();

    List<T> getChilds();

    boolean isExpand();
}
