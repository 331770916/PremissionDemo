package com.basecommon.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseCommonHelper {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue px值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 修改密码 验证新老密码
     * @param oldPwd
     * @param newPwd
     * @param newPwdAgain
     * @return
     */
    public static String isEffectiveFormat(String oldPwd,String newPwd,String newPwdAgain) {
        String msg = "";
        if (TextUtils.isEmpty(oldPwd)||TextUtils.isEmpty(newPwd)||TextUtils.isEmpty(newPwdAgain)) {
            msg = "密码不能为空";
        } else if (newPwd.length()!=6||newPwdAgain.length()!=6){
            msg = "请输入合法新密码";
        } else if (!newPwd.equalsIgnoreCase(newPwdAgain)) {
            msg = "请确认新密码输入一致";
        }
        return msg;
    }

    /**
     * 从Assets文件夹下读取文件
     *
     * @param context  上下文
     * @param fileName 文件名称
     * @return
     */
    public String getStringFromAssets(Context context, String fileName) {
        final String ENCODING = "UTF-8";
        String strData = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            strData = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strData;
    }

    /**
     * 是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            for (int i = 0; i < str.length(); i++) {
//                System.out.println(str.charAt(i));
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    /**
     * 转换成亿或者万数据（字符串可为double类型long类型）
     * @param s
     * @return
     */
    public static String long2Strings(String s) {
        DecimalFormat df = new DecimalFormat("######0.00");
        if (!s.contains("E")) {
            if (!s.equals("")) {
                if (s.contains(" ")) {
                    return "0.00";
                } else {
                    double d = Double.parseDouble(s);
                    if (Math.abs(d) > 100000000) {
                        if (d%100000000 == 0){
                            df = new DecimalFormat("#");
                        }
                        return df.format(d / 100000000) + "亿";
                    } else if (Math.abs(d) > 10000) {
                        if (d%10000 == 0){
                            df = new DecimalFormat("#");
                        }
                        return df.format(d / 10000) + "万";
                    } else {
                        return df.format(d);
                    }
                }
            } else {
                return "0.00";
            }
        } else {
            BigDecimal bd = new BigDecimal(s);
            String ss = bd.toPlainString();
            double d = Double.parseDouble(ss);
            if (Math.abs(d) > 100000000) {
                return df.format(d / 100000000) + "亿";
            } else if (Math.abs(d) > 10000) {
                return df.format(d / 10000) + "万";
            } else {
                return df.format(d);
            }
        }
    }

    /**
     * 判读是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(mobiles);
//        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 验证邮政编码
     *
     * @param post
     * @return
     */
    public static boolean checkPost(String post) {
        return post.matches("[0-9]\\d{5}(?!\\d)");
    }


    /**
     * base64字符串转Bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * 格式化HHmmss类型的时间
     */
    public static String formateDate(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String strDate = "";
        try {
            while(time.length() < 6){
                time = "0"+time;
            }
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            strDate = "--";
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formatTime(String time){
        if (TextUtils.isEmpty(time)||time.length()<5||time.length()>9) {
            return "--";
        }
        if (time.length()<=6) {
            return formateDate(time);
        } else if (time.length()<8) {
            return "--";
        } else {
            return formateHHMMSSSSSDate(time);
        }
    }
//
    /**
     * 格式化HHmmssSSS类型时间为hhMMssSS
     */
    public static String formateHHMMSSSSSDate(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HHmmssSSS");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = "";
        try {
            while(time.length() < 9){
                time = "0"+time;
            }
            Date date = mSimpleDateFormat.parse(time);
            strDate = sdf.format(date);
        } catch (Exception e) {
            strDate = "--";
            e.printStackTrace();
        }

        return strDate;
    }

    /**
     * 时间格式yyyymmdd转 yyyy-mm-dd
     *
     * @param time
     * @return
     */
    public static String formateDate1(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }

    /**
     * 时间格式yyyy-mm-dd转 yyyy-mm-dd
     *
     * @param time
     * @return
     */
    public static String formateDate2(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }


    /**
     * 时间格式yyyymmdd hh:mm:ss转 yyyy.mm.dd
     *
     * @param time
     * @return
     */
    public static String formateDate3(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            strDate = time;
        }
        return strDate;
    }

    /**
     * 时间格式yyyymmdd hh:mm:ss转 yyyy.mm.dd
     *
     * @param time
     * @return
     */
    public static String formateDateY_M_D_hms(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        String strDate = "";
        try {
            Date date = sdf.parse(time);
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formateDate4(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "";
        try {
            Date date = mSimpleDateFormat.parse(time);
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formateDate5(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = "";
        try {
            Date date = mSimpleDateFormat.parse(time);
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }

    public static String formateDate7(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String strDate = "";
        try {
            Date date = mSimpleDateFormat.parse(time);
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }


    public static String formateDate6(String time) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = "";
        try {
            Date date = mSimpleDateFormat.parse(time);
            strDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strDate;
    }


    /**
     * 时间戳转时间
     *
     * @param timec
     * @return
     */
    public static String getTimeByTimeC(String timec) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(Long.parseLong(String.valueOf(timec))));
        return date;
    }

    /**
     * 获取系统日期
     *
     * @return
     */
    public static String getCurDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = simpledateformat.format(calendar.getTime());
        return strDate;
    }

    public static String getCurDate1() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy.MM.dd");
        String strDate = simpledateformat.format(calendar.getTime());
        return strDate;
    }

    /**
     * 两个时间比较大小 取大
     */
    public static String compareTo(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        //获取Calendar实例
        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        try {
            //把字符串转成日期类型
            currentTime.setTime(df.parse(startDate));
            compareTime.setTime(df.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) > 0) {

            return startDate;
        } else {
            return endDate;
        }
    }


    /**
     * 两个时间比较大小
     * 起始时间等于结束时间返回0
     * 起始时间大于结束时间返回1
     * 起始时间小于结束时间返回2
     */
    public static int compareToDate(String startDate, String endDate) {
        int num = -1;
        //获取Calendar实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        Date df_startDate = null;
        Date df_endDate = null;
        try {
            //把字符串转成日期类型
            df_startDate = sdf.parse(startDate);
            df_endDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (df_startDate == null && df_endDate == null) {
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                df_startDate = sdf2.parse(startDate);
                df_endDate = sdf2.parse(endDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (df_startDate == null && df_endDate == null) {
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
                df_startDate = sdf2.parse(startDate);
                df_endDate = sdf2.parse(endDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        currentTime.setTime(df_startDate);
        compareTime.setTime(df_endDate);
        //利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) > 0) {
            num = 1;
        } else if (currentTime.compareTo(compareTime) == 0) {
            num = 0;
        } else {
            num = 2;
        }
        return num;
    }


    /**
     * 两个时间比较大小
     * 起始时间等于结束时间返回0
     * 起始时间大于结束时间返回1
     * 起始时间小于结束时间返回2
     */
    public static int compareToDateForFixFund(String startDate, String endDate) {
        int num = -1;
        //获取Calendar实例
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentTime = Calendar.getInstance();
        Calendar compareTime = Calendar.getInstance();
        Date df_startDate = null;
        Date df_endDate = null;
        try {
            //把字符串转成日期类型
            df_startDate = df.parse(startDate);
            df_endDate = df.parse(endDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            //把字符串转成日期类型
            df_startDate = df1.parse(startDate);
            df_endDate = df1.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentTime.setTime(df_startDate);
        compareTime.setTime(df_endDate);
        //利用Calendar的方法比较大小
        if (currentTime.compareTo(compareTime) > 0) {
            num = 1;
        } else if (currentTime.compareTo(compareTime) == 0) {
            num = 0;
        } else {
            num = 2;
        }
        return num;
    }


    /**
     * 获取当前日期下一年的前一天
     * @param date
     * @return
     */
    public static String getNextYear(Date date,String dateStyle) {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(date);
        ca.add(Calendar.YEAR, 1); // 年份加1
//        ca.add(Calendar.MONTH, -1);// 月份减1
        ca.add(Calendar.DATE, -1);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        return sdf.format(resultDate);
    }


    /**
     * 获取当前日期下一天
     * @param date
     * @return
     */
    public static String getNextDate(Date date,String dateStyle) {
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(date);
//        ca.add(Calendar.YEAR, 1); // 年份加1
//        ca.add(Calendar.MONTH, -1);// 月份减1
        ca.add(Calendar.DATE, 1);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
        return sdf.format(resultDate);
    }

    /**
     * 获取指定日期下一天
     * @return
     */
    public static String getNextDate(String strDate,String dateStyle) {

        String nextDate = "";
        try {
            Date date = null;
            DateFormat df = new SimpleDateFormat(dateStyle);
            date = df.parse(strDate);

            Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
            ca.setTime(date);
            ca.add(Calendar.DATE, 1);// 日期减1
            Date resultDate = ca.getTime(); // 结果
            SimpleDateFormat sdf = new SimpleDateFormat(dateStyle);
            nextDate = sdf.format(resultDate);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return nextDate;
    }

    /**
     * 获取当前日期的前一天
     * @return
     */
    public static Date getBeforeDate(){
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.setTime(new Date());
        ca.add(Calendar.DATE,-1);
        return ca.getTime();
    }

    /**
     * 获取当前日期的前一天
     * @return
     */
    public static String getBeforeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(getBeforeDate());
    }

    /**
     * 获取当前日期的前一天
     * @return
     */
    public static String getBeforeDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(getBeforeDate());
    }

    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDate(String str, String fromFormat, String formatStr) {
        return StringToDate(str, fromFormat, formatStr);
    }

    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDate(String str) {
        return StringToDate(str, "yyyy-MM-dd", "yyyyMMdd");
    }

    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param string
     * @return yymmdd
     */
    //字符串转指定格式时间
    public static String getDateFormatYYMMDD(String string){
        return StringToDate(string, "yyyy.MM.dd", "yyyyMMdd");
    }

    public static String getProTime(String str) {
        /*String defaultFromate = "yyyy-MM-dd";
        if (Helper.isDateFromatYYYY_MM_dd(str)) {
            defaultFromate = "yyyy-MM-dd";
        }

        if (Helper.isDateFromatYYYYMMdd(str)) {
            defaultFromate = "yyyyMMdd";
        }

        return StringToDate(str, defaultFromate, "MM-dd");*/
        return str;
    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        String News = dateStr;
        try {
            Date date = null;
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            News = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return News;
    }


    /**
     * 转换日期格式(yyyyMMdd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDate2(String str) {
        return StringToDate2(str, "yyyy-MM-dd", "yyyyMMdd");
    }

    public static String getProTime2(String str) {
        return StringToDate2(str, "yyyy-MM-dd", "MM月dd日");
    }

    public static String StringToDate2(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }


    /**
     * 转换日期格式(yyyy.MM.dd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateYMD(String str) {
        return StringToDateYMD(str, "yyyyMMdd", "yyyy.MM.dd");
    }

    public static String StringToDateYMD(String dateStr, String dateFormatStr, String formatStr) {
        String nDate = dateStr ;
        Date date = null;
        try {
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            nDate = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nDate ;
    }

    /**
     * 转换日期格式(yyyy-MM-dd)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateY_M_D(String str) {
        return StringToDateY_M_D(str, "yyyyMMdd", "yyyy-MM-dd");
    }

    public static boolean isDateFromatYYYY_MM_dd(String strDate) {
        String reglx = "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))";
        return isMatch(reglx, strDate);
    }

    public static boolean isDateFromatYYYYMMdd(String strDate) {
        String reglx = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
        return isMatch(reglx, strDate);
    }

    public static String StringToDateY_M_D(String dateStr, String dateFormatStr, String formatStr) {
        String nDate = dateStr ;
        Date date = null;
        try {
            DateFormat sdf = new SimpleDateFormat(dateFormatStr);
            date = sdf.parse(dateStr);
            SimpleDateFormat s = new SimpleDateFormat(formatStr);
            nDate = s.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nDate ;
    }


    /**
     * 转换时间格式(HH:mm:ss)
     *
     * @param str
     * @return
     */
    //字符串转指定格式时间
    public static String getMyDateHMS(String str) {
        if (!TextUtils.isEmpty(str) && str.length() == 5) {
            str = "0" + str;
        } else if (TextUtils.isEmpty(str)) {
            return "";
        }
        return StringToDateYMD(str, "HHmmss", "HH:mm:ss");
    }

    public static String StringToDateHMS(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    /**
     * yyyyMMdd 格式获取当前年
     *
     * @param dateStr
     * @return
     */
    public static int getYEAR(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        int year = 0;
        try {
            date = sdf.parse(dateStr);

            Calendar now = Calendar.getInstance();
            now.setTime(date);
            now.setTime(date);

            year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1; // 0-based!
            int day = now.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return year;
    }

    /**
     * 获取2个日期之间周六，周日的天数
     *
     * @param startDate
     * @param endDate
     * @param
     * @return
     */
    public static int getSundayNum(String startDate, String endDate) {
        int num = 0;//周六，周日的总天数

        if ("00000000".equals(startDate) || endDate.equals("00000000")) {
            return 0;
        }


        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


        List yearMonthDayList = new ArrayList();
        Date start = null, stop = null;

        String format = "";

        try {

            try {
                start = sdf.parse(startDate);
                stop = sdf.parse(endDate);
                format = "yyyyMMdd";
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                start = sdf1.parse(startDate);
                stop = sdf1.parse(endDate);
                format = "yyyy-MM-dd";
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (start.after(stop)) {
                Date tmp = start;
                start = stop;
                stop = tmp;
            }
            //将起止时间中的所有时间加到List中
            Calendar calendarTemp = Calendar.getInstance();
            calendarTemp.setTime(start);

            while (calendarTemp.getTime().getTime() <= stop.getTime()) {
                yearMonthDayList.add(new SimpleDateFormat(format)
                        .format(calendarTemp.getTime()));
                calendarTemp.add(Calendar.DAY_OF_YEAR, 1);
            }

            Collections.sort(yearMonthDayList);

            int size = yearMonthDayList.size();
            int week = 0;

            for (int i = 0; i < size; i++) {
                String day = (String) yearMonthDayList.get(i);
//                System.out.println(day);

                week = getWeek(day, format);

                if (week == 6 || week == 0) {//周六，周日
                    num++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return num;
    }



    /**
     * 获取某个日期是星期几
     *
     * @param date
     * @param format
     * @return 0-星期日
     * @author zhaigx
     * @date 2013-3-13
     */
    public static int getWeek(String date, String format) {
        Calendar calendarTemp = Calendar.getInstance();
        try {
            calendarTemp.setTime(new SimpleDateFormat(format).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = calendarTemp.get(Calendar.DAY_OF_WEEK);
        int value = i - 1;//0-星期日
        //        System.out.println(value);
        return value;
    }


    /**
     * 两个日期间差了多少天
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * "yyyy-MM-dd" "yyyyMMdd"
     * @param smdate
     * @param bdate
     * @param format
     * @return
     */
    public static int daysBetween(String smdate,String bdate,String format){
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int)between_days;
    }

    /**
     * 两个日期间差了多少天
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween2(String smdate, String bdate) {
        long between_days = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 两个日期间差了多少天多少时多少分
     *
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static String daysBetween3(String smdate, String bdate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;

        long between_days = 0;
        long between_hours= 0;
        long between_minutes = 0;
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
            between_hours = (time2 - time1) % nd / nh;
            between_minutes = (time2 - time1) % nd % nh / nm;


            String tianUnit = "天";
            String shiUnit = "时";
            String fenUnit = "分";

            String strBetween_days = "";
            String strBetween_hours = "";
            String strBetween_minutes = "";

            if (between_days > 0) {

                if (String.valueOf(between_days).length() == 1) {
                    strBetween_days = "0" + between_days;
                } else {
                    strBetween_days = String.valueOf(between_days);
                }

                if (String.valueOf(between_hours).length() == 1) {
                    strBetween_hours = "0" + between_hours;
                } else {
                    strBetween_hours = String.valueOf(between_hours);
                }

                result = strBetween_days + tianUnit + strBetween_hours + shiUnit;

            } else if (between_days <= 0) {

                if (String.valueOf(between_hours).length() == 1) {
                    strBetween_hours = "0" + between_hours;
                } else {
                    strBetween_hours = String.valueOf(between_hours);
                }

                if (String.valueOf(between_minutes).length() == 1) {
                    strBetween_minutes = "0" + between_minutes;
                } else {
                    strBetween_minutes = String.valueOf(between_minutes);
                }

                result = strBetween_hours + shiUnit + strBetween_minutes + fenUnit;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00
     * @return String 返回值为：xx天xx小时xx分
     */
    public static String getDistanceTime(String str1, String str2,String format) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long diff ;
        diff = getDiffDurTime(str1,str2,format);
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        String result = "";
        if (day !=0) {
            result = ((day+"").length()==1?"0"+day:day)+"天"+((hour+"").length()==1?"0"+hour:hour)+"小时";
        } else {
            result = ((hour+"").length()==1?"0"+hour:hour)+"时"+((min+"").length()==1?"0"+min:min) + "分";
        }
        /*if (day != 0){
            result = result + day + "天";
        }
        if (hour != 0){
            result = result + hour + "小时";
        }
        if (min != 0){
            result = result + min + "分";
        }*/
        /*if (TextUtils.isEmpty(result)) {
            result = "已结束";
        } else {
            result = "距结束:"+ result;
        }*/
        return result;
    }
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     * @param str1 时间参数 1 格式：1990-01-01 12:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00
     * @return String 返回值为：xx天xx小时xx分
     */
    public static SpannableString getDistanceTimeFormat(String str1, String str2, String format) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long diff ;
        String strDay,strHours,strMin;
        String tianUnit = "天";
        String shiUnit = "时";
        String fenUnit = "分";
        String startStr = "距离结束：";
        diff = getDiffDurTime(str1,str2,format);
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        String result = "";
        SpannableString resultSpan ;
        if (day <=0) {
            strDay = "00";
            if (hour<=0){
                strHours = "00";
            } else if (hour < 10){
                strHours = "0"+hour;
            } else {
                strHours = hour+"";
            }
            if (min<=0){
                strMin = "00";
            } else if (min < 10){
                strMin = "0"+min;
            } else {
                strMin = min+"";
            }
            result = startStr + strHours + shiUnit + strMin + fenUnit;
            resultSpan = new SpannableString(result);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), 0, (startStr).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(18, true), startStr.length(), (startStr + strHours).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), (startStr + strHours).length(), (startStr + strHours + shiUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(18, true), (startStr + strHours + shiUnit).length(), (startStr + strHours + shiUnit + strMin).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), (startStr + strHours + shiUnit + strMin).length(), (startStr + strHours + shiUnit + strMin + fenUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
            resultSpan.setSpan(styleSpan_B, 0, (result).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            if (hour<=0){
                strHours = "00";
            } else if (hour < 10){
                strHours = "0"+hour;
            } else {
                strHours = hour+"";
            }
            if (day<=0){
                strDay = "00";
            } else if (day < 10){
                strDay = "0"+day;
            } else {
                strDay = day+"";
            }
            result = startStr + strDay + tianUnit + strHours + shiUnit;
            resultSpan = new SpannableString(result);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), 0, startStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(18, true), startStr.length(), (startStr + strDay).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), (startStr + strDay).length(), (startStr + strDay + tianUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(18, true), (startStr + strDay + tianUnit).length(), (startStr + strDay + tianUnit + strHours).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            resultSpan.setSpan(new AbsoluteSizeSpan(13, true), (startStr + strDay + tianUnit + strHours).length(), (startStr + strDay + tianUnit + strHours + shiUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
            resultSpan.setSpan(styleSpan_B, 0, (result).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
       /* if (day !=0) {
            result = ((day+"").length()==1?"0"+day:day)+"天"+((hour+"").length()==1?"0"+hour:hour)+"小时";
        } else {
            result = ((hour+"").length()==1?"0"+hour:hour)+"时"+((min+"").length()==1?"0"+min:min) + "分";
        }*/

        /*if (day != 0){
            result = result + day + "天";
        }
        if (hour != 0){
            result = result + hour + "小时";
        }
        if (min != 0){
            result = result + min + "分";
        }*/
        /*if (TextUtils.isEmpty(result)) {
            result = "已结束";
        } else {
            result = "距结束:"+ result;
        }*/
        return resultSpan;
    }

    /**
     * 获取两个时间的差值
     * @param str1 开始时间
     * @param str2 结束时间
     * @param format 格式化格式
     * @return
     */
    public static long getDiffDurTime(String str1, String str2, String format) {
        long diff = 0 ;
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat df = new SimpleDateFormat(format);
        Date one;
        Date two;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            /*if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }*/
            diff = time2 - time1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static SpannableString  daysBetween4(String smdate, String bdate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;

        long between_days = 0;
        long between_hours= 0;
        long between_minutes = 0;
        String result = "";
        SpannableString ss = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            between_days = (time2 - time1) / (1000 * 3600 * 24);
            between_hours = (time2 - time1) % nd / nh;
            between_minutes = (time2 - time1) % nd % nh / nm;


            String tianUnit = "天";
            String shiUnit = "时";
            String fenUnit = "分";
            String startStr = "距离结束：";

            if (between_days > 0) {
                result = startStr + between_days + tianUnit + between_hours + shiUnit;
                ss = new SpannableString(result);
                ss.setSpan(new AbsoluteSizeSpan(13, true), 0, (startStr).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(18, true), startStr.length(), (startStr + between_days).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(13, true), (startStr + between_days).length(), (startStr + between_days + tianUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(18, true), (startStr + between_days + tianUnit).length(), (startStr + between_days + tianUnit + between_hours).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(13, true), (startStr + between_days + tianUnit + between_hours).length(), (startStr + between_days + tianUnit + between_hours + shiUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
                ss.setSpan(styleSpan_B, 0, (result).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            } else if (between_days <= 0) {
                result = startStr + between_hours + shiUnit + between_minutes + fenUnit;
                ss = new SpannableString(result);
                ss.setSpan(new AbsoluteSizeSpan(13, true), 0, (startStr).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(18, true), startStr.length(), (startStr + between_hours).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(13, true), (startStr + between_hours).length(), (startStr + between_hours + shiUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(18, true), (startStr + between_hours + shiUnit).length(), (startStr + between_hours + shiUnit + between_minutes).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(13, true), (startStr + between_hours + shiUnit + between_minutes).length(), (startStr + between_hours + shiUnit + between_minutes + fenUnit).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
                ss.setSpan(styleSpan_B, 0, (result).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ss;
    }

    /**
     * 判断正整数，对于正整数而言，可以带+号，第一个数字不能为0
     *
     * @param regex
     * @param orginal
     * @return
     */
    public static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 判断是不是科学计数法
     *
     * @param input
     * @return
     */
    public static boolean isENum(String input) {//判断输入字符串是否为科学计数法
        String regx = "^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$";//科学计数法正则表达式
        Pattern pattern = Pattern.compile(regx);
        return pattern.matcher(input).matches();
    }

    /**
     * 科学计数法转普通
     *
     * @param num
     * @return
     */
    public static String ENumToNomarl(String num) {

        String str = num;
        if (isENum(num)) {
            BigDecimal bd = new BigDecimal(num);
            str = bd.toPlainString();
        }

        return str;

    }

    /**
     * 判断是不是百分数
     *
     * @param input
     * @return
     */
    public static boolean isPersent(String input) {
        String str = "^\\d+\\.?\\d*\\%?$";
        Pattern pattern = Pattern.compile(str);
        return pattern.matcher(input).matches();
    }

    /**
     * 百分比转小数
     *
     * @return
     */
    public static Number persentToNumber(String persent) {

        try {

            if (isPersent(persent)) {
                NumberFormat nf = NumberFormat.getPercentInstance();
                Number m = nf.parse(persent);
                return m;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否是合法的url
     *
     * @param urlString
     * @return
     */
    public static boolean isUrl(String urlString) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    /**
     * 获取IP
     * @param context
     * @return
     */
    public static String getIp(final Context context) {
        String ip = null;
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == android.net.NetworkInfo.State.CONNECTED
                || mobile == android.net.NetworkInfo.State.CONNECTING) {
            ip =  getLocalIpAddress();
        }
        if (wifi == android.net.NetworkInfo.State.CONNECTED
                || wifi == android.net.NetworkInfo.State.CONNECTING) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip =(ipAddress & 0xFF ) + "." +
                    ((ipAddress >> 8 ) & 0xFF) + "." +
                    ((ipAddress >> 16 ) & 0xFF) + "." +
                    ( ipAddress >> 24 & 0xFF) ;
        }
        return ip;

    }

    /**
     *
     * @return 手机GPRS网络的IP
     */
    private static String getLocalIpAddress()
    {
        try {
            //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {//获取IPv4的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 身份证号码验证
     */
    public static boolean verifyIDNumber(String idCardNO) {
        if (idCardNO == null || idCardNO.length() <= 0) {
            return false;
        }

        idCardNO = idCardNO.toUpperCase();

        int total = 0;
        int[] w = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] v = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        for (int i = 0; i < 17; ++i) {
            int a = idCardNO.charAt(i) - '0';
            total += a * w[i];
        }
        int mod = total % 11;

        return v[mod] == idCardNO.charAt(17);
    }

    /**
     * 检查网络类型
     *
     * @return 网络具体类型
     */
    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null)
                isConnected =  mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null)
                isConnected = isConnected || (mWiFiNetworkInfo.isAvailable() && mWiFiNetworkInfo.isConnected());
        }
        return isConnected;
    }

}
