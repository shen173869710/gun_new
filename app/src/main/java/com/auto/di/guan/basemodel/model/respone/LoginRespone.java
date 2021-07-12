package com.auto.di.guan.basemodel.model.respone;

import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;

import java.util.List;

/**
 *   用户登录返回相关数据
 */
public class LoginRespone {
    private User sysRes;
    private List<DeviceInfo> valveDeviceInfos;
    private List<GroupInfo> deviceGroupList;


    public User getSysRes() {
        return sysRes;
    }

    public void setSysRes(User sysRes) {
        this.sysRes = sysRes;
    }

    public List<DeviceInfo> getValveDeviceInfos() {
        return valveDeviceInfos;
    }

    public void setValveDeviceInfos(List<DeviceInfo> valveDeviceInfos) {
        this.valveDeviceInfos = valveDeviceInfos;
    }

    public List<GroupInfo> getDeviceGroupList() {
        return deviceGroupList;
    }

    public void setDeviceGroupList(List<GroupInfo> deviceGroupList) {
        this.deviceGroupList = deviceGroupList;
    }
}
