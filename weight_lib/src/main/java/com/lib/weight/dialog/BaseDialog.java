package com.lib.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lib.weight.R;


public abstract class BaseDialog extends Dialog {

    protected Context context;

    public BaseDialog(Context context) {
        super(context, R.style.Theme_Dialog_From_Bottom);
        this.context = context;
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setWindowAnimations(R.style.AnimationPreview);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        setContentView(getLayoutId());
        setView();
        initData();
    }

    public abstract void setView();

    public abstract int getLayoutId();

    public abstract void initData();

}
