package com.basecommon;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BaseViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment;                         //fragment列表

    public BaseViewPagerFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setFragments(List<Fragment> fragments) {
        list_fragment = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (list_fragment != null && list_fragment.size() > 0) {
            return list_fragment.get(position);
        }
        return null;

    }

    @Override
    public int getCount() {
        if (list_fragment != null && list_fragment.size() > 0) {
            return list_fragment.size();
        }
        return 0;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
