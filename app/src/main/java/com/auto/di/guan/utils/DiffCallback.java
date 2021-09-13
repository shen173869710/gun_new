package com.auto.di.guan.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;

public class DiffCallback extends DiffUtil.ItemCallback<DeviceInfo> {
    private final String TAG = "DiffCallback";
    /**
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areItemsTheSame(@NonNull DeviceInfo oldItem, @NonNull DeviceInfo newItem) {
        return oldItem.getId() == newItem.getId();
    }

    /**
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    @Override
    public boolean areContentsTheSame(@NonNull DeviceInfo oldItem, @NonNull DeviceInfo newItem) {
        return isEquals(oldItem,newItem);
    }

    /**
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     *
     * @param oldItem Old data
     * @param newItem New data
     * @return Payload info. if return null, the entire item will be refreshed.
     */
    @Override
    public Object getChangePayload(@NonNull DeviceInfo oldItem, @NonNull DeviceInfo newItem) {
        return null;
    }


    boolean isEquals(DeviceInfo oldItem, DeviceInfo newItem) {
        ControlInfo oldInfo0 = oldItem.getValveDeviceSwitchList().get(0);
        ControlInfo oldInfo1 = oldItem.getValveDeviceSwitchList().get(1);
        ControlInfo newInfo0 = newItem.getValveDeviceSwitchList().get(0);
        ControlInfo newInfo1 = newItem.getValveDeviceSwitchList().get(1);
        if (oldItem.getDeviceStatus() == newItem.getDeviceStatus()
                && StringEqual(oldItem.getDeviceName(),newItem.getDeviceName())
                && oldItem.getElectricQuantity() == newItem.getElectricQuantity()

                && oldInfo0.getValveGroupId() == newInfo0.getValveGroupId()
                && StringEqual(oldInfo0.getValveName(), newInfo0.getValveName())
                && StringEqual(oldInfo0.getValveAlias(),newInfo0.getValveAlias())
                && oldInfo0.getValveStatus() == newInfo0.getValveStatus()
                && oldInfo1.getValveGroupId() == newInfo1.getValveGroupId()
                && StringEqual(oldInfo1.getValveName(), newInfo1.getValveName())
                && StringEqual(oldInfo1.getValveAlias(),newInfo1.getValveAlias())
                && oldInfo1.getValveStatus() == newInfo1.getValveStatus()
        ) {
            LogUtils.e(TAG, "内容相等");
            return  true;
        }
        LogUtils.e(TAG, "内容变化");
        return false;
    }


    boolean StringEqual(String oldStr, String newStr) {
        if (oldStr == null && newStr == null) {
            return true;
        }

        if (oldStr != null && oldStr.equals(newStr)) {
            return true;
        }
        return false;
    }
}
