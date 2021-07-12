package com.auto.di.guan.floatWindow;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.auto.di.guan.BaseApp;

import java.util.List;

/**
 * Created by yhao on 17-12-1.
 * 用于控制悬浮窗显示周期
 * 使用了三种方法针对返回桌面时隐藏悬浮按钮
 * 1.startCount计数，针对back到桌面可以及时隐藏
 * 2.监听home键，从而及时隐藏
 * 3.resumeCount计时，针对一些只执行onPause不执行onStop的奇葩情况
 */

class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final long delay = 300;
    private Handler mHandler;
    private Class[] activities;
    private boolean showFlag;
    private int startCount;
    private int resumeCount;
    private boolean appBackground;
    private LifecycleListener mLifecycleListener;
    private static ResumedListener sResumedListener;
    private static int num = 0;
    private boolean isShowHome = true;


    FloatLifecycle(Context applicationContext, boolean showFlag, Class[] activities, LifecycleListener lifecycleListener) {
        this.showFlag = showFlag;
        this.activities = activities;
        num++;
        mLifecycleListener = lifecycleListener;
        mHandler = new Handler();
        ((Application) applicationContext).registerActivityLifecycleCallbacks(this);
        applicationContext.registerReceiver(this, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    public void setIsShowHome(boolean isShowHome) {
        this.isShowHome = isShowHome;
    }

    public static void setResumedListener(ResumedListener resumedListener) {
        sResumedListener = resumedListener;
    }

    private boolean needShow(Activity activity) {
        if (activities == null) {
            return true;
        }
        for (Class a : activities) {
            if (a.isInstance(activity)) {
                return showFlag;
            }
        }
        return !showFlag;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (sResumedListener != null) {
            num--;
            if (num == 0) {
                sResumedListener.onResumed();
                sResumedListener = null;
            }
        }
        resumeCount++;
        Log.e("FloatLifecycle", "onActivityResumed resumeCount:" + resumeCount + " activity:" + activity.getClass().getSimpleName());
        if (needShow(activity)) {
            mLifecycleListener.onShow();
        } else {
            mLifecycleListener.onHide();
        }
        if (appBackground) {
            appBackground = false;
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        resumeCount--;
        Log.e("FloatLifecycle", "onActivityPaused resumeCount:" + resumeCount + " activity:" + activity.getClass().getSimpleName());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("FloatLifecycle", "onActivityPaused exec: resumeCount:" + resumeCount);
                    if (resumeCount == 0) {
                        appBackground = true;
                        mLifecycleListener.onBackToDesktop();
                    }
                }
            }, delay);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        startCount++;
        Log.e("FloatLifecycle", "onActivityStarted startCount:" + startCount + " activity:" + activity.getClass().getSimpleName());
        if (isRunningForeground()) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLifecycleListener.onShow();
                }
            }, 100);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (startCount == 0) {
            mLifecycleListener.onBackToDesktop();
        }

        //返回到桌面不需要显示
        if (!isShowHome) {
            if (!isRunningForeground()) {
                mLifecycleListener.onHide();
            }
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                //返回到桌面不需要显示
                if (!isShowHome) {
                    Log.e("FloatLifecycle", "onBackToDesktop");
                    if(mHandler != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mLifecycleListener.onHide();
                            }
                        });
                    }
                }
                mLifecycleListener.onBackToDesktop();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }


    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) BaseApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(BaseApp.getInstance().getApplicationInfo().processName)) {
                    Log.d("FloatLifecycle", "EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        Log.d("FloatLifecycle", "EntryActivity isRunningBackGround");
        return false;
    }
}
