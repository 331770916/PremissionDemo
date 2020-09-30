package com.premissiondemo;

import android.graphics.Color;
import android.view.View;

import com.basecommon.BaseViewPagerActivity;
import com.premissiondemo.fragments.TestFragment1;
import com.premissiondemo.fragments.TestFragment2;
import com.premissiondemo.fragments.TestFragment3;
import com.premissiondemo.fragments.TestFragment4;
import com.premissiondemo.fragments.TestFragment5;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class VewpagerTestActivity extends BaseViewPagerActivity {

    @Override
    public void settingActionBar() {
        mTopBar.setVisibility(View.VISIBLE);
        mComment_title.setVisibility(View.VISIBLE);
        mComment_title.setViewVisibility(View.GONE, 0);
        mComment_title.setTitle("标题");
    }

    @Override
    public List<Fragment> getFragmentList() {

        TestFragment1 fragment1 = new TestFragment1();
        TestFragment2 fragment2 = new TestFragment2();
        TestFragment3 fragment3 = new TestFragment3();
        TestFragment4 fragment4 = new TestFragment4();
        TestFragment5 fragment5 = new TestFragment5();

        List<Fragment> datas = new ArrayList<>();
        datas.add(fragment1);
        datas.add(fragment2);
        datas.add(fragment3);
        datas.add(fragment4);
        datas.add(fragment5);

        return datas;
    }

    @Override
    public int getTabBackground() {
        return Color.BLUE;
    }

    @Override
    public String[] getTabs() {
        String[] tabs = {"Tab1", "Tab2", "Tab3", "Tab4","Tab5"};
        return tabs;
    }
}
