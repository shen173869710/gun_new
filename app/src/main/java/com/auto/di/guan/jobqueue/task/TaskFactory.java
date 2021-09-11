package com.auto.di.guan.jobqueue.task;

import android.text.TextUtils;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.GroupStatusEvent;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.event.AutoTaskEvent;
import com.auto.di.guan.rtm.MessageEntiy;
import com.auto.di.guan.rtm.MessageSend;
import com.auto.di.guan.utils.DateUtils;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建操作任务
 */
public class TaskFactory {

    public static final String TAG = "TaskFactory";


    /**
     *   创建自动轮灌查询
     */
    public static void createPullTask(GroupInfo groupInfo) {
        List<ControlInfo> controlInfos = ControlInfoSql.queryControlList(groupInfo.getGroupId());
        if (controlInfos != null) {
            int size = controlInfos.size();
            for (int i = 0; i < size; i++) {
                TaskFactory.createPollReadTask(controlInfos.get(i), TaskEntiy.TASK_OPTION_READ ,Entiy.ACTION_TYPE_4);
            }

            FloatWindowUtil.getInstance().cleanList();
            TaskFactory.createPollReadEndTask();
            TaskManager.getInstance().startTask();
            PollingUtils.isRun = true;

        }
    }
    /**
     * 创建自动轮灌查询
     *
     * @param info
     */
    public static void createPollReadTask(ControlInfo info, int type, int actionType) {
        final String cmd = Entiy.cmdRead(getProjectId(), info.getDeviceProtocalId());
        TaskManager.getInstance().addTask(new ReadTask(type, cmd, info,actionType));
    }
    /**
     * 自动轮灌读取
     */
    public static void createPollReadEndTask() {
        TaskManager.getInstance().addTask(new SingleEndTask(TaskEntiy.TASK_POLL_END, ""));
    }

    /************************************************************************************************
     *          绑定gid bid
     ************************************************************************************************
     *
     *  /
     /**
     *  创建写入和读取gid  task
     */
    public static void createGidTak() {
        TaskManager.getInstance().clearTask();
        TaskManager.getInstance().addTask(new BindIdTask(TaskEntiy.TASK_TYPE_GID, Entiy.writeGid(getProjectId())));
        TaskManager.getInstance().addTask(new ReadIdTask(TaskEntiy.TASK_READ_GID, "rgid"));
        TaskManager.getInstance().startTask();
    }

    /**
     * 创建写入和读取gid  task
     */
    public static void createBidTak(String bid) {
        TaskManager.getInstance().clearTask();
        TaskManager.getInstance().addTask(new BindIdTask(TaskEntiy.TASK_TYPE_BID, Entiy.writeBid(bid)));
        TaskManager.getInstance().addTask(new ReadIdTask(TaskEntiy.TASK_READ_BID, "rbid"));
        TaskManager.getInstance().startTask();
    }

    /************************************************************************************************
     *         手动 单个开关阀
     ************************************************************************************************
     *
     *  /
     /**
     *        创建开阀操作
     * @param info
     */
    public static void createOpenTask(ControlInfo info) {
        final String cmd = Entiy.cmdOpen(getProjectId(), info.getDeviceProtocalId(), info.getProtocalId());
        TaskManager.getInstance().addTask(new OpenTask(TaskEntiy.TASK_OPTION_OPEN, cmd, info));
    }

    /**
     * 创建关阀操作
     *
     * @param info
     */
    public static void createCloseTask(ControlInfo info) {
        final String cmd = Entiy.cmdClose(getProjectId(), info.getDeviceProtocalId(), info.getProtocalId());
        TaskManager.getInstance().addTask(new CloseTask(TaskEntiy.TASK_OPTION_ClOSE, cmd, info));
    }

    /**
     * 创建开阀读取状态操作
     *
     * @param info
     */
    public static void createReadTask(ControlInfo info, int type, int actionType) {
        final String cmd = Entiy.cmdRead(getProjectId(), info.getDeviceProtocalId());
        TaskManager.getInstance().addTask(new ReadTask(type, cmd, info,actionType));
    }

    /**
     * 创建开阀单个读取状态操作
     *
     * @param info
     */
    public static void createReadSingleTask(ControlInfo info, int type, int actionType) {
        final String cmd = Entiy.cmdRead(getProjectId(), info.getDeviceProtocalId());
        TaskManager.getInstance().addTask(new ReadSingleTask(type, cmd, info,actionType));
    }

    /**
     * 创建读取结束标志位
     */
    public static void createReadEndTask(int taskType, ControlInfo controlInfo) {
        TaskManager.getInstance().addTask(new SingleEndTask(taskType, "", controlInfo));
    }



    /************************************************************************************************
     *         手动 单组开关
     ************************************************************************************************
     *
     *  /

     /**
     *           单组操作读取结束标志位
     *
     * @param type      类型
     */
    public static void createGroupReadEndTask(int type, GroupInfo info) {
        TaskManager.getInstance().addTask(new GroupEndTask(type, "",info));
    }

    /**
     * 创建手动开启分组轮灌
     * 1. 查看是否有需要关闭的组
     * 2. 修改当前组的状态为开启
     * 3. 获取所有组的设备
     * 4. 添加开启taks
     * 5. 添加读取taks
     * 6. 添加开启结束标志位
     * 7. 修改需要关闭组的状态为关闭
     * 8. 添加关闭task
     * 9. 添加读取task
     * 10. 添加关闭结束标志位
     */

    public static void createGroupOpenTask(GroupInfo groupInfo) {
        // 1. 查看是否有需要关闭的组
//        List<GroupInfo> groupInfos = GroupInfoSql.queryOpenGroupList();
//        GroupInfo closeGroupInfo = null;
//        int closeGroupId = -1;
//        if (groupInfos != null) {
//            if (groupInfos.size() > 1) {
//                LogUtils.e(TAG, "当前有多组处于运行状态 异常");
//                return;
//            }
//            if (groupInfos.size() == 1) {
//                closeGroupInfo = groupInfos.get(0);
//                closeGroupId = closeGroupInfo.getGroupId();
//            }
//        }
        LogUtils.e(TAG, "********************单组操作开始****************");
        //  2. 更新当前组的状态
            groupInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
            groupInfo.setGroupStop(0);
            GroupInfoSql.updateGroup(groupInfo);
        //  3. 获取所有组的设备
        ArrayList<ControlInfo> openList = new ArrayList<>();
        ArrayList<ControlInfo> closeList = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        int openGroupId = groupInfo.getGroupId();
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo0 = deveiceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo1 = deveiceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (controlInfo0.getValveGroupId() == openGroupId) {
                openList.add(controlInfo0);
            }
            if (controlInfo1.getValveGroupId() == openGroupId) {
                openList.add(controlInfo1);
            }

//            if (closeGroupId > 0) {
//                if (controlInfo0.getValveGroupId() == closeGroupId) {
//                    closeList.add(controlInfo0);
//                }
//                if (controlInfo1.getValveGroupId() == closeGroupId) {
//                    closeList.add(controlInfo1);
//                }
//            }
        }

        if (openList.size() == 0) {
            LogUtils.e(TAG, "当前分组没有设备");
            return;
        }
        // 4. 添加开启task
        addOpenGroupTask(openList, true);
        // 5. 添加开启状态查询task
        addReadGroupTask(openList, TaskEntiy.TASK_OPTION_OPEN_READ,Entiy.ACTION_TYPE_31);
        // 6. 添加开启结束标志位
        createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_OPEN_READ_END,groupInfo);

//        if (closeGroupId > 0 && closeList.size() > 0) {
//            // 7 修改需要关闭组的状态为关闭
//            closeGroupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
//            closeGroupInfo.setGroupStop(0);
//            LogUtils.e(TAG, "********************单组操作有需要关闭的组****************");
//            // 8 添加关闭其他组task
//            addOpenGroupTask(closeList, false);
//            // 9 添加关闭状态查询task
//            addReadGroupTask(closeList, TaskEntiy.TASK_OPTION_CLOSE_READ,Entiy.ACTION_TYPE_31);
//            // 10. 添加关闭结束标志位
//            createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END,closeGroupInfo);
//        }
    }

    /**
     * 添加开启的设备task
     *
     * @param controlInfos
     */
    private static void addOpenGroupTask(List<ControlInfo> controlInfos, boolean isOpen) {
        int size = controlInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo info = controlInfos.get(i);
            if (isOpen) {
                createOpenTask(info);
            } else {
                createCloseTask(info);
            }
        }
    }

    /**
     * 添加读取状态task
     *
     * @param controlInfos
     */
    private static void addReadGroupTask(List<ControlInfo> controlInfos, int type, int actionType) {
        int size = controlInfos.size();
        for (int i = 0; i < size; i++) {
            createReadTask(controlInfos.get(i), type,actionType);
        }
    }

    /**
     * 手动单组操作  关闭
     * . 1.添加关闭task
     * 2.添加读取task
     * 3.添加关闭标志位
     *
     * @param groupInfo
     */
    public static void createGroupCloseTask(GroupInfo groupInfo) {
        ArrayList<ControlInfo> closeList = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        int goupId = groupInfo.getGroupId();
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo0 = deveiceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo1 = deveiceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (controlInfo0.getValveGroupId() == goupId) {
                closeList.add(controlInfo0);
            }
            if (controlInfo1.getValveGroupId() == goupId) {
                closeList.add(controlInfo1);
            }
        }
        addOpenGroupTask(closeList, false);
        // 1 添加关闭状态查询task
        addReadGroupTask(closeList, TaskEntiy.TASK_OPTION_CLOSE_READ,Entiy.ACTION_TYPE_31);
        // 4. 添加关闭结束标志位
        createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END,groupInfo);
    }


    /************************************************************************************************
     *          自动轮灌
     ************************************************************************************************
     *  /
     /**
     *                 自动轮灌完成标志位
     * @param type      类型
     */
    public static void createGroupAutoEndTask(int type, GroupInfo groupInfo) {
        TaskManager.getInstance().addTask(new GroupAutoEndTask(type,groupInfo));
    }

    /**
     *    自动轮灌开启
     * 1. 更新当前组的状态为运行
     * 2. 获取所有组的设备
     * 3. 添加开启task
     * 4. 添加开启状态查询task
     * 5. 添加开启结束标志位 或者执行下一组标志位
     */
    public static void createAutoGroupOpenTask(GroupInfo groupInfo) {
        LogUtils.e(TAG, "**********************************自动轮灌开启****************************");
        //  1. 更新当前组的状态为运行
        groupInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
        groupInfo.setGroupEndTime(System.currentTimeMillis());
        LogUtils.e("轮灌开始时间  自动开始  ===========", DateUtils.stampToDate(groupInfo.getGroupEndTime()));
        groupInfo.setGroupStop(0);
        GroupInfoSql.updateGroup(groupInfo);
        EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_START,groupInfo));
//        EventBus.getDefault().post(new GroupStatusEvent(groupInfo));

        //  2. 获取所有组的设备
        ArrayList<ControlInfo> openList = getControlInfo(groupInfo);
        if (openList.size() == 0) {
            LogUtils.e(TAG, "当前分组没有设备");
            return;
        }
        // 3. 添加开启task
        addOpenGroupTask(openList, true);
        // 4. 添加开启状态查询task
        addReadGroupTask(openList, TaskEntiy.TASK_OPTION_OPEN_READ,Entiy.ACTION_TYPE_32);
        // 5. 添加开启结束标志位
        createGroupAutoEndTask(TaskEntiy.TASK_OPTION_AUTO_OPEN,groupInfo);
    }


    /**
     *    自动轮灌开启
     * 1. 更新当前组的状态为运行
     * 2. 获取所有组的设备
     * 3. 添加开启task
     * 4. 添加开启状态查询task
     * 5. 添加执行下一组标志位
     */
    public static void createAutoGroupOpenNextTask(GroupInfo curInfo) {
        LogUtils.e(TAG, "**********************************自动轮灌开启****************************");
        //  1. 更新当前组的状态为运行
        curInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
        curInfo.setGroupEndTime(System.currentTimeMillis());
        LogUtils.e("轮灌开始时间  自动开始  ", DateUtils.stampToDate(curInfo.getGroupEndTime()));
        curInfo.setGroupStop(0);
        GroupInfoSql.updateGroup(curInfo);

        EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_NEXT));

        EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_START,curInfo));
        MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_STATUS);
        //  2. 获取所有组的设备
        ArrayList<ControlInfo> openList = getControlInfo(curInfo);
        if (openList.size() == 0) {
            LogUtils.e(TAG, "当前分组没有设备");
            return;
        }
        // 3. 添加开启task
        addOpenGroupTask(openList, true);
        // 4. 添加开启状态查询task
        addReadGroupTask(openList, TaskEntiy.TASK_OPTION_OPEN_READ,Entiy.ACTION_TYPE_32);
        // 5. 添加开启结束标志位
        createGroupAutoEndTask(TaskEntiy.TASK_OPTION_AUTO_NEXT, curInfo);
    }
    /**
     *    自动轮灌开启
     * 1. 更新当前组的状态为关闭
     * 2. 获取所有组的设备
     * 3. 添加关闭task
     * 4. 添加关闭状态查询task
     * 5. 添加关闭结束标志位
     */
    public static void createAutoGroupCloseTask(GroupInfo groupInfo) {
        LogUtils.e(TAG, "*********************************自动轮灌关闭当前组*****************************");
        //  1. 更新当前组的状态为运行
        groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
        groupInfo.setGroupRunTime(0);
        groupInfo.setGroupTime(0);
        groupInfo.setGroupStop(0);
        GroupInfoSql.updateGroup(groupInfo);

//        EventBus.getDefault().post(new AutoTaskEvent(groupInfo));
//        EventBus.getDefault().post(new GroupStatusEvent(groupInfo));

        MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_STATUS);
        //  2. 获取组的设备信息
        ArrayList<ControlInfo> closeList  = getControlInfo(groupInfo);
        if (closeList.size() == 0) {
            LogUtils.e(TAG, "当前分组没有设备     createAutoGroupCloseTask()");
            return;
        }
        // 3 添加关闭task
        addOpenGroupTask(closeList, false);
        // 3 添加关闭状态查询task
        addReadGroupTask(closeList, TaskEntiy.TASK_OPTION_CLOSE_READ,Entiy.ACTION_TYPE_32);
        // 4. 添加关闭结束标志位
        createGroupAutoEndTask(TaskEntiy.TASK_OPTION_AUTO_CLOSE,groupInfo);
    }

    /**
     * 自动轮灌开启下一组
     * 1. 关闭当前组的运行状态
     * 2. 判断是否有下一组    如果有                      如果没有
     * 1. 添加下一组开启任务                             1. 关闭当前组
     * 2. 查询下一组开启状态任务                         2. 查询当前组的关闭状态
     * 3. 添加开启成功标志                               3. 添加关闭完成标志
     * 4. 关闭当前组
     * 5. 查询当前组的关闭状态
     * 6. 添加关闭完成标志
     */
    public static void createAutoGroupNextTask(GroupInfo groupInfo) {
        LogUtils.e(TAG, "*********************************自动轮灌 是否有下一组需要操作*****************************");
        //查看是否有下一组
        List<GroupInfo> groupList = GroupInfoSql.queryNextGroupList(groupInfo.getGroupId());
        groupInfo.setGroupTime(0);
        groupInfo.setGroupRunTime(0);
        groupInfo.setGroupEndTime(0);
        groupInfo.setGroupStop(0);
        GroupInfoSql.updateGroup(groupInfo);
        if (groupList != null) {
            groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            GroupInfoSql.updateGroup(groupInfo);
            createAutoGroupOpenNextTask(groupList.get(0));
            createAutoGroupCloseTask(groupInfo);
            TaskManager.getInstance().startTask();
        } else {
            LogUtils.e(TAG, "*********************************自动轮灌完成 停止计时*****************************");
            LogUtils.e("轮灌开始时间  结束时间  ===========", DateUtils.stampToDate(System.currentTimeMillis()));
            EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_FINISH));
            MessageSend.syncAuto(MessageEntiy.TYPE_AUTO_STATUS);
        }
    }

    /**
     *        根据组信息 获取设备信息
     * @param groupInfo
     * @return
     */
    public static ArrayList<ControlInfo> getControlInfo(GroupInfo groupInfo) {
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        ArrayList<ControlInfo> list = new ArrayList<>();
        int openGroupId = groupInfo.getGroupId();
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo0 = deveiceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo1 = deveiceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (controlInfo0.getValveGroupId() == openGroupId) {
                list.add(controlInfo0);
            }
            if (controlInfo1.getValveGroupId() == openGroupId) {
                list.add(controlInfo1);
            }
        }
        return list;
    }

    /**
     *
     * @return
     */
    public static String getProjectId() {
        String id = BaseApp.getProjectId();
        if (TextUtils.isEmpty(id)) {
            User user = UserSql.queryUserInfo();

            LogUtils.e(TAG, "----------用户项目ID为空------------"+new Gson().toJson(user));
            if (user == null) {
                return "";
            }
            id = user.getProjectGroupId();
        }
        if (TextUtils.isEmpty(id)) {
            LogUtils.e(TAG, "----------用户项目ID为空------------");
        }
        return id;
    }
}
