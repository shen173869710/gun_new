package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ControlInfo implements Serializable {
    static final long serialVersionUID=2L;
    @Id(autoincrement = true)
    private Long id;
    private int valveSwitchId;
    private int valveGroupId;
    //设备 组 id
    private int deviceId;
    //设备id
    private int valveId;
    // 阀门图片地址
    private String valveImgagePath;
    // 阀门图片id地址
    private int valveImgageId;
    // 阀门的通信id
    private String valveName;
    /**
     * 绑定设备的ID
     *  显示的名字
     */
    private String valveAlias;
    /**
     *   0  未添加
     *   1  已经添加
     *   2  已经连接
     *   3  工作当中
     *   4  异常当中
     */
    private int valveStatus;
    //创建者
    private String createBy;
    //创建时间
    private String createTime;
    // 更新者
    private String updateBy;
    //更新时间
    private String updateTime;
    //通信协议项目ID
    private String protocalId;
    //通信协议ID
    private String deviceProtocalId;
    @Transient
    private boolean isSelect;

    public ControlInfo() {

    }
    public ControlInfo(int deviceId, String name, int valueStatus) {
        this.deviceId = deviceId;
        this.valveName = name;
        this.valveStatus = valueStatus;
    }

    public int getValveGroupId() {
        return valveGroupId;
    }

    public void setValveGroupId(int valveGroupId) {
        this.valveGroupId = valveGroupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getValveId() {
        return valveId;
    }

    public void setValveId(int valveId) {
        this.valveId = valveId;
    }

    public String getValveImgagePath() {
        return valveImgagePath;
    }

    public void setValveImgagePath(String valveImgagePath) {
        this.valveImgagePath = valveImgagePath;
    }

    public int getValveImgageId() {
        return valveImgageId;
    }

    public void setValveImgageId(int valveImgageId) {
        this.valveImgageId = valveImgageId;
    }

    public String getValveName() {
        return valveName;
    }

    public void setValveName(String valveName) {
        this.valveName = valveName;
    }

    public String getValveAlias() {
        return valveAlias;
    }

    public void setValveAlias(String valveAlias) {
        this.valveAlias = valveAlias;
    }

    public int getValveStatus() {
        return valveStatus;
    }

    public void setValveStatus(int valveStatus) {
        this.valveStatus = valveStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProtocalId() {
        return protocalId;
    }

    public void setProtocalId(String protocalId) {
        this.protocalId = protocalId;
    }

    public String getDeviceProtocalId() {
        return deviceProtocalId;
    }

    public void setDeviceProtocalId(String deviceProtocalId) {
        this.deviceProtocalId = deviceProtocalId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getValveSwitchId() {
        return valveSwitchId;
    }

    public void setValveSwitchId(int valveSwitchId) {
        this.valveSwitchId = valveSwitchId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
