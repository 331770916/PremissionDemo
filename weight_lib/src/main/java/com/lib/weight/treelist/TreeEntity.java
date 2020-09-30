package com.lib.weight.treelist;

import java.util.List;
import java.util.Map;

public class TreeEntity extends TreeItem {

    private int mLevel;
    private String mTitle;
    private List<TreeEntity> mChilds;

    public void  setLevel(int level) {
        mLevel = level;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setChilds(List<TreeEntity> childs) {
        mChilds = childs;
    }

    @Override
    public int getLevel() {
        return mLevel;
    }

    @Override
    public List getChilds() {
        return mChilds != null && mChilds.size() > 0 ? mChilds : null;
    }
}
