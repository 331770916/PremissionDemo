package com.lib.weight.free_recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.lib.weight.R;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by fanlinlin on 17/12/29.
 */

public class FreeRecyclerView extends RecyclerView {

    private int firstVisibleItemPosition = 0;   //第一个可见的item的position
    private int lastVisibleItemPosition = 0;    //最后一个可见的item的position

    private int offset = 0;                     //横向滑动的偏移量

    private HeaderWrapper adapter;

    private View mCurrentHeader;

    public FreeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addScrollListener();
    }

    public FreeRecyclerView(Context context) {
        super(context);

        addScrollListener();
    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    private void addScrollListener(){
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollBy(offset);

                mCurrentHeader = getHeaderView(firstVisibleItemPosition, mCurrentHeader);
                if (mCurrentHeader != null && offset == 0) {
                    findTempView(mCurrentHeader).setBackgroundDrawable(getResources().getDrawable(R.drawable.stocklist_titlebg));
                    findTempView(mCurrentHeader).setVisibility(View.GONE);

                }

            }
        });
    }


    private View getHeaderView(int position, View v){

        View view = adapter.getHeaderView(position);
        if (view != null){
            v = view;
        }
        v.setFocusable(false);
        return v;
    }

    /**
     * 获得屏幕内的第一个position 和最后一个 position
     */
    private void findEyeablePosition(){

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
            lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
        }
    }

    /**
     * 当某个item滚动的时候，屏幕内的其他item跟着滚动
     * @param moveX
     */
    public void scrollTo(int moveX){
        this.offset = moveX;//获取scrollview的偏移量   从AnimateScrollView 的onScrollChanged()中传入

        //获得屏幕内的第一个position 和最后一个 position
        findEyeablePosition();

        //遍历所有item
        for(int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {

            AnimateScrollView scrollView = getAnimateView(i);
            View view = getChildTempView(i);
            if (scrollView != null) {
                startAnmateView(scrollView, moveX);

                if (view != null) {
                    if (moveX == 0) {
                        view.setVisibility(View.GONE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }

        }

        if (mCurrentHeader != null) {
            startAnmateView(findAnimateScrollView(mCurrentHeader),offset);//mCurrentHeader scrollView

            findTempView(mCurrentHeader).setVisibility(View.VISIBLE);
            if (offset == 0) {
                findTempView(mCurrentHeader).setVisibility(View.GONE);
            }

        }

        invalidate();
    }

    private void startAnmateView(AnimateScrollView scrollView,int moveX){
        scrollView.smoothScrollTo(moveX,0);
    }



    /**
     * 滚动的时候刚进入屏幕内的item 增加一个偏移量 offset
     * @param offset
     */
    private void scrollBy(int offset){
        findEyeablePosition();
        for(int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {

            AnimateScrollView scrollView = getAnimateView(i);
            View view = getChildTempView(i);

            if (view != null && offset == 0) {
                view.setVisibility(View.GONE);
            }

            if (scrollView != null) {
                startScrollBy(scrollView,offset);
            }

        }
    }

    private void startScrollBy(AnimateScrollView scrollView,int moveX){
        scrollView.scrollTo(moveX, 0);
    }


    private AnimateScrollView getAnimateView(int index){
        try {
            View childView = findViewHolderForAdapterPosition(index).itemView;

            if (childView != null) {
                AnimateScrollView horizontalScrollView = findAnimateScrollView(childView);
                return horizontalScrollView;
            } else {
                return  null;
            }
        } catch (Exception e) {
            return  null;
        }
    }

    private View getChildTempView(int index) {
        try {
            View childView = findViewHolderForAdapterPosition(index).itemView;

            if (childView != null) {
                View view = findTempView(childView);
                return view;
            } else {
                return  null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    private AnimateScrollView findAnimateScrollView(View v){
        return (AnimateScrollView) v.findViewById(R.id.scrollView);
    }

    private View findTempView(View v) {
        return v.findViewById(R.id.stocklistlineview);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = (HeaderWrapper) adapter;
        super.setAdapter(adapter);
    }



    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mCurrentHeader == null)
            return;
        int saveCount = canvas.save();
        mCurrentHeader.draw(canvas);
        canvas.restoreToCount(saveCount);
    }
}
