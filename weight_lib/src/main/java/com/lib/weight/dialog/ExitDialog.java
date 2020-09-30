package com.lib.weight.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lib.weight.R;


/**
 * 退出程序Dialog
 */
public class ExitDialog extends BaseDialog implements View.OnClickListener {

    private Button mConfirmbtn, mCancelbtn;

    public ExitDialog(Context context) {
        super(context);
    }

    @Override
    public void setView() {
        TextView mPromptTv = (TextView) findViewById(R.id.DLPromptIV);
        TextView mPromptDetailsTV = (TextView) findViewById(R.id.DLPromptDetailsTV);
        mConfirmbtn = (Button) findViewById(R.id.Confirmbtn);
        mCancelbtn = (Button) findViewById(R.id.Cancelbtn);
        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_tuichu);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPromptTv.setCompoundDrawables(drawable, null, null, null);
        mPromptTv.setText("退出程序");
        mPromptDetailsTV.setText("您确认退出APP吗?");

    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_exit;
    }

    @Override
    public void initData() {
        mConfirmbtn.setOnClickListener(this);
        mCancelbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.Cancelbtn) {
            dismiss();
        } else if (v.getId() == R.id.Confirmbtn) {
            this.dismiss();
            ((Activity) context).finish();
        }

    }

}
