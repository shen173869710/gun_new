package com.auto.di.guan.dialog;

import android.content.Context;

public class DialogUtil {

    /**
     *       保存分组
     * @param context
     * @param onDialogClick
     */
    public static void showSaveGroup(Context context, OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否保存已经选择的分组?";
        content.cancle = "取消";
        content.ok = "确认";
        OptionDialog.show(context,content,onDialogClick);
    }


    /**
     *   暂停计时
     */
    public static void showStopCount(Context context, OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否进行暂停计时操作?";
        content.cancle = "取消";
        content.ok = "确认暂停";
        OptionDialog.show(context,content,onDialogClick);
    }

    /**
     *   开始计时
     */
    public static void showStartCount(Context context, OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否进行开始计时操作?";
        content.cancle = "取消";
        content.ok = "确认开始";
        OptionDialog.show(context,content,onDialogClick);
    }

    /**
     *   开启水泵
     */
    public static void startSocket(Context context, String name,OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否开启"+name;
        content.cancle = "取消";
        content.ok = "开启";
        OptionDialog.show(context,content,onDialogClick);
    }

    /**
     *   关闭水泵
     */
    public static void closeSocket(Context context, String name,OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否关闭"+name;
        content.cancle = "取消";
        content.ok = "关闭";
        OptionDialog.show(context,content,onDialogClick);
    }


    /**
     *   关闭水泵
     */
    public static void addBeng(Context context, String name,OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否添加水泵"+name;
        content.cancle = "取消";
        content.ok = "确定";
        OptionDialog.show(context,content,onDialogClick);
    }

    /**
     *   关闭水泵
     */
    public static void delBeng(Context context, String name,OnDialogClick onDialogClick) {
        DialogContent content = new DialogContent();
        content.desc = "是否删除水泵"+name;
        content.cancle = "取消";
        content.ok = "确定";
        OptionDialog.show(context,content,onDialogClick);
    }
}
