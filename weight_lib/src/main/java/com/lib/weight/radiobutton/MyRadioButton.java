package com.lib.weight.radiobutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.lib.weight.R;

import androidx.annotation.DrawableRes;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;
import skin.support.widget.SkinCompatTextHelper;

/**
 * Created by zhangwenbo on 2016/5/12.
 * 自定义RadioButton
 */
public class MyRadioButton extends androidx.appcompat.widget.AppCompatRadioButton implements SkinCompatSupportable {

    private int mDrawableSize;// xml文件中设置的大小
    private SkinCompatTextHelper mTextHelper;
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public MyRadioButton(Context context) {
        this(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) { return; }
        Drawable drawableLeft   = null;
        Drawable drawableTop    = null;
        Drawable drawableRight  = null;
        Drawable drawableBottom = null;

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            Log.i("MyRadioButton", "attr:" + attr);
            if (attr == R.styleable.MyRadioButton_pictureSize) {
                mDrawableSize = typedArray.getDimensionPixelSize(R.styleable.MyRadioButton_pictureSize, 50);
                Log.i("MyRadioButton", "mDrawableSize:" + mDrawableSize);
            } else if (attr == R.styleable.MyRadioButton_pictureTop) {
                drawableTop = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_pictureBottom) {
                drawableRight = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_pictureRight) {
                drawableBottom = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_pictureLeft) {
                drawableLeft = typedArray.getDrawable(attr);
            }
        }

        typedArray.recycle();

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
        mTextHelper = SkinCompatTextHelper.create(this);
        mTextHelper.loadFromAttributes(attrs, defStyleAttr);
    }


    public void setMyRadioButtonpictureTop(Drawable drawableTop){
        setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);

    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableSize, mDrawableSize);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(resId);
        }
    }

    @Override
    public void setTextAppearance(int resId) {
        setTextAppearance(getContext(), resId);
    }

    @Override
    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (mTextHelper != null) {
            mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
        if (mTextHelper != null) {
            mTextHelper.applySkin();
        }
    }

}
