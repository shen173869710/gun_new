package com.auto.di.guan.rtm;

public class MessageEntiy {
    // 登录
    public static final int TYPE_LOGIN = 1;
    // 登出
    public static final int TYPE_LOGOUT = 2;
    // 单个操作 读
    public static final int TYPE_SINGLE_READ = 3;
    // 单个操作 开
    public static final int TYPE_SINGLE_OPEN= 4;
    // 单个操作 关
    public static final int TYPE_SINGLE_CLOSE = 5;
    // 单组操作 开
    public static final int TYPE_GROUP_OPEN = 6;
    // 单组操作 关
    public static final int TYPE_GROUP_CLOSE = 7;
    // 单组自动轮灌开
    public static final int TYPE_AUTO_OPEN = 8;
    // 单组自动轮灌 暂停
    public static final int TYPE_AUTO_STOP = 9;
    // 单组自动轮灌 开始
    public static final int TYPE_AUTO_START = 10;
    // 单组自动轮灌 关闭
    public static final int TYPE_AUTO_CLOSE = 11;
    // 单组自动轮灌 下一组
    public static final int TYPE_AUTO_NEXT = 12;
    // 单组自动轮灌 设置时间
    public static final int TYPE_AUTO_TIME = 13;
    // 自动轮灌 设备状态
    public static final int TYPE_AUTO_STATUS = 14;
    // 创建分组
    public static final int TYPE_CREATE_GROUP = 20;
    // 解散所有分组
    public static final int TYPE_DEL_GROUP = 21;
    // 退出当前小组
    public static final int TYPE_EXIT_GROUP = 22;
    // 解散一个小组
    public static final int TYPE_DISS_GROUP = 23;
    // 轮灌设置
    public static final int TYPE_GROUP_LEVEL = 30;
    // 开泵
    public static final int TYPE_BENG_OPEN = 40;
    // 关崩
    public static final int TYPE_BENG_CLOSE = 41;
    // 自动轮灌查询开启
    public static final int TYPE_AUTO_POLL_START = 1113;
    // 自动轮灌查询关闭
    public static final int TYPE_AUTO_POLL_CLOSE = 1114;
    // 轮灌操作相关信息
    public static final int TYPE_MESSAGE = 100;
    /**
     *  自动轮灌 时间同步
     */
    public static final int TYPE_TIME_COUNT = 8888888;
    /**
     *   tab点击事件
     */
    public static final int TYPE_CLICK = 999999;
    /**
     *   Activity 跳转
     */
    public static final int TYPE_ACTIVITY = 10000000;
    public static final int TYPE_ACTIVITY_STATUS_START = 100000001;
    public static final int TYPE_ACTIVITY_STATUS_FINISH = 100000002;


    public static final int TYPE_SYNC_CONTROL = 123456;

    // 农田信息
    public static final int TYPE_FARMLAND = 10000;
    /**农田信息item点击事件**/
    public static final int TYPE_FARMLAND_CLICK= 10002;

}
