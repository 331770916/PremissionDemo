package com.lib.weight.log_collector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 日志工具类
 */
public class LogHelper {

    public static boolean enableDefaultLog = false;

    private static final int RETURN_NOLOG = -1;

    //错误日志相关
    public static boolean LOG_DEBUG = true;
    public static boolean LOG_IS_BASE64 = false;

    public static int i(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.i(tag, msg) : RETURN_NOLOG;
    }

    public static int d(String tag, String msg) {

        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.d(tag, msg) : RETURN_NOLOG;
    }

    public static int e(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.e("--TPY"+tag, msg) : RETURN_NOLOG;
    }

    public static int w(String tag, String msg) {
        if (msg == null)
            msg = "";

        return enableDefaultLog ? Log.w(tag, msg) : RETURN_NOLOG;
    }


    /**
     * 网络是否连接
     * @param context
     * @return
     */
    public static boolean checkNetworkConnected(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断当前网络是否是wifi网络
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
