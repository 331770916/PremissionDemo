package com.lib.weight.picktime.adapter;

import java.util.List;

/**
 * Created by zhangbiao on 2018/8/11.
 */

public class WeekWheelAdapter implements WheelAdapter {

    private List<String> mList;

    public WeekWheelAdapter(List<String> list) {
        mList = list;
    }

    @Override
    public int getItemsCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int index) {
        if (mList != null) {
            return mList.get(index);
        } else {
            return "";
        }
    }

    @Override
    public int indexOf(Object o) {
        return mList.indexOf(o);
    }
}