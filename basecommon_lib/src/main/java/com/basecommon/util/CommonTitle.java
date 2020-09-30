package com.basecommon.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basecommon.R;

public class CommonTitle extends RelativeLayout {

    private Context mContext;
    private RelativeLayout mCommon_title;
    private ImageView mTitle_leftIcon;
    private TextView mTitle_centerText;
    private TextView mTitle_rightText;
    private ImageView mTitle_rightIcon;
    private ImageView mTitle_rightIcon_most;
    private int px10, px15;

    public CommonTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        px10 = BaseCommonHelper.dip2px(mContext, 10);
        px15 = BaseCommonHelper.dip2px(mContext, 15);
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.common_title, this, true);
        mCommon_title = (RelativeLayout) findViewById(R.id.Common_title);//根布局
        mTitle_leftIcon = (ImageView) findViewById(R.id.title_leftIcon);//左边图片
        mTitle_centerText = (TextView) findViewById(R.id.title_centerText);//中间文字
        mTitle_rightText = (TextView) findViewById(R.id.title_rightText);//右边文字
        mTitle_rightIcon = (ImageView) findViewById(R.id.title_rightIcon);//右边图片
        mTitle_rightIcon_most = (ImageView) findViewById(R.id.title_rightIcon_most);//最右边图片

    }

    public CommonTitle(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CommonTitle(Context context) {
        this(context, null);
    }

    public TextView getmTitle_rightText() {
        return mTitle_rightText;
    }

    public TextView getmTitle_centerText() {
        return mTitle_centerText;
    }


    /**
     *
     * @param centerTextStyle	中间文字样式
     * @param rightTextStyle	右边文字样式
     * @param rightTextListener	右边文字点击事件
     * @param rightIconResource	右边图片
     * @param rightIconListener	右边图片点击事件
     * @param rightMostIconResource	最右边图片
     * @param rightMostIconListener	最右边图片点击事件
     */
    public void initTitle(int centerTextStyle, int rightTextStyle, OnClickListener rightTextListener, int rightIconResource,
                          OnClickListener rightIconListener,  int rightMostIconResource, OnClickListener rightMostIconListener){
        if(0 != centerTextStyle){
            mTitle_centerText.setVisibility(View.VISIBLE);
            mTitle_centerText.setTextAppearance(mContext,centerTextStyle);
        }
        if(0 != rightTextStyle){
            mTitle_rightText.setVisibility(View.VISIBLE);
            mTitle_rightText.setTextAppearance(mContext,rightTextStyle);
            mTitle_rightText.setOnClickListener(rightTextListener);
        }
        if(0 != rightIconResource){
            mTitle_rightIcon.setVisibility(View.VISIBLE);
            mTitle_rightIcon.setImageResource(rightIconResource);
            mTitle_rightIcon.setOnClickListener(rightIconListener);
        }
        if(0 != rightMostIconResource){
            mTitle_rightIcon_most.setVisibility(View.VISIBLE);
            mTitle_rightIcon_most.setImageResource(rightMostIconResource);
            mTitle_rightIcon_most.setOnClickListener(rightMostIconListener);
        }
    }

    /**
     * 标题，右边两张图
     * @param title
     * @param rightIconResource
     * @param rightIconListener
     * @param rightMostIconResource
     * @param rightMostIconListener
     */
    public void initTitle(String title, int rightIconResource, OnClickListener rightIconListener,
                          int rightMostIconResource, OnClickListener rightMostIconListener){
        initTitle(R.style.common_title_center_text,0,null,rightIconResource,rightIconListener,
                rightMostIconResource,rightMostIconListener);
        setTitle(title);
        setPadding(px10,px10,px10,px10,1);
        setPadding(px10,px10,px10,px10,2);
    }

    /**
     * 标题，右边文字
     * @param title
     * @param rightText
     * @param rightTextListener
     */
    public void initTitle(String title, String rightText, OnClickListener rightTextListener){
        initTitle(R.style.common_title_center_text,R.style.common_title_right_text,rightTextListener,0,null,
                0,null);
        setTitle(title);
        setRightText(rightText);
        setPadding(px10,px10,px10,px10,3);
    }

    /**
     * 标题，右边图片
     * @param title
     * @param rightIconResource
     * @param rightIconListener
     */
    public void initTitle(String title, int rightIconResource, OnClickListener rightIconListener){
        initTitle(R.style.common_title_center_text,0,null,
                rightIconResource,rightIconListener,0,null);
        setTitle(title);
        setPadding(px10,px10,px15,px10,1);
    }

    public void setTitle(String title){
        if(!TextUtils.isEmpty(title)){
            mTitle_centerText.setText(title);
            mTitle_centerText.setVisibility(View.VISIBLE);
        }
    }

    /**
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param location	0：左边图片 1：右边图片 2：最右边图片 3：右边文字
     */
    public void setPadding(int left, int top, int right, int bottom, int location){
        switch (location){
            case 0:
                mTitle_leftIcon.setPadding(left, top, right, bottom);
                break;
            case 1:
                mTitle_rightIcon.setPadding(left, top, right, bottom);
                break;
            case 2:
                mTitle_rightIcon_most.setPadding(left, top, right, bottom);
                break;
            case 3:
                mTitle_rightText.setPadding(left, top, right, bottom);
                break;
        }

    }

    /**
     *
     * @param resource 图片
     * @param location 0：左边图片 1：右边图片 2：最右边图片
     */
    public void setIconResource(int resource, int location){
        switch (location){
            case 0:
                mTitle_leftIcon.setImageResource(resource);
                break;
            case 1:
                mTitle_rightIcon.setImageResource(resource);
                break;
            case 2:
                mTitle_rightIcon_most.setImageResource(resource);
                break;
        }

    }

    /**
     *
     * @param textStyle 文字样式
     * @param location 0：标题 1：右边文字
     */
    public void setTextAppearance(int textStyle, int location){
        switch (location){
            case 0:
                mTitle_centerText.setTextAppearance(mContext,textStyle);
                break;
            case 1:
                mTitle_rightText.setTextAppearance(mContext,textStyle);
                break;

        }
    }

    /**
     * 设置背景色
     * @param backgroundResource
     */
    public void setBackgroundResource(int backgroundResource){
        mCommon_title.setBackgroundResource(backgroundResource);
    }

    /**
     * 图片隐藏
     * @param location 0：左边，1：右边，2：最右边，3：右边文字
     */
    public void setViewVisibility(int visibility, int location){
        switch (location){
            case 0:
                mTitle_leftIcon.setVisibility(visibility);
                break;
            case 1:
                mTitle_rightIcon.setVisibility(visibility);
                break;
            case 2:
                mTitle_rightIcon_most.setVisibility(visibility);
                break;
            case 3:
                mTitle_rightText.setVisibility(visibility);
                break;

        }

    }

    /**
     * 返回ImageView
     * @param location 0：左边，1：右边，2：最右边
     * @return
     */
    public ImageView getImageView(int location){
        if(0 == location){
            return mTitle_leftIcon;
        }else if(1 == location){
            return mTitle_rightIcon;
        }else {
            return mTitle_rightIcon_most;
        }
    }

    public void setOnBackListener(OnClickListener listener){
        mTitle_leftIcon.setOnClickListener(listener);
    }

    public void setRightText(String rightText){
        if(!TextUtils.isEmpty(rightText))
            mTitle_rightText.setText(rightText);
    }
}
