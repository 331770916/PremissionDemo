package com.lib.weight.log_collector;

import android.content.Context;
import android.util.Log;


import java.util.Map;


/**
 * Created by zhangwenbo on 2016/5/17.
 * Log日志收集类
 */
public class LogCollector {

    private static final String TAG = LogCollector.class.getName();

    private static Context mContext;

    private static boolean isInit = false;

    private static Map<String, String> mParams;
    private static String URL ;


    public static void initLog(Context context) {
        initLog(context, null, null);
    }

    public static void initLog(Context context, String upload_url, Map<String, String> params) {
        if (context == null) {
            return;
        }

        if (isInit) {
            return;
        }

        mContext = context;
        mParams  = params;
        URL      = upload_url;

        CrashHandler crashHandler = CrashHandler.getInstance(context);
        crashHandler.init();

        isInit = true;
    }

    /**
     * 上传log文件
     * @param isWifiOnly
     */
    public static void upload(boolean isWifiOnly){
        if(mContext == null || URL == null){
            Log.d(TAG, "please check if init() or not");
            return;
        }
        if(!LogHelper.checkNetworkConnected(mContext)){
            return;
        }

        boolean isWifiMode = LogHelper.isWifi(mContext);

        if(isWifiOnly && !isWifiMode){
            return;
        }

//        NetWorkUtil.getInstence().okHttpForPostFileForm(URL, );

    }

    public static void setDebugMode(boolean isDebug){
        LogHelper.LOG_DEBUG = isDebug;
        LogHelper.enableDefaultLog = isDebug;
    }

    public static void setEncodeMode(boolean isBase64) {
        LogHelper.LOG_IS_BASE64 = isBase64;
    }
}
