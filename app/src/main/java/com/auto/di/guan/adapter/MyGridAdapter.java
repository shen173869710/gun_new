package com.auto.di.guan.adapter;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.GlideUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class MyGridAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder> {
    public MyGridAdapter(List<DeviceInfo> data) {
        super(R.layout.grid_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DeviceInfo deviceInfo) {

        TextView grid_item_device_id = holder.findView(R.id.grid_item_device_id);
        grid_item_device_id.setText(deviceInfo.getDeviceSort()+"");
        grid_item_device_id.setVisibility(View.VISIBLE);
        TextView grid_item_device_name = holder.findView(R.id.grid_item_device_name);
        grid_item_device_name.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
                grid_item_device_name.setText(deviceInfo.getDeviceName() + "");
                grid_item_device_name.setVisibility(View.VISIBLE);
            }
            GlideUtil.loadDeviceImage(getContext(),grid_item_device, deviceInfo);
            grid_item_device_id.setVisibility(View.VISIBLE);
            grid_item_device.setVisibility(View.VISIBLE);
            grid_item_device_value.setVisibility(View.VISIBLE);
            grid_item_device_value.setText("电量"+deviceInfo.getElectricQuantity() + "%");
            grid_item_device_value_icon.setVisibility(View.GONE);
            if (deviceInfo.getElectricQuantity() < 20) {
                grid_item_device_value_icon.setVisibility(View.VISIBLE);
            }
            grid_item_left_layout.setVisibility(View.VISIBLE);
            grid_item_left_sel.setVisibility(View.GONE);


            ControlInfo controlInfo0 = deviceInfo.getValveDeviceSwitchList().get(0);
            if (controlInfo0.getValveGroupId() == 0) {
                grid_item_left_group.setVisibility(View.GONE);
            } else {
                grid_item_left_group.setVisibility(View.VISIBLE);
//                grid_item_left_group.setText(controlInfo0.getValveGroupId() + "");

                GlideUtil.loadGroupImage(grid_item_left_group, controlInfo0.getValveGroupId());
            }
            if (controlInfo0.getValveStatus() == 0) {
                grid_item_left_image.setVisibility(View.INVISIBLE);
                grid_item_left_id.setText("");
                grid_item_left_alias.setText("");
            } else {
                grid_item_left_image.setVisibility(View.VISIBLE);
//                grid_item_left_image.setImageResource(Entiy.getImageResource(controlInfo0.getValveStatus()));
                GlideUtil.loadControlImage(getContext(),grid_item_left_image, controlInfo0);
                grid_item_left_id.setText(controlInfo0.getValveName() + "");
                grid_item_left_alias.setText(controlInfo0.getValveAlias() + "");
            }
            grid_item_right_layout.setVisibility(View.VISIBLE);
            grid_item_right_sel.setVisibility(View.GONE);

            ControlInfo controlInfo1 = deviceInfo.getValveDeviceSwitchList().get(1);
            if (controlInfo1.getValveGroupId() == 0) {
                grid_item_right_group.setVisibility(View.GONE);
            } else {
                grid_item_right_group.setVisibility(View.VISIBLE);
//                grid_item_right_group.setText(controlInfo1.getValveGroupId() + "");
                GlideUtil.loadGroupImage(grid_item_right_group, controlInfo1.getValveGroupId());
            }
            if (controlInfo1.getValveStatus() == 0) {
                grid_item_right_image.setVisibility(View.INVISIBLE);
                grid_item_right_id.setText("");
                grid_item_right_alias.setText("");
            } else {
                grid_item_right_image.setVisibility(View.VISIBLE);
//                grid_item_right_image.setImageResource(Entiy.getImageResource(controlInfo1.getValveStatus()));
                GlideUtil.loadControlImage(getContext(),grid_item_right_image, controlInfo1);
                grid_item_right_id.setText(controlInfo1.getValveName() + "");
                grid_item_right_alias.setText(controlInfo1.getValveAlias() + "");
            }

            if (controlInfo0.getValveStatus() == 0) {
                grid_item_left_layout.setVisibility(View.INVISIBLE);
            } else {
                grid_item_left_layout.setVisibility(View.VISIBLE);
            }

            if (controlInfo1.getValveStatus() == 0) {
                grid_item_right_layout.setVisibility(View.INVISIBLE);
            } else {
                grid_item_right_layout.setVisibility(View.VISIBLE);
            }
        }
    }
}
