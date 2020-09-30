package com.lib.weight.svgmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created on 2017/6/1 15:26
 *
 * @author
 */

public class SvgMapView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener{

    private final String TAG = SvgMapView.class.getName();
    private List<Area> areas;
    private boolean enlarge = false;//是否放大
    private MapCallback mMapCallback;

    private int width; //  测量宽度 View的宽度
    private int height; // 测量高度 View的高度
    private int maxWidth; // 最大宽度 window 的宽度
    private int maxHeight; // 最大高度 window 的高度

    private float lastX;
    private float lastY;

    //手势监听器
    private GestureDetector mDetector;
    private float scale = 1.8f;//放大缩小默认倍数

    public SvgMapView(Context context) {
        this(context, null);
    }

    public SvgMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDetector = new GestureDetector(context, this);
        mDetector.setOnDoubleTapListener(this);
    }

    public void setMapCallback(MapCallback mapCallback){
        mMapCallback = mapCallback;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取屏宽高 和 可是适用范围 （我的需求是可在屏幕内拖动 不超出范围 也不需要隐藏）
        width  = getMeasuredWidth();
        height = getMeasuredHeight();

        maxWidth = getMaxWidth(getContext());
        maxHeight = getMaxHeight(getContext()) - getStatusBarHeight();// 此时减去状态栏高度 注意如果有状态栏 要减去状态栏 如下行 得到的是可活动的高度


    }

    public static int getMaxWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.widthPixels;
    }
    // 获取最大高度
    public static int getMaxHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );
        return dm.heightPixels;
    }

    // 获取状态栏高度
    public int getStatusBarHeight(){
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != areas && areas.size() > 0) {
            canvas.scale(scale, scale);

            for (Area area : areas) {
                area.draw(canvas);
            }


        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return mDetector.onTouchEvent(event); //把手势相关操作返回给 手势操控类;
    }

    public List<Area> getCities() {
        return areas;

    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        Log.e(TAG, "-----onSingleTapConfirmed-----");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (Area area : areas) {
                    Region region = area.getRegion();
                    boolean isContain = region.contains((int) (x / scale), (int) (y / scale));
                    if (isContain) {
                        area.setTouch(true);
                        if (mMapCallback != null) {
                            mMapCallback.mapCallback(area);
                        }
                    } else {
                        area.setTouch(false);
                    }
                }
                invalidate();
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e(TAG, "-----onDoubleTap-----");
        if (enlarge) {
            scale = scale * 1.1f;
            invalidate();
        }

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.e(TAG, "-----onDoubleTapEvent-----");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.e(TAG, "-----onShowPress-----");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.e(TAG, "-----onLongPress-----");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e(TAG, "-----onScroll-----");
//
//        float moveX = e1.getX() - e2.getX();
//        float moveY = e1.getY() - e2.getY();
//        scrollTo((int)moveX, (int)moveY);

        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e(TAG, "-----onFling-----");
        return true;
    }


    public interface MapCallback {
        void mapCallback(Area area);
    }
}
