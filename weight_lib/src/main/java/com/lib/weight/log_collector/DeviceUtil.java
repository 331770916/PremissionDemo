package com.lib.weight.log_collector;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.lib.weight.toast.CentreToast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;


/**
 * 设备相关信息工具类
 */
public class DeviceUtil {

    private static final String TAG = "DeviceUtil";

    public static String getDeviceId(Context context) {
        StringBuffer device = new StringBuffer();
        device.append(Build.MODEL+"_");
        device.append(getPsuedoId(context));
        String result = device.toString();
        result =  result.replace(" ","");
        return result;
    }

    //获得独一无二的Psuedo ID
    public static String getPsuedoId(Context context) {
        String serial = null;
        String device = "";
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10 + getIMIE(context);
        ; //12 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            device = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            return device;
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        device = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        return device;
    }

    // IMEI码
    public static String getIMIE(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String iMEI = "";
        try {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                iMEI = tm.getDeviceId();
            }else {
                Method method = tm.getClass().getMethod("getImei");
                iMEI = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return iMEI;
    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    // Android Id
    private static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 判断是否添加了权限
     * @param context
     * @return
     */
    public static boolean hasPermission(Context context) {
        if (context != null) {
            boolean b1 = context .checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED;//
            boolean b2 = context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED;
            boolean b3 = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
            boolean b4 = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED;
            boolean b5 = context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED;

            if(!b1 || !b2 || !b3 || !b4 || !b5){
                CentreToast.showText(context, "没有添加权限");
            }
            return b1 && b2 && b3 && b4 && b5;
        }

        return false;
    }

    public static String getMid(Context context) {
//        String imei = getIMIE(context);  //imei号

        String AndroidID = getAndroidId(context);

        String serialNo = getDeviceSerialForMid2();
        String m2 = getMD5Str(""  + AndroidID + serialNo);
        return m2;
    }

    private static String getDeviceSerialForMid2() {
        String serial = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
        }
        return serial;
    }

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }


        return md5StrBuff.toString();
    }

    /**
     * 获取时间
     * @return
     */
    public static String getCurrentTime(){
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

    /**
     * 获取程序外部(sd)的目录
     *
     * @param context
     * @return
     */
    public static File getExternalDir(Context context , String dirName) {
        final String cacheDir = "/AppLog/" + context.getPackageName()
                + "/";
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + cacheDir + dirName + "/");
    }

    /**
     * 判断sdcard是否存在
     * @return
     */
    public static boolean isSDcardExsit() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    /**
     * 调用相机拍照
     */
    public static String takePic(Activity activity, int requestCode) {

        String filePath = "";

        try {
            if (isSDcardExsit()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(DeviceUtil.getExternalDir(activity, "openvc").getPath());

                if (!file.exists()) {
                    file.mkdirs();
                }

                File photoFile = new File(file, "photo.jpg");

                if (photoFile != null) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        Uri fileUri = Uri.fromFile(photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    } else {
                        Uri fileUri = FileProvider.getUriForFile(activity, "com.hebeiydjz.appocr.provider", photoFile);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    }

                    filePath = photoFile.getPath();
                }

                activity.startActivityForResult(intent, requestCode);
            } else {
                CentreToast.showText(activity, "请检查sd卡是否存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;

    }



    /**
     * 根据Uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 19) {
            return getRealPathFromUriAboveApi19(context, uri);
        } else {
            return getRealPathFromUriBelowAPI19(context, uri);
        }
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 使用':'分割
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};
                filePath = getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
