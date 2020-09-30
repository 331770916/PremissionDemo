package com.lib.weight.banner;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.weight.R;

import androidx.viewpager.widget.ViewPager;

public class AutoSwitchView extends AutoLoopSwitchBaseView {

    private int mType;
    private int marginright = 0;
    private int marginleft = 0;
    private int margintop = 0;
    private int marginbottom = 0;
    private TextView tvTime;

    public AutoSwitchView(Context context) {
        super(context.getApplicationContext());
    }

    public void setType(int mType,int marginleft,int margintop,int marginright,int marginbottom) {
        this.mType = mType;
        this.marginleft = marginleft;
        this.margintop = margintop;
        this.marginbottom = marginbottom;
        this.marginright = marginright;
        initView();
    }
    public void setType(int mType) {
        this.mType = mType;
        initView();
    }

    @Override
    protected void initView() {
        if (this.getChildCount()>0) {
            this.removeAllViews();
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
        frameLayout.setPadding(marginleft,margintop,marginright,marginbottom);

        mViewPager = new ViewPager(getContext());
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setId(R.id.autoswitchview);
        RelativeLayout.LayoutParams viewpagelayoutparams = generalLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        viewpagelayoutparams.setMargins(marginleft,margintop,marginright,marginbottom);
//        viewpagelayoutparams.setMargins(marginleft,margintop,marginright,Helper.dip2px(mContext,12));
//        addView(mViewPager, viewpagelayoutparams);
        controlViewPagerSpeed();
        mPageShowView = new PageShowView(getContext());
        RelativeLayout.LayoutParams params;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        params = generalLayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, displayMetrics));


        switch (mType){
            case 0://首页轮播
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                addView(mViewPager, viewpagelayoutparams);
                mPageShowView.initColor(Color.WHITE,getResources().getColor(R.color.banner_selected_color_nod));
                if(displayMetrics.density==2.0)
                    mPageShowView.setWidthHeightMargin(displayMetrics,16,16,8);
                else if(displayMetrics.density>2.0)
                    mPageShowView.setWidthHeightMargin(displayMetrics,20,20,10);
                else if(displayMetrics.density<2.0)
                    mPageShowView.setWidthHeightMargin(displayMetrics,12,12,5);
                addView(mPageShowView, params);
                break;

        }
        mHandler = new LoopHandler(this);
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    protected void onSwitch(int index, Object o) {
        if(pageChangeListener!=null)
            pageChangeListener.onSwitch(index,tvTime);
    }


    @Override
    protected View getFailtView() {
        return null;
    }

    @Override
    protected long getDurtion() {
        return 3000;
    }

    @Override
    public void setAdapter(AutoLoopSwitchBaseAdapter adapter) {
        super.setAdapter(adapter);
        mHandler.sendEmptyMessage(LoopHandler.MSG_REGAIN);
    }
    public interface  PageChangeListener {
        void onSwitch(int currentIndex, TextView remarks);
    }
    PageChangeListener pageChangeListener;


    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }
}
