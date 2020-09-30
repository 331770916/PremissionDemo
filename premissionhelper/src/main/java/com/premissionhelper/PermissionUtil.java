package com.premissionhelper;

import android.Manifest;

public class PermissionUtil {


    public static String getChineseByPremission(String[] permissions) {

        if (permissions != null && permissions.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i =0; i < permissions.length; i++) {
                sb.append(getChineseByPremission(permissions[i])).append("\n");
            }

            return sb.toString();
        }

        return null;
    }

    public static String getChineseByPremission(String permission) {

        if (Manifest.permission.WRITE_CONTACTS.equals(permission)) {
            return "写入联系人,但不可读取";
        } else if (Manifest.permission.GET_ACCOUNTS.equals(permission)) {
            return "允许程序访问账户Gmail列表";
        } else if (Manifest.permission.READ_CONTACTS.equals(permission)) {
            return "允许程序访问联系人通讯录信息";
        } else if (Manifest.permission.READ_CALL_LOG.equals(permission)) {
            return "读取通话记录";
        } else if (Manifest.permission.READ_PHONE_STATE.equals(permission)) {
            return "允许程序访问电话状态";
        } else if (Manifest.permission.CALL_PHONE.equals(permission)) {
            return "允许程序从非系统拨号器里拨打电话";
        } else if (Manifest.permission.WRITE_CALL_LOG.equals(permission)) {
            return "允许程序写入（但是不能读）用户的联系人数据";
        } else if (Manifest.permission.USE_SIP.equals(permission)) {
            return "允许程序使用SIP视频服务";
        } else if (Manifest.permission.PROCESS_OUTGOING_CALLS.equals(permission)) {
            return "允许程序监视，修改或放弃播出电话";
        } else if (Manifest.permission.READ_CALENDAR.equals(permission)) {
            return "允许程序读取用户的日程信息";
        } else if (Manifest.permission.WRITE_CALENDAR.equals(permission)) {
            return "允许程序写入日程，但不可读取";
        } else if (Manifest.permission.CAMERA.equals(permission)) {
            return "允许程序访问摄像头进行拍照";
        } else if (Manifest.permission.BODY_SENSORS.equals(permission)) {
            return "允许应用程序访问用户使用的传感器来测量他/她的身体内发生了什么，如心率仪";
        } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            return "允许一个程序访问CellID或 WiFi热点来获取粗略的位置";
        } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permission)) {
            return "允许一个程序访问CellID或 WiFi热点来获取粗略的位置";
        } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
            return "程序可以读取设备外部存储空间(内置SDcard和外置SDCard的文件)";
        } else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
            return "允许程序写入外部存储";
        } else if (Manifest.permission.READ_SMS.equals(permission)) {
            return "允许程序读取短信内容";
        } else if (Manifest.permission.RECEIVE_WAP_PUSH.equals(permission)) {
            return "允许程序接收WAP PUSH信息";
        } else if (Manifest.permission.RECEIVE_MMS.equals(permission)) {
            return "允许程序接收彩信";
        } else if (Manifest.permission.RECEIVE_SMS.equals(permission)) {
            return "允许程序接收短信";
        } else if (Manifest.permission.SEND_SMS.equals(permission)) {
            return "发送和查看信息";
        } else if (Manifest.permission.READ_PHONE_NUMBERS.equals(permission)) {
            return "拨打电话和管理通话";
        } else if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
            return "允许程序录制声音通过手机或耳机的麦克";
        } else if (Manifest.permission.ADD_VOICEMAIL.equals(permission)) {
            return "允许一个应用程序添加语音邮件系统";
        }

        return null;
    }

}
