package com.auto.di.guan.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.dialog.MainoptionDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.GlideUtil;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyGridOpenAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder> {
    public MyGridOpenAdapter(List<DeviceInfo> data) {
        super(R.layout.grid_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DeviceInfo deviceInfo) {
        TextView grid_item_device_id = holder.findView(R.id.grid_item_device_id);
        grid_item_device_id.setText(deviceInfo.getDeviceSort()+"");
        grid_item_device_id.setVisibility(View.VISIBLE);
        TextView grid_item_device_name = holder.findView(R.id.grid_item_device_name);
        ImageView grid_item_device = holder.findView(R.id.grid_item_device);
        TextView grid_item_device_value = holder.findView(R.id.grid_item_device_value);


        RelativeLayout grid_item_left_layout = holder.findView(R.id.grid_item_left_layout);
        TextView grid_item_left_sel = holder.findView(R.id.grid_item_left_sel);
        ImageView grid_item_left_image = holder.findView(R.id.grid_item_left_image);
        TextView grid_item_left_alias = holder.findView(R.id.grid_item_left_alias);
        TextView grid_item_left_id = holder.findView(R.id.grid_item_left_id);
        ImageView grid_item_left_group = holder.findView(R.id.grid_item_left_group);


        RelativeLayout grid_item_right_layout = holder.findView(R.id.grid_item_right_layout);
        TextView grid_item_right_sel = holder.findView(R.id.grid_item_right_sel);
        ImageView grid_item_right_image = holder.findView(R.id.grid_item_right_image);
        TextView grid_item_right_alias = holder.findView(R.id.grid_item_right_alias);
        TextView grid_item_right_id = holder.findView(R.id.grid_item_right_id);
        ImageView grid_item_right_group = holder.findView(R.id.grid_item_right_group);
        ImageView grid_item_device_value_icon = holder.findView(R.id.grid_item_device_value_icon);

        /******设备未绑定******/
        if (deviceInfo.getDeviceStatus() == Entiy.DEVEICE_UNBIND) {
            grid_item_device_name.setVisibility(View.INVISIBLE);
            grid_item_device.setVisibility(View.INVISIBLE);
            grid_item_device_value.setVisibility(View.INVISIBLE);
            grid_item_left_layout.setVisibility(View.INVISIBLE);
            grid_item_right_layout.setVisibility(View.INVISIBLE);
        }else {
            if (!TextUtils.isEmpty(deviceInfo.getDeviceName())) {
                grid_item_device_name.setText(deviceInfo.getDeviceName()+"");
                grid_item_device_name.setVisibility(View.VISIBLE);
            }
            GlideUtil.loadDeviceImage(getContext(),grid_item_device, deviceInfo);
            grid_item_device.setVisibility(View.VISIBLE);
            grid_item_left_layout.setVisibility(View.VISIBLE);
            grid_item_device_value.setVisibility(View.VISIBLE);
            grid_item_device_value.setText("电量"+deviceInfo.getElectricQuantity() + "%");
            grid_item_device_value_icon.setVisibility(View.GONE);
            if (deviceInfo.getElectricQuantity() < 20) {
                grid_item_device_value_icon.setVisibility(View.VISIBLE);
            }
            grid_item_left_sel.setVisibility(View.GONE);

            ControlInfo controlInfo_0 = deviceInfo.getValveDeviceSwitchList().get(0);
            if (controlInfo_0.getValveGroupId() == 0) {
                grid_item_left_group.setVisibility(View.GONE);
            }else {
                grid_item_left_group.setVisibility(View.VISIBLE);
//                grid_item_left_group.setText(controlInfo_0.getValveGroupId()+"");
                GlideUtil.loadGroupImage(grid_item_left_group, controlInfo_0.getValveGroupId());
            }

            if (controlInfo_0.getValveStatus() == 0) {
                grid_item_left_image.setVisibility(View.INVISIBLE);
                grid_item_left_layout.setOnClickListener(null);
            }else {
                grid_item_left_image.setVisibility(View.VISIBLE);
//                grid_item_left_image.setImageResource(Entiy.getImageResource(controlInfo_0.getValveStatus()));
                GlideUtil.loadControlImage(getContext(),grid_item_left_image, controlInfo_0);
                grid_item_left_id.setText(controlInfo_0.getValveName()+"");
                grid_item_left_alias.setText(controlInfo_0.getValveAlias()+"");

                grid_item_left_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NoFastClickUtils.isFastClick()){
                            return;
                        }
                        openDevice(controlInfo_0);
                    }
                });
            }
            ControlInfo controlInfo_1 = deviceInfo.getValveDeviceSwitchList().get(1);
            grid_item_right_layout.setVisibility(View.VISIBLE);
            grid_item_right_sel.setVisibility(View.GONE);

            if (controlInfo_1.getValveGroupId() == 0) {
                grid_item_right_group.setVisibility(View.GONE);
            }else {
                grid_item_right_group.setVisibility(View.VISIBLE);
//                grid_item_right_group.setText(controlInfo_1.getValveGroupId()+"");
                GlideUtil.loadGroupImage(grid_item_right_group, controlInfo_1.getValveGroupId());
            }
            if (controlInfo_1.getValveStatus() == 0) {
                grid_item_right_image.setVisibility(View.INVISIBLE);
                grid_item_right_layout.setOnClickListener(null);
            }else {
                grid_item_right_image.setVisibility(View.VISIBLE);
//                grid_item_right_image.setImageResource(Entiy.getImageResource(controlInfo_1.getValveStatus()));
                GlideUtil.loadControlImage(getContext(),grid_item_right_image, controlInfo_1);
                grid_item_right_id.setText(controlInfo_1.getValveName()+"");
                grid_item_right_alias.setText(controlInfo_1.getValveAlias()+"");


                grid_item_right_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NoFastClickUtils.isFastClick()){
                            return;
                        }
                       openDevice(controlInfo_1);
                    }
                });
            }

            if (controlInfo_0.getValveStatus() == 0) {
                grid_item_left_layout.setVisibility(View.INVISIBLE);
            }else{
                grid_item_left_layout.setVisibility(View.VISIBLE);
            }

            if (controlInfo_1.getValveStatus() == 0) {
                grid_item_right_layout.setVisibility(View.INVISIBLE);
            }else{
                grid_item_right_layout.setVisibility(View.VISIBLE);
            }
        }
    }

        private void openDevice(final ControlInfo controlInfo) {
        String status = "关闭";
        if (controlInfo.getValveStatus() == Entiy.CONTROL_STATUS＿RUN) {
            status = "开启";
        }
        MainoptionDialog.ShowDialog((Activity) getContext(),controlInfo , "手动操作",status,new MainoptionDialog.ItemClick() {
            @Override
            public void onItemClick(int index) {
                /**
                 *    index = 0  读
                 *    index = 1  开阀
                 *    index = 2  关阀
                 */
                if (index == 0) {
                    TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_READ,controlInfo);
                    TaskManager.getInstance().startTask();
                }else if (index == 1) {
                    TaskFactory.createOpenTask(controlInfo);
                    TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_OPEN_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_OPEN_READ,controlInfo);
                    TaskManager.getInstance().startTask();
                }else if (index == 2) {
                    TaskFactory.createCloseTask(controlInfo);
                    TaskFactory.createReadSingleTask(controlInfo, TaskEntiy.TASK_OPTION_CLOSE_READ ,Entiy.ACTION_TYPE_4);
                    TaskFactory.createReadEndTask(TaskEntiy.TASK_OPTION_CLOSE_READ,controlInfo);
                    TaskManager.getInstance().startTask();
                }
            }
        });
    }
}
