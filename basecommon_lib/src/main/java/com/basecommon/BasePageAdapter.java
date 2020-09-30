package com.basecommon;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

public class BasePageAdapter extends PagerAdapter {

    private List<Fragment> mDatas;

    public void setDatas(List<Fragment> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mDatas.get(position).getView();
        if (view == null)
            view = new View(container.getContext());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
