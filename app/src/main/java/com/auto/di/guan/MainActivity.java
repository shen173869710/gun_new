package com.auto.di.guan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.auto.di.guan.api.ApiUtil;
import com.auto.di.guan.api.GlobalConstant;
import com.auto.di.guan.api.HttpManager;
import com.auto.di.guan.basemodel.model.request.BaseRequest;
import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.UserActionSql;
import com.auto.di.guan.dialog.InputPasswordDialog;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.PollingEvent;
import com.auto.di.guan.entity.SyncData;
import com.auto.di.guan.event.ActivityEvent;
import com.auto.di.guan.event.AutoCountEvent;
import com.auto.di.guan.event.AutoTaskEvent;
import com.auto.di.guan.event.DelayEvent;
import com.auto.di.guan.event.GroupStatusEvent;
import com.auto.di.guan.event.LoginEvent;
import com.auto.di.guan.event.SendCmdEvent;
import com.auto.di.guan.event.UserStatusEvent;
import com.auto.di.guan.event.VideoPlayEcent;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.rtm.ChatManager;
import com.auto.di.guan.rtm.MessageEntiy;
import com.auto.di.guan.utils.FloatStatusUtil;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends SerialPortActivity {
    private final String TAG = "------" + MainActivity.class.getSimpleName();
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView textView;
    private static final int HANDLER_WHAT_FALG = 1;
    private MediaPlayer mp;
    /**
     * 当前运行的剩余时间
     ***/
    public int curRunTime = 0;

    private ChatManager chatManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupInfo groupInfo = null;
            if (msg.obj != null) {
                groupInfo = (GroupInfo) msg.obj;
                groupInfo.setGroupRunTime(groupInfo.getGroupRunTime() + 1);
                curRunTime = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
                /**如果运行时间到呢,**/
                if (groupInfo.getGroupTime() <= groupInfo.getGroupRunTime()) {
                    /**
                     *   如果运行时间到呢, 就执行下一组
                     */
                    TaskFactory.createAutoGroupNextTask(groupInfo);
                    EventBus.getDefault().post(new AutoCountEvent(groupInfo));
                } else {
                    Message message = new Message();
                    message.obj = groupInfo;
                    message.what = HANDLER_WHAT_FALG;
                    sendMessageDelayed(message, Entiy.RUN_TIME_COUNT);
                    /**
                     *   每隔 1秒保存一次数据
                     */
                    GroupInfoSql.updateRunGroup(groupInfo);
                    EventBus.getDefault().post(new AutoCountEvent(groupInfo));

                    if(groupInfo.getGroupRunTime() % Entiy.ALERM_TIME == 0 && PollingUtils.isIsStart()) {
                        onPollingEvent(new PollingEvent());
                    }
                }
            }
        }
    };

    public static int windowTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.e("time", "time == "+System.currentTimeMillis());
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        textView = (TextView) findViewById(R.id.title_bar_title);
        textView.setText(BaseApp.getUser().getProjectName());
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        final ArticleListFragment articleListFragment = new ArticleListFragment();
        transaction.add(R.id.center, articleListFragment, "center");
        transaction.commitAllowingStateLoss();
        windowTop = getStatusBarHeight();

        chatManager = BaseApp.getInstance().getChatManager();
        chatManager.init();
        chatManager.doLogin();

        syncData();


        EventBus.getDefault().post(new SendCmdEvent("rs 99999 999"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new SendCmdEvent("rs 99999 999"));
            }
        },4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new SendCmdEvent("rs 99999 999"));
            }
        },4000);

        FloatStatusUtil.getInstance().show();

        //查询有没有自动轮毂
        List<GroupInfo> list = GroupInfoSql.queryOpenGroupList();
        if (list != null && list.size() > 0) {

            GroupInfo groupInfo = list.get(0);
            int runTime = groupInfo.getGroupRunTime();
            int allTime = groupInfo.getGroupTime();
            if (runTime != 0 && runTime < allTime) {
                // 开启自动管灌盖
                EventBus.getDefault().post(new AutoTaskEvent(Entiy.RUN_DO_START, groupInfo));
                LogUtils.e(TAG, "异常退出开启自动轮灌");
            }
        }
    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
    }

    public void setTitle(String title) {
        String mainTitle = BaseApp.getUser().getProjectName();
        if (mainTitle == null && TextUtils.isEmpty(mainTitle)) {
            mainTitle = "";
        } else {
            mainTitle = mainTitle + "-";
        }
        title = mainTitle + title;
        textView.setText(title);
    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
       final String receive = new String(buffer, 0, buffer.length);
        int length = receive.trim().length();
        LogUtils.e(TAG, "收到 -------------------" + receive + "    length = " + length);

        if (TextUtils.isEmpty(receive)) {
            ToastUtils.showLongToast("错误命令" + receive);
            return;
        }
        if ((receive.contains("reg")
                || receive.contains("standby_flag")
                || receive.contains("Adc"))
                || receive.contains("asuring")) {
            LogUtils.i(TAG, "过滤乱码信息 ============================="+receive);
            return;
        }

        String cmd = "";
        if (TaskManager.getInstance().getmTask() != null) {
            cmd = TaskManager.getInstance().getmTask().getTaskCmd();
        }
        if (receive.trim().equals(cmd)) {
            LogUtils.e(TAG, "过滤回显信息 =============================");
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TaskManager.getInstance().endTask(receive);
            }
        });
    }

    public void showDialog() {
        FloatWindowUtil.getInstance().show();
    }

    private void play() {

        if (Entiy.isPlay) {
            try {
                mp = MediaPlayer.create(MainActivity.this, R.raw.alert);
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Entiy.isSendCode) {
                sendMessage();
        }

    }


    private void dologOut() {
        LogUtils.e(TAG, "dologOut"+chatManager);
        if (chatManager != null) {
            chatManager.doLogout();
        }
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeMessages(HANDLER_WHAT_FALG);
            handler = null;
        }
        PollingUtils.stopPollingService(this);
        FloatWindowUtil.getInstance().distory();


        InputPasswordDialog.dismiss(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy"+chatManager);
        if (chatManager != null) {
            chatManager.doLogout();
        }
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeMessages(HANDLER_WHAT_FALG);
            handler = null;
        }
        PollingUtils.stopPollingService(this);
        FloatWindowUtil.getInstance().distory();


        InputPasswordDialog.dismiss(this);

    }

    /**
     *        单个设备的变化
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatsuEvent(CmdStatus event) {
        FloatWindowUtil.getInstance().onStatsuEvent(event);
        /**
         *  悬浮球单个设备状态变化
         */
        if (TextUtils.isEmpty(event.getCmd_read_end())) {
            FloatWindowUtil.getInstance().onStatsuEvent(event);
        }
    }

    public static boolean isOpen = false;
    /**
     *        接收taks 发送过来的命令 写入
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendCmdEvent(SendCmdEvent event) {
        if (event == null) {
            return;
        }
        String cmd = event.getCmd();
        if (TextUtils.isEmpty(cmd)) {
            ToastUtils.showLongToast("无效的命令格式");
            return;
        }

        if (!cmd.contains("bid") &&
                !cmd.contains("gid") &&
                !cmd.contains("99999")) {
            showDialog();
        }
        LogUtils.e(TAG, "-----写入命令" + event.getCmd());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
        try {
            if (mOutputStream != null) {
                mOutputStream.write(new String(event.getCmd()).getBytes());
                mOutputStream.write("\n".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // kf 012 004 0 ok
        // gf 012 004 0 ok
        //zt 012 004 xxxx
        // zt 102 002 1100 090
//        String buf= "";
//        if (event.getCmd().contains("kf")) {
//           buf = "kf 10000 001 0 ok";
//            isOpen = true;
//        }else if (event.getCmd().contains("gf")) {
//            buf = "gf 10000 001 0 ok";
//            isOpen = false;
//        }else if (event.getCmd().contains("rs")) {
//            if (isOpen) {
//                buf = "zt 10000 001 1110 010";
//            }else {
//                buf = "zt 10000 001 1100 010";
//            }
//        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        onDataReceived(buf.getBytes(), 22);
    }
    /**
     * 异常报警
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoPlayEvent(VideoPlayEcent event) {
        play();
    }

    /**
     *   接收自动轮灌相关操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoTaskEvent(AutoTaskEvent event) {
        if(handler != null && event != null) {
            /**
             *  轮灌完成
             */
            if(event.getType() == Entiy.RUN_DO_FINISH) {
                handler.removeMessages(HANDLER_WHAT_FALG);
                LogUtils.e(TAG, "---------自动轮灌结束--------- ");
            }  else if (event.getType() == Entiy.RUN_DO_STOP) {
                /**
                 *  停止计时
                 */
                handler.removeMessages(HANDLER_WHAT_FALG);
                LogUtils.e(TAG, "---------暂停轮灌计时--------- ");
            }else if (event.getType() == Entiy.RUN_DO_START){
                LogUtils.e(TAG, "---------开启轮灌计时--------- ");
                // 轮灌开始
                handler.removeMessages(HANDLER_WHAT_FALG);
                Message message = new Message();
                message.obj = event.getGroupInfo();
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
            }else {
                handler.removeMessages(HANDLER_WHAT_FALG);
                Message message = new Message();
                message.obj = event.getGroupInfo();
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
                LogUtils.e(TAG, "----当前运行的groupinfo "+(new Gson().toJson(event.getGroupInfo())));
            }
        }
    }

    /**
     *  自动轮灌查询功能
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPollingEvent(PollingEvent event) {
        LogUtils.e(TAG,"--------------------------------自动轮灌查询执行-----------------------------------");

        if (!TaskManager.getInstance().hasTask()) {
            LogUtils.e(TAG,"--------------------------------自动轮灌查询执行, 有任务正在执行跳过-----------------------------------");
            return;
        }
        List<GroupInfo> groupInfos  = GroupInfoSql.queryOpenGroupList();
        if (groupInfos != null && groupInfos.size() == 1) {
            GroupInfo groupInfo  = groupInfos.get(0);
            int time = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
            if (time > Entiy.ALERM_TIME) {
                TaskFactory.createPullTask(groupInfo);
            }else {
                PollingUtils.stopPollingService(MainActivity.this);
            }
        }
    }

    private long firstTime=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                ToastUtils.showToast("再按一次退出");
                firstTime=System.currentTimeMillis();
            }else{
                dologOut();
                finish();

                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserStatusEvent(UserStatusEvent event) {
        if (event == null || TextUtils.isEmpty(event.getPeerId())) {
            return;
        }
        if (event.getPeerId().equals(BaseApp.getUser().getMemberId().toString())) {
            if (event.getStatus() == 0) {
                LogUtils.e(TAG, "管理员在线");
                BaseApp.setWebLogin(true);
            }else {
                LogUtils.e(TAG, "管理员离线");
                BaseApp.setWebLogin(false);
                InputPasswordDialog.dismiss(MainActivity.this);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
       if (event != null && event.isLogin()) {
           BaseApp.setWebLogin(true);
           InputPasswordDialog.show(MainActivity.this);
       }else {
           BaseApp.setWebLogin(false);
           InputPasswordDialog.dismiss(MainActivity.this);
       }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityEvent(ActivityEvent event) {
        if (event.getIndex() == MessageEntiy.TYPE_ACTIVITY_STATUS_START) {
            startActivity(new Intent(MainActivity.this, GroupStatusActivity.class));
        }
    }

    /**
     *  数据同步
     */
    public void syncData() {
//
//        SyncData data = new SyncData();
//        data.setDevices(DeviceInfoSql.queryDeviceList());
//        data.setActions(UserActionSql.queryUserActionlList());
//        data.setGroups(GroupInfoSql.queryGroupList());
//
//        HttpManager.syncData(ApiUtil.createApiService().sync(data), new HttpManager.OnResultListener() {
//            @Override
//            public void onSuccess(BaseRespone t) {
//                LogUtils.e(TAG, "同步数据成功");
//            }
//
//            @Override
//            public void onError(Throwable error, Integer code, String msg) {
//                LogUtils.e(TAG, "同步数据失败");
//            }
//        });
    }

    /**
     *  自动轮灌设备更新
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupStatusEvent(GroupStatusEvent event) {
        LogUtils.i("GroupStatusActivity",  "更新设备-----------------------------\n"+(new Gson().toJson(event)));
        if (event != null && event.getGroupInfo() != null) {
           FloatStatusUtil.getInstance().onGroupStatusEvent(event.getGroupInfo());
        }
    }

    /**
     *  自动轮灌设记时
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoCountEvent(AutoCountEvent event) {
        LogUtils.i("GroupStatusActivity",  "更新设备-----------------------------\n"+(new Gson().toJson(event)));
        if (event != null && event.getGroupInfo() != null) {
            FloatStatusUtil.getInstance().onAutoCountEvent(event.getGroupInfo());
        }
    }


    /**
     *  自动轮灌设记时
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDelayEvent(DelayEvent event) {
        LogUtils.e("mainActivity",  "延时任务启动----------------------------");
        TaskManager.getInstance().doNextTask();
    }


    private void sendMessage() {
            TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("mobile", Entiy.PHONE);
        treeMap.put("content","1111111111");

    HttpManager.newApi(ApiUtil.createApiService().sendSmsMsg(BaseRequest.toMerchantTreeMap(treeMap)), new HttpManager.OnResultListener() {
        @Override
        public void onSuccess(BaseRespone respone) {

        }
        @Override
        public void onError(Throwable error, Integer code,String msg) {

        }
    });
    }


}
