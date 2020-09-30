package com.basecommon;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basecommon.util.BaseCommonHelper;
import com.basecommon.util.CommonTitle;
import com.basecommon.util.StatusBarHelper;
import com.lib.weight.dialog.LoadingDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity implements DialogInterface.OnCancelListener {

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
    private ViewGroup viewGroup;
    protected View mContentView;
    protected CommonTitle mComment_title;
    protected RelativeLayout mTopBar;
    //网络状态相关
    private ConnectionNetWordReceiver mConnectionReceiver;
    private RelativeLayout mViewNetError;

    //数据加载时异常相关
    protected RelativeLayout net_nohappy;
    private TextView net_nohappy_retry;

    protected RelativeLayout service_error;
    private TextView service_error_retry;

    protected RelativeLayout data_empty;

    protected SmartRefreshLayout base_smartrefreshlayout;


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        setTheme(R.style.CustomAppTheme);
        super.setContentView(R.layout.activity_base);

        //屏幕竖直设置
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置键盘不挤压布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);     //设置键盘不挤压布局

        //actionbar 设置
        viewGroup = (ViewGroup) findViewById(R.id.ll_base_root);
        mContentView = getLayoutInflater().inflate(layoutResID, viewGroup);
        //网络状态
        mViewNetError = (RelativeLayout) findViewById(R.id.rl_network_error);
        mComment_title = (CommonTitle) findViewById(R.id.comment_title);
        mTopBar = (RelativeLayout) findViewById(R.id.rl_top_bar_mark);
        mTopBar.setVisibility(View.GONE);
        mComment_title.setVisibility(View.GONE);
        mViewNetError.setVisibility(View.GONE);
        mComment_title.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftIconClick();
            }
        });

        //数据加载时异常相关
        net_nohappy = (RelativeLayout) findViewById(R.id.net_nohappy);
        net_nohappy_retry = (TextView) findViewById(R.id.net_nohappy_retry);
        net_nohappy_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                base_smartrefreshlayout.autoRefresh();
            }
        });

        service_error = (RelativeLayout)findViewById(R.id.service_error);
        service_error_retry = (TextView) findViewById(R.id.service_error_retry);
        service_error_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                base_smartrefreshlayout.autoRefresh();
            }
        });

        data_empty = (RelativeLayout)findViewById(R.id.data_empty);

        base_smartrefreshlayout = (SmartRefreshLayout) findViewById(R.id.base_smartrefreshlayout);
        base_smartrefreshlayout.setEnableLoadMore(false);
        base_smartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doBaseRefresh();
            }
        });


        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //适配状态栏
        StatusBarHelper.setStatusBarLightMode(this);

        //监听网络状态
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectionReceiver = new ConnectionNetWordReceiver();
        registerReceiver(mConnectionReceiver, intentFilter);

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

    protected void doBaseRefresh() {

    }

    protected void onLeftIconClick() {
        finish();
    }



    /**
     * 当网络不可用时，展示网络不可用页面
     */
    private void showNetErrorView() {
        mTopBar.setVisibility(View.VISIBLE);
        mViewNetError.setVisibility(View.VISIBLE);
        mViewNetError.findViewById(R.id.ll_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
    }

    /**
     * 网络状态可用性广播
     */
    class ConnectionNetWordReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!BaseCommonHelper.isConnected(context)){
                showNetErrorView();
            } else {
                mViewNetError.setVisibility(View.GONE);
            }
        }
    }

    protected void initLoadDialog() {
        mProgressDialog = LoadingDialog.initDialog(this, "正在加载...");
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
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        if (mConnectionReceiver != null) {
            unregisterReceiver(mConnectionReceiver);
        }
    }

    public abstract void initView();

    public abstract int getLayoutId();

}
