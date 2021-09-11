package com.auto.di.guan.utils;


import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.User;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 *   对象拷贝
 */
public class CopyObject {

    public static DeviceInfo copyData(DeviceInfo info) {
        DeviceInfo deviceInfo = new DeviceInfo();

        deviceInfo.setDeviceId(info.getDeviceId());
        deviceInfo.setDeviceName(info.getDeviceName());
        deviceInfo.setDeviceSort(info.getDeviceSort());
        deviceInfo.setProtocalId(info.getProtocalId());
        deviceInfo.setUserId(info.getUserId());
        deviceInfo.setDeviceStatus(info.getDeviceStatus());
        deviceInfo.setDeviceImagePath(info.getDeviceImagePath());
        deviceInfo.setCreateTime(info.getCreateTime());
        deviceInfo.setCreateBy(info.getCreateBy());
        deviceInfo.setElectricQuantity(info.getElectricQuantity());
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();

        ControlInfo controlInfo1 = new ControlInfo();
        ControlInfo info1 = info.getValveDeviceSwitchList().get(0);
        controlInfo1.setValveSwitchId(info1.getValveSwitchId());
        controlInfo1.setValveGroupId(info1.getValveGroupId());
        controlInfo1.setDeviceId(info1.getDeviceId());
        controlInfo1.setValveId(info1.getValveId());
        controlInfo1.setValveImgageId(info1.getValveImgageId());
        controlInfo1.setValveImgagePath(info1.getValveImgagePath());
        controlInfo1.setValveName(info1.getValveName());
        controlInfo1.setValveAlias(info1.getValveAlias());
        controlInfo1.setValveStatus(info1.getValveStatus());
        controlInfo1.setCreateBy(info1.getCreateBy());
        controlInfo1.setCreateTime(info1.getCreateTime());
        controlInfo1.setUpdateBy(info1.getUpdateBy());
        controlInfo1.setUpdateTime(info1.getUpdateTime());
        controlInfo1.setProtocalId(info1.getProtocalId());
        controlInfo1.setDeviceProtocalId(info1.getDeviceProtocalId());

        ControlInfo controlInfo2 = new ControlInfo();
        ControlInfo info2 = info.getValveDeviceSwitchList().get(1);
        controlInfo2.setValveSwitchId(info2.getValveSwitchId());
        controlInfo2.setValveGroupId(info2.getValveGroupId());
        controlInfo2.setDeviceId(info2.getDeviceId());
        controlInfo2.setValveId(info2.getValveId());
        controlInfo2.setValveImgageId(info2.getValveImgageId());
        controlInfo2.setValveImgagePath(info2.getValveImgagePath());
        controlInfo2.setValveName(info2.getValveName());
        controlInfo2.setValveAlias(info2.getValveAlias());
        controlInfo2.setValveStatus(info2.getValveStatus());
        controlInfo2.setCreateBy(info2.getCreateBy());
        controlInfo2.setCreateTime(info2.getCreateTime());
        controlInfo2.setUpdateBy(info2.getUpdateBy());
        controlInfo2.setUpdateTime(info2.getUpdateTime());
        controlInfo2.setProtocalId(info2.getProtocalId());
        controlInfo2.setDeviceProtocalId(info2.getDeviceProtocalId());
        controlInfos.add(controlInfo1);
        controlInfos.add(controlInfo2);
        deviceInfo.setValveDeviceSwitchList(controlInfos);
        return deviceInfo;
    }


    public static GroupInfo copyGroup(GroupInfo info) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(info.getGroupId());
        groupInfo.setGroupName(info.getGroupName());
        groupInfo.setGroupStatus(info.getGroupStatus());
        groupInfo.setGroupTime(info.getGroupTime());
        groupInfo.setGroupLevel(info.getGroupLevel());
        groupInfo.setGroupRunTime(info.getGroupRunTime());
        groupInfo.setGroupIsJoin(info.getGroupIsJoin());
        groupInfo.setGroupStop(info.getGroupStop());
        groupInfo.setUserId(info.getUserId());
        return groupInfo;
    }


    public static User copyUser(User user) {
        User info = new User();
        info.setUserId(user.getUserId());
        info.setMemberId(user.getMemberId());
        info.setDeptId(user.getDeptId());
        info.setParentId(user.getParentId());
        info.setRoleId(user.getRoleId());
        info.setLoginName(user.getLoginName());
        info.setUserName(user.getUserName());
        info.setUserType(user.getUserType());
        info.setEmail(user.getEmail());
        info.setPhonenumber(user.getPhonenumber());
        info.setSex(user.getSex());
        info.setAvatar(user.getAvatar());
        info.setPassword(user.getPassword());
        info.setSalt(user.getSalt());
        info.setStatus(user.getStatus());
        info.setDelFlag(user.getDelFlag());
        info.setLoginIp(user.getLoginIp());
        info.setLoginDate(user.getLoginDate());
        info.setMac(user.getMac());
        info.setParentId(user.getParentId());
        info.setProjectGroupId(user.getProjectGroupId());
        info.setTrunkPipeNum(user.getTrunkPipeNum());
        info.setTrunkPipeMaxNum(user.getTrunkPipeMaxNum());
        info.setPileOutNum(user.getPileOutNum());
        info.setProjectName(user.getProjectName());
        info.setProjectDesc(user.getProjectDesc());
        info.setProjectRemarks(user.getProjectRemarks());
        info.setLongitudeLatitude(user.getLongitudeLatitude());
        info.setParentLoginName(user.getParentLoginName());
        return info;
    }


    public static User copyUserData(User info, User user) {
        info.setUserId(user.getUserId());
        info.setMemberId(user.getMemberId());
        info.setDeptId(user.getDeptId());
        info.setParentId(user.getParentId());
        info.setRoleId(user.getRoleId());
        info.setLoginName(user.getLoginName());
        info.setUserName(user.getUserName());
        info.setUserType(user.getUserType());
        info.setEmail(user.getEmail());
        info.setPhonenumber(user.getPhonenumber());
        info.setSex(user.getSex());
        info.setAvatar(user.getAvatar());
        info.setPassword(user.getPassword());

        info.setSalt(user.getSalt());
        info.setStatus(user.getStatus());
        info.setDelFlag(user.getDelFlag());
        info.setLoginIp(user.getLoginIp());
        info.setLoginDate(user.getLoginDate());
        info.setMac(user.getMac());
        info.setParentId(user.getParentId());
        info.setProjectGroupId(user.getProjectGroupId());
        info.setTrunkPipeNum(user.getTrunkPipeNum());
        info.setTrunkPipeMaxNum(user.getTrunkPipeMaxNum());
        info.setPileOutNum(user.getPileOutNum());
        info.setProjectName(user.getProjectName());
        info.setProjectDesc(user.getProjectDesc());
        info.setProjectRemarks(user.getProjectRemarks());
        info.setLongitudeLatitude(user.getLongitudeLatitude());
        info.setParentLoginName(user.getParentLoginName());
        LogUtils.e("------登陆成功初始化密码", "" + (new Gson().toJson(info)));
        return info;
    }

}
