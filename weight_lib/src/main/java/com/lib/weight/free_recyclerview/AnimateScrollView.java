package com.lib.weight.free_recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.widget.HorizontalScrollView;

/**
 * myt.
 */
public class AnimateScrollView extends HorizontalScrollView {

    private FreeRecyclerView myRecyclerView;

    public AnimateScrollView(Context context) {
        super(context);

        //Overscroll（边界回弹）效果-- android2.3新增的功能，也就是当滑动到边界的时候，如果再滑动，就会有一个边界就会有一个发光效果。
        //OVER_SCROLL_NEVER  就是去掉这种效果
        setOverScrollMode(OVER_SCROLL_NEVER);

    }

    public AnimateScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        FreeRecyclerView freeRecyclerView = getMyRecyclerView();
        if (freeRecyclerView != null) {
            freeRecyclerView.scrollTo(l);
        }
    }

    private FreeRecyclerView getMyRecyclerView() {
        if (myRecyclerView == null){
            myRecyclerView = (FreeRecyclerView)getTag();
        }
        return myRecyclerView;
    }

}
