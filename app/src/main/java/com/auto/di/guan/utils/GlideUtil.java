package com.auto.di.guan.utils;

import android.content.Context;
import android.widget.ImageView;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.entity.Entiy;
import com.bumptech.glide.Glide;

public class GlideUtil {
    /**
     *        加载设备图片
     * @param imageView
     * @param deviceInfo
     */
    public  static void loadDeviceImage(Context context, ImageView imageView, DeviceInfo deviceInfo) {
        int resId = R.drawable.device_stop;
        if (deviceInfo.getDeviceStatus() == 0) {
            return;
        }

        if (deviceInfo.getValveDeviceSwitchList().get(0).getValveStatus() == Entiy.CONTROL_STATUS＿RUN
                || deviceInfo.getValveDeviceSwitchList().get(1).getValveStatus() == Entiy.CONTROL_STATUS＿RUN) {
            resId = R.drawable.device_run;
        }
        Glide.with(BaseApp.getContext()).asGif().load(resId).into(imageView);
//        try {
//            GifDrawable gifFromResource = new GifDrawable(context.getResources(), resId);
//            imageView.setImageDrawable(gifFromResource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *      加载组信息
     * @param imageView
     */
    public  static void loadGroupImage(ImageView imageView, int getGroupId) {
        if (getGroupId < 1 || getGroupId > 50 ) {
            return;
        }
        Glide.with(BaseApp.getContext()).load(Entiy.GROUP_IMAGE[getGroupId]).into(imageView);
    }

    /**
     *        加载设备图片
     * @param info
     */
    public  static void loadControlImage(Context context, ImageView imageView, ControlInfo info) {
        if (info == null || info.getValveStatus() == 0) {
            return;
        }

        int resId = -1;
        boolean isUp = false;
        if ("0".equals(info.getProtocalId())) {
            isUp = true;
        }
        switch (info.getValveStatus()) {
            case Entiy.CONTROL_STATUS＿CONNECT:
                if (isUp) {
                    resId = R.mipmap.control_up_close;
                }else {
                    resId = R.mipmap.control_down_close;
                }
                Glide.with(context).load(resId).into(imageView);
                break;
            case Entiy.CONTROL_STATUS＿RUN:
                if (isUp) {
                    resId = R.mipmap.control_up_run;
                }else {
                    resId = R.mipmap.control_down_run;
                }
                Glide.with(context).asGif().load(resId).into(imageView);
                break;
            case Entiy.CONTROL_STATUS＿ERROR:
            case Entiy.CONTROL_STATUS＿NOTCLOSE:
                if (isUp) {
                    resId = R.mipmap.control_up_error;
                }else {
                    resId = R.mipmap.control_down_error;
                }
                Glide.with(context).asGif().load(resId).into(imageView);
                break;
        }
//        if (resId == -1) {
//            return;
//        }
//        try {
//            GifDrawable gifFromResource = new GifDrawable(context.getResources(), resId);
//            imageView.setImageDrawable(gifFromResource);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 加载单独的设备图片
     *
     * @param info
     */
    public static void loadControlExpand(Context context, ImageView imageView, ControlInfo info) {
        int resId = -1;
        switch (info.getValveStatus()) {
            case Entiy.CONTROL_STATUS＿CONNECT:
                resId = R.mipmap.light_1;
                break;
            case Entiy.CONTROL_STATUS＿RUN:
                resId = R.mipmap.light_2;
                break;
            case Entiy.CONTROL_STATUS＿ERROR:
            case Entiy.CONTROL_STATUS＿NOTCLOSE:
                resId = R.mipmap.light_3;
                break;
        }
        if (resId == -1) {
            return;
        }
        Glide.with(context).load(resId).into(imageView);
    }

}
