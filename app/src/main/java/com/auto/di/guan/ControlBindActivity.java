package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.dialog.LoadingDialog;
import com.auto.di.guan.dialog.MainShowDialogBg;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.event.BindIdEvent;
import com.auto.di.guan.event.BindSucessEvent;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class ControlBindActivity extends Activity implements View.OnClickListener, IUrlRequestCallBack {
    private View view;
    private TextView textView;
    private DeviceInfo info;
    private EditText bind_device_name;
    private View bind_deivce_item;
    private View bind_group_item;
    /**
     *   阀门1
     */
    private View bind_control_id_1;
    private View bind_control_alias_1;
    private TextView bind_control_name_1;
    private EditText bind_control_nick_1;
    private CheckBox bind_control_sel_1;

    private View bind_control_id_2;
    private View bind_control_alias_2;
    private EditText bind_control_nick_2;
    private TextView bind_control_name_2;
    private CheckBox bind_control_sel_2;

    private Button bind_deivce_id;
    private Button bind_deivce_group_id;
    private Button bind_cantrol_save;

    private List<ControlInfo> controlInfos;

    private LoadingDialog mLoadingDailog;

    /***是否写入项目ID**/
    private boolean isPeroJectId;
    /***是否写入组ID**/
    private boolean isGroupId;
    private boolean isGroupClick;


    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_bind);
        isPeroJectId = false;
        isGroupId = false;
        init();
        setListener();
    }

    protected void init() {
        EventBus.getDefault().register(this);
        controlInfos = ControlInfoSql.queryControlList();
        mUser = BaseApp.getUser();
        view = findViewById(R.id.title_bar);
        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("绑定阀门");
        view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BindIdEvent());
                finish();
            }
        });
        info = (DeviceInfo) getIntent().getSerializableExtra("info");

        bind_device_name = (EditText) findViewById(R.id.bind_device_name);
        bind_deivce_item = findViewById(R.id.bind_deivce_item);
        ((TextView) (bind_deivce_item.findViewById(R.id.item_title))).setText("项目ID");
        ((TextView) (bind_deivce_item.findViewById(R.id.item_desc))).setText(BaseApp.getProjectId() + "");
        bind_group_item = findViewById(R.id.bind_group_item);
        ((TextView) (bind_group_item.findViewById(R.id.item_title))).setText("阀控器ID");
        ((TextView) (bind_group_item.findViewById(R.id.item_desc))).setText(info.getDeviceSort() + "");
        bind_device_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    bind_control_name_1.setText(s + "_1");
                    bind_control_name_2.setText(s + "_2");
                }else {
                    bind_control_name_1.setText("");
                    bind_control_name_2.setText("");
                }
            }
        });

        // 阀门1
        ((TextView) findViewById(R.id.bind_control_num_1)).setText("1");
        bind_control_sel_1 = (CheckBox) findViewById(R.id.bind_control_sel_1);
        // 阀门编号
        bind_control_id_1 = findViewById(R.id.bind_control_id_1);
        ((TextView) (bind_control_id_1.findViewById(R.id.item_title))).setText("阀门编号");
        bind_control_name_1 = ((TextView) bind_control_id_1.findViewById(R.id.item_desc));
        // 阀门别名
        bind_control_alias_1 = findViewById(R.id.bind_control_alias_1);
        ((TextView) (bind_control_alias_1.findViewById(R.id.item_title))).setText("阀门别名");
        bind_control_nick_1 = bind_control_alias_1.findViewById(R.id.item_desc);
        // 阀门1
        ((TextView)findViewById(R.id.bind_control_num_2)).setText("2");
        bind_control_sel_2 = (CheckBox) findViewById(R.id.bind_control_sel_2);
        // 阀门编号
        bind_control_id_2 = findViewById(R.id.bind_control_id_2);
        ((TextView) (bind_control_id_2.findViewById(R.id.item_title))).setText("阀门编号");
        bind_control_name_2 = ((TextView) bind_control_id_2.findViewById(R.id.item_desc));
        // 阀门别名
        bind_control_alias_2 = findViewById(R.id.bind_control_alias_2);
        ((TextView) (bind_control_alias_2.findViewById(R.id.item_title))).setText("阀门别名");
        bind_control_nick_2 = bind_control_alias_2.findViewById(R.id.item_desc);

        bind_cantrol_save = (Button) findViewById(R.id.bind_cantrol_save);
        bind_deivce_id = (Button) findViewById(R.id.bind_deivce_id);
        bind_deivce_group_id = (Button) findViewById(R.id.bind_deivce_control_id);
    }


    protected void setListener() {
        bind_cantrol_save.setOnClickListener(this);
        bind_deivce_id.setOnClickListener(this);
        bind_deivce_group_id.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_cantrol_save:
                String controlName1 = bind_control_name_1.getText().toString().trim();
                String controlName2 = bind_control_name_2.getText().toString().trim();
                String deviceName = bind_device_name.getText().toString().trim();

                String nick1 = bind_control_nick_1.getText().toString().trim();
                String nick2 = bind_control_nick_2.getText().toString().trim();

                if (!isPeroJectId) {
                    showToastLongMsg("项目ID未写入");
                    return;
                }
                if (!isGroupId) {
                    showToastLongMsg("组ID未写入");
                    return;
                }

                if(!bind_control_sel_1.isChecked() && !bind_control_sel_1.isChecked()) {
                    showToastLongMsg("没有选中阀门");
                    return;
                }

                if (TextUtils.isEmpty(deviceName)) {
                    showToastLongMsg("请输入设备的名称");
                    return;
                }


                if (TextUtils.isEmpty(nick1) && bind_control_sel_1.isChecked()) {
                    showToastLongMsg("请输入控制阀 1 的别名");
                    return;
                }
                if (TextUtils.isEmpty(nick2) && bind_control_sel_2.isChecked()) {
                    showToastLongMsg("请输入控制阀 2 的别名");
                    return;
                }
                info.setDeviceName(deviceName);
                info.setDeviceStatus(1);

                ControlInfo controlInfo_0 = info.getValveDeviceSwitchList().get(0);
                ControlInfo controlInfo_1 = info.getValveDeviceSwitchList().get(1);
                if (bind_control_sel_1.isChecked()) {
                    controlInfo_0.setValveStatus(Entiy.CONTROL_STATUS＿CONNECT);
                    controlInfo_0.setDeviceId(info.getDeviceId());
                    controlInfo_0.setValveName(controlName1);
                    controlInfo_0.setValveAlias(nick1);
                    controlInfo_0.setValveId(info.getDeviceSort() * 2 - 1);
                    controlInfo_0.setProtocalId("0");
                    controlInfo_0.setDeviceProtocalId(info.getProtocalId());

                } else {
                    controlInfo_0.setValveStatus(0);
                    controlInfo_0.setValveId(0);
                }
                if (bind_control_sel_2.isChecked()) {
                    controlInfo_1.setValveStatus(Entiy.CONTROL_STATUS＿CONNECT);
                    controlInfo_1.setDeviceId(info.getDeviceId());
                    controlInfo_1.setValveName(controlName2);
                    controlInfo_1.setValveAlias(nick2);
                    controlInfo_1.setValveId(info.getDeviceId() * 2);
                    controlInfo_1.setDeviceProtocalId(info.getProtocalId());
                    controlInfo_1.setProtocalId("1");
                } else {
                    controlInfo_1.setValveStatus(0);
                    controlInfo_1.setValveId(0);
                }
                DeviceInfoSql.updateDevice(info);
                EventBus.getDefault().post(new BindIdEvent());
                finish();
                break;
            case R.id.bind_deivce_id:
//                if (!TaskManager.getInstance().hasTask()) {
//                    TaskManager.getInstance().clearTask();
//                    return;
//                }
                isGroupClick = true;
                showWaitingDialog();
                TaskFactory.createGidTak();
                break;
            case R.id.bind_deivce_control_id:
//                if (!TaskManager.getInstance().hasTask()) {
//                    TaskManager.getInstance().clearTask();
//                    return;
//                }
                showWaitingDialog();
                isGroupClick = false;
                TaskFactory.createBidTak(info.getProtocalId());
                break;
        }
    }

    /**
     * 显示字符串类型数据
     *
     * @param msg
     */
    public void showToastLongMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void urlRequestStart(Task result) {

    }

    @Override
    public void urlRequestEnd(Task result) {

    }

    @Override
    public void urlRequestException(Task result) {

    }



    /**
     *       设置项目GID正常
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindSucessEvent(BindSucessEvent event) {
        hideWaitingDialog();

        if (event == null) {
            return;
        }

        if (!event.isOk()) {
            Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtils.e("BaseTask == ", "onBindSucessEvent"+event.getType() );
        boolean is = false;
        if (event.getType() == TaskEntiy.TASK_READ_GID) {
            is = true;
        }
        LogUtils.e("BaseTask == ", "is = "+is);
        if (event.getType() == TaskEntiy.TASK_READ_GID) {
            isPeroJectId = true;
//            LogUtils.e("bind","TASK_READ_GID  event.getType()="+event.getType());
//            Toast.makeText(ControlBindActivity.this, "写入项目ID成功", Toast.LENGTH_SHORT).show();
//            ToastUtils.showToast("111111111111111111111111111111111");
        }else if (event.getType() == TaskEntiy.TASK_READ_BID) {
            isGroupId = true;
//            LogUtils.e("bind"," TASK_READ_BID  event.getType()="+event.getType());
//            Toast.makeText(ControlBindActivity.this, "写入组ID成功", Toast.LENGTH_SHORT).show();
//            ToastUtils.showToast("22222222222222222222222222222222222222222");
        }
//        LogUtils.e("ControlBindActivity ", "绑定成功");
//        LogUtils.e("bind","event.getResult()="+event.getResult());
//        Toast.makeText(this, ""+event.getResult(), Toast.LENGTH_SHORT).show();
//        ToastUtils.showToast(""+event.getResult());
//
        MainShowDialogBg.ShowDialog(ControlBindActivity.this, "提示", event.getResult() + "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }




    public void showWaitingDialog() {
        if (null == mLoadingDailog) {
            mLoadingDailog = new LoadingDialog(this, R.style.CustomDialog);
        }
        if (!mLoadingDailog.isShowing()) {
            mLoadingDailog.show();
        }
    }

    /**
     * 隐藏等待提示框
     */
    public void hideWaitingDialog() {
        if (mLoadingDailog != null && mLoadingDailog.isShowing()) {
            mLoadingDailog.dismiss();
            mLoadingDailog = null;
        }
    }
}
