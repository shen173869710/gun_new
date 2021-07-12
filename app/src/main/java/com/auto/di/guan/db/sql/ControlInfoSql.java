package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.auto.di.guan.db.sql.DeviceInfoSql.updateDeviceList;

public class ControlInfoSql extends BaseSql {

    public static void updateControl(ControlInfo info) {
        LogUtils.e("ControlInfoSql", "更新单个设备信息"+new Gson().toJson(info));
        List<DeviceInfo> deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo_0 = deviceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo_1 = deviceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (info.getValveId() == controlInfo_0.getValveId()) {
                controlInfo_0.setValveGroupId(0);
                controlInfo_0.setSelect(info.isSelect());
            }
            if (info.getValveId() == controlInfo_1.getValveId()) {
                controlInfo_1.setValveGroupId(0);
                controlInfo_1.setSelect(info.isSelect());
            }
        }
        updateDeviceList(deviceInfos);
    }



    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryControlList(int groupId) {
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo_0 = deviceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo_1 = deviceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (groupId == controlInfo_0.getValveGroupId()) {
                controlInfos.add(controlInfo_0);
            }
            if (groupId == controlInfo_1.getValveGroupId()) {
                controlInfos.add(controlInfo_1);
            }
        }
        return controlInfos;
    }

    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryControlList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            controlInfos.add(deviceInfos.get(i).getValveDeviceSwitchList().get(0));
            controlInfos.add(deviceInfos.get(i).getValveDeviceSwitchList().get(1));
        }
        return controlInfos;
    }


    /**
     * 查询已经开启的设备
     */
    public static List<ControlInfo> queryControlRunList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {

        }
        return controlInfos;
    }

    public static ControlInfo findControlById(int valueId) {
        ControlInfo controlInfo = null;
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size = deviceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo_0 = deviceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo_1 = deviceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (valueId == controlInfo_0.getValveId()) {
                controlInfo = controlInfo_0;
            }
            if (valueId == controlInfo_1.getValveId()) {
                controlInfo= controlInfo_1;
            }
        }
        return controlInfo;
    }
}
