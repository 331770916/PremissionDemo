package com.lib.weight.free_recyclerview;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lib.weight.R;


/**
 * Created by zwb on 18/1/3.
 * 提示框 模仿Toast
 */

public class StockListToast {

    private Activity mActivity;
    private PopupWindow mPopupWindow;

    public StockListToast(Activity activity) {
        mActivity = activity;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_stocklist_toast, null);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//		mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void show(View viewGroup) {
        if (null ==mActivity || mActivity.isFinishing()) {
            return;
        }
        mPopupWindow.showAtLocation(viewGroup, Gravity.BOTTOM, 0, 200);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (null != mActivity && null != mPopupWindow){
                        mPopupWindow.setFocusable(false);
                        mPopupWindow.dismiss();
                }

            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 5000);
    }
}
