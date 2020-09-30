package com.basecommon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lib.weight.dialog.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment implements DialogInterface.OnCancelListener{

    protected final String NET_NO_HAPPY_CODE = "1001";
    protected final String SERVICE_ERROR_CODE = "1002";
    protected final String RESULT_EMPTY_CODE = "1003";

    static {
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
    }

    protected Dialog mProgressDialog;
    protected Disposable mDisposable;

    //数据加载时异常相关
    protected RelativeLayout net_nohappy;
    private TextView net_nohappy_retry;

    protected RelativeLayout service_error;
    private TextView service_error_retry;

    protected RelativeLayout data_empty;

    protected SmartRefreshLayout base_smartrefreshlayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        LinearLayout viewgroup = (LinearLayout) view.findViewById(R.id.ll_fragemnt_base);


        //数据加载时异常相关
        net_nohappy = (RelativeLayout) view.findViewById(R.id.net_nohappy);
        net_nohappy_retry = (TextView) view.findViewById(R.id.net_nohappy_retry);
        net_nohappy_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                base_smartrefreshlayout.autoRefresh();
            }
        });

        service_error = (RelativeLayout) view.findViewById(R.id.service_error);
        service_error_retry = (TextView) view.findViewById(R.id.service_error_retry);
        service_error_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                base_smartrefreshlayout.autoRefresh();
            }
        });

        data_empty = (RelativeLayout) view.findViewById(R.id.data_empty);

        base_smartrefreshlayout = (SmartRefreshLayout) view.findViewById(R.id.base_smartrefreshlayout);
        base_smartrefreshlayout.setEnableLoadMore(false);
        base_smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doBaseRefresh();
            }
        });



        View view1 = LayoutInflater.from(getContext()).inflate(getLayoutId(), viewgroup,false);
        viewgroup.addView(view1);
        initView(view1);
        return view;
    }

    protected void doBaseRefresh() {

    }

    /**
     * 处理网络请求数据结果显示
     */
    protected void doDataResult(String resultcode) {
        switch (resultcode) {
            case NET_NO_HAPPY_CODE:
                base_smartrefreshlayout.setVisibility(View.VISIBLE);
                net_nohappy.setVisibility(View.VISIBLE);
                break;
            case SERVICE_ERROR_CODE:
                base_smartrefreshlayout.setVisibility(View.VISIBLE);
                service_error.setVisibility(View.VISIBLE);
                break;
            case RESULT_EMPTY_CODE:
                base_smartrefreshlayout.setVisibility(View.VISIBLE);
                data_empty.setVisibility(View.VISIBLE);
                break;
            default:
                base_smartrefreshlayout.setVisibility(View.GONE);
                net_nohappy.setVisibility(View.GONE);
                service_error.setVisibility(View.GONE);
                data_empty.setVisibility(View.GONE);
                break;
        }
    }

    protected void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(getActivity(), "正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.show();
    }

    protected void closeLoadDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public abstract void initView(View view);

    public abstract int getLayoutId();

}
