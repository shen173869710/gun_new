package com.auto.di.guan.db;

import com.auto.di.guan.entity.Entiy;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by czl on 2017/11/28.
 */
@Entity
public class DeviceInfo implements Serializable{
    static final long serialVersionUID=3L;
    @Id(autoincrement = true)
    private Long id;
    // 设备id
    @Property(nameInDb = "device_id")
    private int deviceId;
    // 设备名称
    @Property(nameInDb = "device_name")
    private String deviceName;
    // 设备位置
    @Property(nameInDb = "device_sort")
    private int deviceSort;
    // 本地通信协议ID
    private String protocalId;
    //所有者ID
    @Property(nameInDb = "user_id")
    private int userId;
    // 设备图片
    @Property(nameInDb = "device_image_path")
    private String deviceImagePath;
    // 创建时间
    @Property(nameInDb = "create_time")
    private String createTime;
    // 创建者
    @Property(nameInDb = "create_by")
    private String createBy;
    // 设备电量
    @Property(nameInDb = "electric_quantity")
    private int electricQuantity;
    /**
     *   0  未添加
     *   1  已经添加
     */
    @Property(nameInDb = "device_status")
    private int deviceStatus;
    @Property(nameInDb = "remark")
    private String remark;

    @Convert(columnType =String.class, converter = ControlConvert.class)
    @Property(nameInDb = "valve_device_switch_list")
    private ArrayList<ControlInfo>valveDeviceSwitchList;

    @Generated(hash = 2079321104)
    public DeviceInfo(Long id, int deviceId, String deviceName, int deviceSort,
            String protocalId, int userId, String deviceImagePath,
            String createTime, String createBy, int electricQuantity,
            int deviceStatus, String remark,
            ArrayList<ControlInfo> valveDeviceSwitchList) {
        this.id = id;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceSort = deviceSort;
        this.protocalId = protocalId;
        this.userId = userId;
        this.deviceImagePath = deviceImagePath;
        this.createTime = createTime;
        this.createBy = createBy;
        this.electricQuantity = electricQuantity;
        this.deviceStatus = deviceStatus;
        this.remark = remark;
        this.valveDeviceSwitchList = valveDeviceSwitchList;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

//    public int getDeviceId() {
//        return this.deviceId;
//    }
//
//    public void setDeviceId(int deviceId) {
//        this.deviceId = deviceId;
//    }

    public String getProtocalId() {
        return this.protocalId;
    }

    public void setProtocalId(String protocalId) {
        this.protocalId = protocalId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDeviceImagePath() {
        return this.deviceImagePath;
    }

    public void setDeviceImagePath(String deviceImagePath) {
        this.deviceImagePath = deviceImagePath;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getElectricQuantity() {
        return this.electricQuantity;
    }

    public void setElectricQuantity(int electricQuantity) {
        this.electricQuantity = electricQuantity;
    }

    public int getDeviceStatus() {
        return this.deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ArrayList<ControlInfo> getValveDeviceSwitchList() {
        return this.valveDeviceSwitchList;
    }

    public void setValveDeviceSwitchList(
            ArrayList<ControlInfo> valveDeviceSwitchList) {
        this.valveDeviceSwitchList = valveDeviceSwitchList;
    }

    /**
     *  绑定设备
     */
    public void bindDevice(int id) {
        setDeviceStatus(Entiy.DEVEICE_BIND);
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        controlInfos.add(new ControlInfo(id,"0",0));
        controlInfos.add(new ControlInfo(id, "1",0));
        setValveDeviceSwitchList(controlInfos);
    }
    /**
     *  解除绑定设备
     */
    public void unBindDevice(int id) {
        setDeviceStatus(Entiy.DEVEICE_UNBIND);
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        controlInfos.add(new ControlInfo(id,"0", 0));
        controlInfos.add(new ControlInfo(id,"1",0));
        setValveDeviceSwitchList(controlInfos);
    }

    public int getDeviceSort() {
        return this.deviceSort;
    }

    public void setDeviceSort(int deviceSort) {
        this.deviceSort = deviceSort;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     */
}
