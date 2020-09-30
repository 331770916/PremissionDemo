package com.premissiondemo.fragments;

import android.view.View;
import android.widget.TextView;

import com.basecommon.BaseFragment;
import com.premissiondemo.R;

public class TestFragment5 extends BaseFragment {

    @Override
    public void initView(View view) {

        TextView content  = (TextView) view.findViewById(R.id.tv_content);
        content.setText("TestFragment5");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }
}
