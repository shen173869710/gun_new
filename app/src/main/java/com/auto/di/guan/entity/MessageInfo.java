package com.auto.di.guan.entity;

import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.UserAction;

import java.util.List;

public class MessageInfo {

    private Long loginId;
    private List<DeviceInfo> devices;
    private List<GroupInfo> groups;
    private List<UserAction> actions;

    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public List<DeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceInfo> devices) {
        this.devices = devices;
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupInfo> groups) {
        this.groups = groups;
    }

    public List<UserAction> getActions() {
        return actions;
    }

    public void setActions(List<UserAction> actions) {
        this.actions = actions;
    }
}
