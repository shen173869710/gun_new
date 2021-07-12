package com.auto.di.guan.rtm;

import android.text.TextUtils;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.entity.BengOptionEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.ActivityEvent;
import com.auto.di.guan.event.ActivityItemEvent;
import com.auto.di.guan.event.TabClickEvent;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.event.AutoTaskEvent;
import com.auto.di.guan.event.ChooseGroupEvent;
import com.auto.di.guan.event.Fragment32Event;
import com.auto.di.guan.event.LoginEvent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.GzipUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 解析消息
 */
public class MessageParse {
    public static final String TAG = "MessageParse------web------";

    public static void praseData(String data, String peerId) {

        String  res = GzipUtil.ungzip(data);
        MessageInfo info = new Gson().fromJson(res, MessageInfo.class);
        if (info == null) {
            LogUtils.e(TAG, "gson 数据解析异常");
            return;
        }

        GroupInfo groupInfo = info.getGroupInfo();
        ControlInfo controlInfo = info.getControlInfo();

        switch (info.getType()) {
            case MessageEntiy.TYPE_LOGIN:
                // 登录
                LogUtils.e(TAG, "同步登陆信息 peerId = "+peerId);
                MessageSend.syncLogin(peerId);
                EventBus.getDefault().post(new LoginEvent(true));
                break;
            case MessageEntiy.TYPE_LOGOUT:
                // 登出
                EventBus.getDefault().post(new LoginEvent(false));
                break;
            case MessageEntiy.TYPE_SINGLE_READ:
                // 单个操作 读
            case MessageEntiy.TYPE_SINGLE_OPEN:
                // 单个操作 开
            case MessageEntiy.TYPE_SINGLE_CLOSE:
                // 单个操作 关
                if (controlInfo != null) {
                    dealSingle(info.getType(), controlInfo);
                }
                break;
            case MessageEntiy.TYPE_GROUP_OPEN:
                // 单组操作 开
            case MessageEntiy.TYPE_GROUP_CLOSE:
                // 单组操作 关
                if (groupInfo != null) {
                    dealGroup(info.getType(), groupInfo);
                }
                break;
            case MessageEntiy.TYPE_AUTO_OPEN:
                // 单组自动轮灌开
                dealAutoOpen();
                break;
            case MessageEntiy.TYPE_AUTO_CLOSE:
                // 单组自动轮灌开
                dealAutoClose();
                break;
            case MessageEntiy.TYPE_AUTO_START:
                // 单组自动轮灌 暂停
                if (groupInfo != null) {
                    dealAutoStart(groupInfo);
                }
                break;
            case MessageEntiy.TYPE_AUTO_STOP:
                // 单组自动轮灌 开始
                if (groupInfo != null) {
                    dealAutoStop(groupInfo);
                }
                break;
            case MessageEntiy.TYPE_AUTO_NEXT:
                // 单组自动轮灌 下一组
                if (groupInfo != null) {
                    dealAutoNext(groupInfo);
                }
                break;
            case MessageEntiy.TYPE_AUTO_TIME:
                // 自动轮灌 设置时间
                if (groupInfo != null) {
                    dealAutoTime(groupInfo);
                }
                break;
            case MessageEntiy.TYPE_CREATE_GROUP:
                if (info.getDeviceInfos() != null) {
                    dealCreateGroup(info.getGroupInfo(), info.getDeviceInfos());
                    MessageSend.syncGroupAndDeviceInfo(MessageEntiy.TYPE_CREATE_GROUP);
                }
                break;
            case MessageEntiy.TYPE_DEL_GROUP:
                dealDelGroup();
                MessageSend.syncGroupAndDeviceInfo(MessageEntiy.TYPE_DEL_GROUP);
                break;
            case MessageEntiy.TYPE_EXIT_GROUP:
                LogUtils.e(TAG, "退出小组"+info.getGroupInfo());
                if (info.getControlInfo() != null) {
                    dealExitGroup(info.getControlInfo());
                    MessageSend.syncGroupAndDeviceInfo(MessageEntiy.TYPE_EXIT_GROUP);
                }
                break;
            case MessageEntiy.TYPE_DISS_GROUP:
                dealDissGroup(info.getGroupInfo());
                MessageSend.syncGroupAndDeviceInfo(MessageEntiy.TYPE_DISS_GROUP);
                break;
            case MessageEntiy.TYPE_GROUP_LEVEL:
                dealGroupLevel(info.getGroupInfos());
                MessageSend.syncGroupAndDeviceInfo(MessageEntiy.TYPE_GROUP_LEVEL);
                break;
            case MessageEntiy.TYPE_BENG_OPEN:
                dealBengOpen(info.getPostion(),true);
                break;
            case MessageEntiy.TYPE_BENG_CLOSE:
                dealBengOpen(info.getPostion(),false);
                break;
            case MessageEntiy.TYPE_MESSAGE:

                break;
            case MessageEntiy.TYPE_CLICK:
                EventBus.getDefault().post(new TabClickEvent(info.getIndex()));
                MessageSend.syncClickEvent();
                break;
            case MessageEntiy.TYPE_ACTIVITY:
                EventBus.getDefault().post(new ActivityEvent(info.getIndex()));
                MessageSend.syncActivityEvent();
                break;

            case MessageEntiy.TYPE_FARMLAND:
                /**
                 *  农田信息添加
                 */
                if (!TextUtils.isEmpty(info.getSn())) {
                    EventBus.getDefault().post(new ActivityItemEvent(info.getIndex(),info.getSn(), info.getSnType()));
                }

                break;
            case MessageEntiy.TYPE_FARMLAND_CLICK:
                /**
                 *  农田信息item点击
                 */
                EventBus.getDefault().post(new ActivityItemEvent(info.getIndex()));
                break;
        }
    }
    /**
     * =============================================================================================================================
     */
    /**
     * 处理单个操作
     */
    public static void dealSingle(int type, ControlInfo controlInfo) {
        if (type == MessageEntiy.TYPE_SINGLE_READ) {
            LogUtils.e(TAG, "收到单个读操作");
            TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_READ, Entiy.ACTION_TYPE_4);
            TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_READ,controlInfo);
            TaskManager.getInstance().startTask();
        } else if (type == MessageEntiy.TYPE_SINGLE_OPEN) {
            LogUtils.e(TAG, "收到单个开启操作");
            TaskFactory.createOpenTask(controlInfo);
            TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_OPEN_READ, Entiy.ACTION_TYPE_4);
            TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_OPEN_READ,controlInfo);
            TaskManager.getInstance().startTask();
        } else if (type == MessageEntiy.TYPE_SINGLE_CLOSE) {
            LogUtils.e(TAG, "收到单个关闭操作");
            TaskFactory.createCloseTask(controlInfo);
            TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_CLOSE_READ, Entiy.ACTION_TYPE_4);
            TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_CLOSE_READ,controlInfo);
            TaskManager.getInstance().startTask();
        }
    }
    /**
     * =============================================================================================================================
     */
    /**
     * 单组操作开关
     */
    public static void dealGroup(int type, GroupInfo groupInfo) {
        if (type == MessageEntiy.TYPE_GROUP_OPEN) {
            LogUtils.e(TAG, "收到单组开启操作");
            TaskFactory.createGroupOpenTask(groupInfo);
            TaskManager.getInstance().startTask();
        } else if (type == MessageEntiy.TYPE_GROUP_CLOSE) {
            LogUtils.e(TAG, "收到单组关闭操作");
            TaskFactory.createGroupCloseTask(groupInfo);
            TaskManager.getInstance().startTask();
        }
    }

    /**
     * =============================================================================================================================
     */

    /**
     * 处理自动轮灌开启
     */
    public static void dealAutoOpen() {
        LogUtils.e(TAG, "收到自动轮灌开启操作");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupSettingList();
        if (groupInfos != null && groupInfos.size() > 0) {
            if (GroupInfoSql.queryOpenGroupList() != null) {
                ToastUtils.showLongToast("自动轮灌正在运行当中, 无法再次开启自动轮灌");
                return;
            }
            PollingUtils.stopPollingService(BaseApp.getContext());
            TaskFactory.createAutoGroupOpenTask(groupInfos.get(0));
            TaskManager.getInstance().startTask();
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_OPEN);
        }
    }

    /**
     * 处理自动轮毂关闭
     */
    private static void dealAutoClose() {
        LogUtils.e(TAG, "收到自动轮灌关闭操作");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupSettingList();
        if (groupInfos != null && groupInfos.size() > 0) {
            for (int i = 0; i < groupInfos.size(); i++) {
                GroupInfo info = groupInfos.get(i);
                info.setGroupRunTime(0);
                info.setGroupTime(0);
                info.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            }
            GroupInfoSql.updateGroupList(groupInfos);
            PollingUtils.stopPollingService(BaseApp.getContext());
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_FINISH));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_CLOSE);
        }
    }

    /**
     * 处理自动轮毂开始
     */
    private static void dealAutoStart(GroupInfo info) {
        LogUtils.e(TAG, "收到自动轮灌开始操作");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupInfoById(info.getGroupId());
        if (groupInfos == null || groupInfos.size() != 1) {
            LogUtils.e(TAG, "自动轮灌开始,当前组不存在");
        } else {
            GroupInfo groupInfo = groupInfos.get(0);
            groupInfo.setGroupStop(0);
            GroupInfoSql.updateGroup(groupInfo);
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_START, groupInfo));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_START);
        }
    }

    /**
     * 处理自动轮毂暂停
     */
    private static void dealAutoStop(GroupInfo info) {
        LogUtils.e(TAG, "收到自动轮灌暂停操作");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupInfoById(info.getGroupId());
        if (groupInfos == null || groupInfos.size() != 1) {
            LogUtils.e(TAG, "自动轮灌暂停,当前组不存在");
        } else {
            GroupInfo groupInfo = groupInfos.get(0);
            groupInfo.setGroupStop(1);
            GroupInfoSql.updateGroup(groupInfo);
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_STOP, groupInfo));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_STOP);
        }
    }

    /**
     * 处理自动轮毂下一组
     */
    private static void dealAutoNext(GroupInfo info) {
        LogUtils.e(TAG, "收到自动轮灌开启下一组操作");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupInfoById(info.getGroupId());
        if (groupInfos == null || groupInfos.size() != 1) {
            LogUtils.e(TAG, "自动轮灌暂停,当前组不存在");
        } else {
            GroupInfo groupInfo = groupInfos.get(0);
            groupInfo.setGroupRunTime(info.getGroupTime());
            GroupInfoSql.updateGroup(groupInfo);
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_NEXT, groupInfo));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_NEXT);
        }
    }


    /**
     * 处理自动轮毂设置时间
     */
    private static void dealAutoTime(GroupInfo info) {
        LogUtils.e(TAG, "收到轮灌设置时间");
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupInfoById(info.getGroupId());
        if (groupInfos == null || groupInfos.size() != 1) {
            LogUtils.e(TAG, "收到轮灌设置时间,当前组不存在");
        } else {
            GroupInfo groupInfo = groupInfos.get(0);
            groupInfo.setGroupTime(info.getGroupTime());
            GroupInfoSql.updateGroup(groupInfo);
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_TIME, groupInfo));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_TIME);
        }
    }

    /**
     * =============================================================================================================================
     *   分组相关操作
     */
    /**
     * 创建一个分组
     * 如果组对象不为空 则给组添加
     */
    private static void dealCreateGroup(GroupInfo groupInfo, List<DeviceInfo> list) {
        LogUtils.e(TAG, "收到创建分组");
        int groupId = 0;
        if (groupInfo == null) {
            groupInfo = new GroupInfo();
        }else {
            groupId = groupInfo.getGroupId();
        }
        List<LevelInfo> levelInfos = LevelInfoSql.queryUserLevelInfoListByGroup(false);
        if (levelInfos != null && levelInfos.size() > 0) {
            if (groupId == 0) {
                LevelInfo levelInfo = levelInfos.get(0);
                groupId = levelInfo.getLevelId();
                groupInfo.setGroupId(groupId);
                groupInfo.setGroupLevel(groupId);
                groupInfo.setGroupName(groupId + "");
                levelInfo.setIsGroupUse(true);
                GroupInfoSql.insertGroup(groupInfo);
                LevelInfoSql.updateLevelInfo(levelInfo);
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (list.get(i).getValveDeviceSwitchList().get(0).isSelect()) {
                    list.get(i).getValveDeviceSwitchList().get(0).setValveGroupId(groupId);
                }
                if (list.get(i).getValveDeviceSwitchList().get(1).isSelect()) {
                    list.get(i).getValveDeviceSwitchList().get(1).setValveGroupId(groupId);
                }

            }
            DeviceInfoSql.updateDeviceList(list);
        }
        EventBus.getDefault().post(new ChooseGroupEvent());
    }

    /**
     * 删除全部分组
     */
    private static void dealDelGroup() {
        LogUtils.e(TAG, "收到删除全部分组");
        List<DeviceInfo> deviceInfos = DeviceInfoSql.queryDeviceList();
        int size = deviceInfos.size();
        for (int i = 0; i < size; i++) {
            DeviceInfo deviceInfo = deviceInfos.get(i);
            deviceInfo.getValveDeviceSwitchList().get(0).setValveGroupId(0);
            deviceInfo.getValveDeviceSwitchList().get(0).setSelect(false);
            deviceInfo.getValveDeviceSwitchList().get(1).setValveGroupId(0);
            deviceInfo.getValveDeviceSwitchList().get(1).setSelect(false);
        }
        DeviceInfoSql.updateDeviceList(deviceInfos);
        List<GroupInfo> groupInfos = GroupInfoSql.queryGroupList();
        int count = groupInfos.size();
        for (int i = 0; i < count; i++) {
            GroupInfoSql.deleteGroup(groupInfos.get(i));
        }
        groupInfos.clear();
        LevelInfoSql.delLevelInfoList();
        if (LevelInfoSql.queryLevelInfoList().size() == 0) {
            List<LevelInfo> levelInfos = new ArrayList<>();
            for (int i = 1; i < 200; i++) {
                LevelInfo info = new LevelInfo();
                info.setLevelId(i);
                info.setIsGroupUse(false);
                info.setIsLevelUse(false);
                levelInfos.add(info);
            }
            LevelInfoSql.insertLevelInfoList(levelInfos);
        }
        EventBus.getDefault().post(new ChooseGroupEvent());
    }

    /**
     * 退出当前组
     */
    private static void dealExitGroup(ControlInfo info) {
        LogUtils.e(TAG, "收到退出当前分组");
        info.setValveGroupId(0);
        info.setSelect(false);
        ControlInfoSql.updateControl(info);
        EventBus.getDefault().post(new ChooseGroupEvent());
    }

    /**
     * 解散一个小组
     */
    private static void dealDissGroup(GroupInfo groupInfo) {
        LogUtils.e(TAG, "解散一个小组");
        if (groupInfo == null) {
            LogUtils.e(TAG, "解散分组 组ID为空");
            return;
        }

        int groupId = groupInfo.getGroupId();
        List<ControlInfo> controlInfos = ControlInfoSql.queryControlList(groupId);
        int size = controlInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo info = controlInfos.get(i);
            info.setValveGroupId(0);
            info.setSelect(false);
            ControlInfoSql.updateControl(info);
        }
        LevelInfo levelInfo = LevelInfoSql.queryLevelInfo(groupId);
        levelInfo.setIsGroupUse(false);
        levelInfo.setIsLevelUse(false);
        LevelInfoSql.updateLevelInfo(levelInfo);
        GroupInfoSql.deleteGroup(groupId);
        EventBus.getDefault().post(new ChooseGroupEvent());
    }


    /**
     * =============================================================================================================================
     *   轮灌设置相关
     */
    public static void dealGroupLevel(List<GroupInfo> groupInfos) {
        LogUtils.e(TAG, "轮灌设置");
        if(groupInfos == null) {
            LogUtils.e(TAG, "轮灌设置组信息为null");
            return;
        }
        int size = groupInfos.size();
        HashMap<Integer, Integer> lv = new HashMap<>();
        for (int i = 0; i < size; i++) {
            GroupInfo groupInfo = groupInfos.get(i);
            if (groupInfo.getGroupIsJoin() == 1) {
                if (groupInfo.getGroupTime() == 0 || groupInfo.getGroupLevel() == 0) {
                    ToastUtils.showLongToast("轮灌优先级或者轮灌时长不能为0");
                    return;
                }
            }else {
                groupInfo.setGroupRunTime(0);
                groupInfo.setGroupLevel(0);
                groupInfo.setGroupTime(0);
            }
            int level = groupInfo.getGroupLevel();
            if (level > 0) {
                if (lv.containsKey(level)) {
                    ToastUtils.showLongToast("不能设置相同的轮灌优先级,或者优先级不能为空");
                    return;
                }
                lv.put(level,level);
            }
        }
        GroupInfoSql.updateGroupList(groupInfos);
        EventBus.getDefault().post(new Fragment32Event());
    }

    /**
     * =============================================================================================================================
     *   开泵 管泵
     */
    public static void dealBengOpen(int postion, boolean open) {
        EventBus.getDefault().post(new BengOptionEvent(postion,open));
    }
}
