package com.auto.di.guan.jobqueue.task;

import android.os.Handler;
import android.os.Looper;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.DelayEvent;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.event.SendCmdEvent;
import com.auto.di.guan.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;

public abstract class BaseTask {

    public final String BASETAG = "BaseTask == ";
    public final int RETRY_COUNT = 2;
    private int taskCount = RETRY_COUNT;
    private int taskType;
    private String taskCmd;
    private ControlInfo taskInfo;
    /*每个任务默认存活的时间*/
    private int taskLife;
    /**
     *   接收的数据
     */
    private String receive;

    public BaseTask(int taskType, String taskCmd) {
        this.taskType = taskType;
        this.taskCmd = taskCmd;
        setTaskCount(RETRY_COUNT);
    }

    public BaseTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        this.taskType = taskType;
        this.taskCmd = taskCmd;
        this.taskInfo = taskInfo;
        setTaskCount(RETRY_COUNT);
    }

    /**
     *   开始执行命令
     */
    public abstract  void startTask();
    /**
     *   命令异常
     */
    public abstract void errorTask();
    /**
     *   任务执行结束
     */
    public abstract void endTask(String receive);
    /**
     *   重试执行任务
     */
    public abstract void retryTask();

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public ControlInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(ControlInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void writeCmd(String cmd) {
        EventBus.getDefault().post(new SendCmdEvent(cmd));
}

    public int getRETRY_COUNT() {
        return RETRY_COUNT;
    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    /***
     *   任务完成
     */
    public void finishTask() {
        TaskManager.getInstance().finishTask();
        LogUtils.e("BaseTask", "############################################完成一次操作#############################################");
//        try {
//            Thread.sleep(Entiy.CMD_TIME);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LogUtils.e("BaseTask", "#############延时任务################");
//                Looper.prepare();
//                TaskManager.getInstance().doNextTask();
//                Looper.loop();
//            }
//        },Entiy.CMD_TIME);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Entiy.CMD_TIME);
                    EventBus.getDefault().post(new DelayEvent());
                    LogUtils.e("BaseTask", "#############延时任务################");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public int getTaskLife() {
        return taskLife;
    }

    public void setTaskLife(int taskLife) {
        this.taskLife = taskLife;
    }
}
