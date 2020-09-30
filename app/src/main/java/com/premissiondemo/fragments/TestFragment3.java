package com.premissiondemo.fragments;

import android.view.View;
import android.widget.TextView;

import com.basecommon.BaseFragment;
import com.premissiondemo.R;

public class TestFragment3 extends BaseFragment {

    @Override
    public void initView(View view) {
        TextView content  = (TextView) view.findViewById(R.id.tv_content);
        content.setText("TestFragment3");
        doDataResult(SERVICE_ERROR_CODE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }
}
