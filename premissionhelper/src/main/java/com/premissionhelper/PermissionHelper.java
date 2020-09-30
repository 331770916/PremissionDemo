package com.premissionhelper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class PermissionHelper {

    private static final String SUFFIX = "$$PermissionProxy";

    public static void requestPermission(Activity activity, int requestCode, String... permission) {
        doRequestPermission(activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, int requestCode, String... permission) {
        doRequestPermission(fragment.getActivity(), permission, requestCode);
    }

    private static void doRequestPermission(Activity activity, String[] permission, int requestCode) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            doExecuteGrant(activity, requestCode, permission);
            return;
        }

        boolean rational = shouldShowPermissionRational(activity, requestCode, permission);

        if (rational) {
            return;
        }
    }

    private static void _doRequestPermission(Activity activity, String[] permission, int requestCode) {

        List<String> deniedPermissions = findDeniedPermissioins(activity, permission);

        if (deniedPermissions.size() > 0) {
            String[] denied = new String[deniedPermissions.size()];
            deniedPermissions.toArray(denied);
            ActivityCompat.requestPermissions(activity, denied, requestCode);
        } else {
            doExecuteGrant(activity, requestCode, permission);
        }

    }

    private static boolean shouldShowPermissionRational(final Activity activity, final int requestCode, final String[] permissions) {
        PermissionProxy proxy = findProxy(activity);

        List<String> deniedPermissions = findDeniedPermissioins(activity, permissions);

        if (!deniedPermissions.isEmpty()) {

            String[] denied = new String[deniedPermissions.size()];
            deniedPermissions.toArray(denied);
            return proxy.rational(requestCode, activity, denied, new PermissionRationalCallback(){
                @Override
                public void onRationalExecute() {
                    _doRequestPermission(activity, permissions, requestCode);
                }
            });
        }

        return false;
    }

    private static List<String> findDeniedPermissioins(Activity activity, String[] permissions) {
        List<String> denied = new ArrayList<>();
        for (String permission : permissions) {

            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
                    ||ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                denied.add(permission);
            }
        }

        return denied;

    }

    private static void doExecuteGrant(Activity activity, int requestCode, String[] permission) {
        PermissionProxy proxy = findProxy(activity);
        proxy.grant(requestCode, activity, permission);
    }

    private static void doExecuteDenied(Activity activity, int requestCode, String[] permission){
        PermissionProxy proxy = findProxy(activity);
        proxy.denied(requestCode, activity, permission);
    }

    private static PermissionProxy findProxy(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        try {
            Class<?> forName = aClass.forName(aClass.getName() + SUFFIX);
            return (PermissionProxy)forName.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {

        List<String> grant = new ArrayList<>();
        List<String> denied = new ArrayList<>();

        for (int i =0; i < grantResults.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                denied.add(PermissionUtil.getChineseByPremission(permission));
            } else {
                grant.add(permission);
            }
        }

        if (grant.size() > 0) {
            doExecuteGrant(activity,requestCode, permissions);
        }

        if (denied.size() > 0) {
            String[] strDenied = new String[denied.size()];
            denied.toArray(strDenied);
            doExecuteDenied(activity, requestCode, strDenied);
        }

    }
}
