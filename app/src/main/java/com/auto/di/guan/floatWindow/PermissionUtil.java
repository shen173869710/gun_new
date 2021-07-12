package com.auto.di.guan.floatWindow;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

/**
 * Created by yhao on 2017/12/29.
 * https://github.com/yhaolpz
 */

public class PermissionUtil {

    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return hasPermissionBelowMarshmallow(context);
        }
    }

    static boolean hasPermissionOnActivityResult(Context context) {
        boolean result = false;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            result = hasPermissionForO(context);
            if(result){
                return true;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            result = Settings.canDrawOverlays(context);
            result = canDrawOverlaysO(context);
        } else {
            result = hasPermissionBelowMarshmallow(context);
        }
        return result;
//        if(result){
//            return true;
//        }
        //如果还是认为没有权限，再重新判断下
//        result = checkPermission(context);
//        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean canDrawOverlaysO(Context context) {
        AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsMgr == null) return false;
        int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context.getPackageName());
        Log.e("TTT", "android:system_alert_window: mode=" + mode);
        return AppOpsManager.MODE_ERRORED != mode;

    }



//    private static boolean checkPermission(Context context) {
//        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
//        if (Build.VERSION.SDK_INT < 23) {
//            if (RomUtils.checkIsMiuiRom()) {
//                return miuiPermissionCheck(context);
//            } else if (RomUtils.checkIsMeizuRom()) {
//                return meizuPermissionCheck(context);
//            } else if (RomUtils.checkIsHuaweiRom()) {
//                return huaweiPermissionCheck(context);
//            } else if (RomUtils.checkIs360Rom()) {
//                return qikuPermissionCheck(context);
//            } else if (RomUtils.checkIsOppoRom()) {
//                return oppoROMPermissionCheck(context);
//            }
//        }
//        return commonROMPermissionCheck(context);
//    }
//
//    private static boolean huaweiPermissionCheck(Context context) {
//        return HuaweiUtils.checkFloatWindowPermission(context);
//    }
//
//    private static boolean miuiPermissionCheck(Context context) {
//        return MiuiUtils.checkFloatWindowPermission(context);
//    }
//
//    private static boolean meizuPermissionCheck(Context context) {
//        return MeizuUtils.checkFloatWindowPermission(context);
//    }
//
//    private static boolean qikuPermissionCheck(Context context) {
//        return QikuUtils.checkFloatWindowPermission(context);
//    }
//
//    private static boolean oppoROMPermissionCheck(Context context) {
//        return OppoUtils.checkFloatWindowPermission(context);
//    }
//
//    private static boolean commonROMPermissionCheck(Context context) {
//        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
//        if (RomUtils.checkIsMeizuRom()) {
//            return meizuPermissionCheck(context);
//        } else {
//            Boolean result = true;
//            if (Build.VERSION.SDK_INT >= 23) {
//                try {
//                    Class clazz = Settings.class;
//                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
//                    result = (Boolean) canDrawOverlays.invoke(null, context);
//                } catch (Exception e) {
//                    Log.e("PermissionUtil", Log.getStackTraceString(e));
//                }
//            }
//            return result;
//        }
//    }


    /**
     * 6.0以下判断是否有权限
     * 理论上6.0以上才需处理权限，但有的国内rom在6.0以下就添加了权限
     * 其实此方式也可以用于判断6.0以上版本，只不过有更简单的canDrawOverlays代替
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static boolean hasPermissionBelowMarshmallow(Context context) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Method dispatchMethod = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            return AppOpsManager.MODE_ALLOWED == (Integer) dispatchMethod.invoke(
                    manager, 24, Binder.getCallingUid(), context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 用于判断8.0时是否有权限，仅用于OnActivityResult
     * 针对8.0官方bug:在用户授予权限后Settings.canDrawOverlays或checkOp方法判断仍然返回false
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean hasPermissionForO(Context context) {
        try {
            WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false;
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception e) {
            LogUtil.e("hasPermissionForO e:" + e.toString());
        }
        return false;
    }


}
