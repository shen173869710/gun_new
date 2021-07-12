package com.auto.di.guan.entity;

import com.auto.di.guan.db.ControlInfo;

import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;

public class RtmDevice {
    private Long id;
    // 设备id
    private int deviceId;
    // 设备名称
    private String deviceName;
    // 设备位置
    private int deviceSort;
    // 本地通信协议ID
    private String protocalId;
    //所有者ID
    private int userId;
    // 设备电量
    private int electricQuantity;
    /**
     *   0  未添加
     *   1  已经添加
     */
    private int deviceStatus;

    private ArrayList<RtmControl> valveDeviceSwitchList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceSort() {
        return deviceSort;
    }

    public void setDeviceSort(int deviceSort) {
        this.deviceSort = deviceSort;
    }

    public String getProtocalId() {
        return protocalId;
    }

    public void setProtocalId(String protocalId) {
        this.protocalId = protocalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getElectricQuantity() {
        return electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public ArrayList<RtmControl> getValveDeviceSwitchList() {
        return valveDeviceSwitchList;
    }

    public void setValveDeviceSwitchList(ArrayList<RtmControl> valveDeviceSwitchList) {
        this.valveDeviceSwitchList = valveDeviceSwitchList;
    }
}
