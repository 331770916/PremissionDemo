package com.basecommon;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.lib.weight.magicindicator.MagicIndicator;
import com.lib.weight.magicindicator.ViewPagerHelper;
import com.lib.weight.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.lib.weight.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.lib.weight.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.lib.weight.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.lib.weight.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.lib.weight.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.lib.weight.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public abstract class BaseViewPagerActivity extends BaseActivity {

    private List<Fragment> fragmentList;
    private List<String> mTabList;
    private ViewPager mViewPager;

    @Override
    public void initView() {
        settingActionBar();

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentList = getFragmentList();
        BaseViewPagerFragmentAdapter adapter = new BaseViewPagerFragmentAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.setFragments(fragmentList);
        mViewPager.setAdapter(adapter);


        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);

        mTabList = Arrays.asList(getTabs());
        if (mTabList == null || mTabList.size() <= 0) {
            return;
        }

        magicIndicator.setBackgroundColor(getTabBackground());
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setText(mTabList.get(index));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(Color.WHITE);
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    public abstract List<Fragment> getFragmentList();

    public abstract int getTabBackground();

    public abstract String[] getTabs();

    public abstract void settingActionBar();

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_viewpager;
    }
}
