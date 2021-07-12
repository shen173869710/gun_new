package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.event.BindSucessEvent;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 *   读取设备的任务
 */
public class ReadIdTask extends BaseTask{
    private final String TAG = BASETAG+"ReadTask";

    public ReadIdTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "读取 id 开始=======  cmd =="+getTaskCmd());
        writeCmd(getTaskCmd());
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "读取 id 错误 ======="+"errorTask()");
        EventBus.getDefault().post(new BindSucessEvent(false,getTaskType()));
        finishTask();
    }

    @Override
    public void endTask(String receive) {
        LogUtils.e(TAG, "读取 id 结束 ======="+"endTask()"+"====收到信息 =="+receive);
        if (receive.toLowerCase().contains("gid")) {
            LogUtils.e(TAG, "读取 gid 正常 =======");
            EventBus.getDefault().post(new BindSucessEvent(true,getTaskType(), "写入项目ID成功"));
            finishTask();
        }else if (receive.toLowerCase().contains("bid")) {
            LogUtils.e(TAG, "读取 bid 正常 =======");
            EventBus.getDefault().post(new BindSucessEvent(true,getTaskType(),"写入阀控器ID成功"));
            finishTask();
        }else {
            retryTask();
        }
    }

    @Override
    public void retryTask() {
        if (getTaskCount() == 2) {
            setTaskCount(1);
            LogUtils.e(TAG, "读取id 重试======="+"retryTask()  cmd =="+getTaskCmd());
            writeCmd( getTaskCmd());
        }else {
            errorTask();
        }
    }

}
