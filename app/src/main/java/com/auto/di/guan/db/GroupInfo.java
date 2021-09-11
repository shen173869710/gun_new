package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/9.
 */

@Entity
public class GroupInfo {
    @Id(autoincrement = true)
    private Long id;
    private int groupId;
    private long userId;
    private String groupName;
    /**轮灌的状态 **/
    private int groupStatus;
    private int groupImage;
    /** 轮灌优先级**/
    private int groupLevel;
    /** 运行的总时间**/
    private int groupTime;
    /** 已经运行的时间**/
    private int groupRunTime;
    /**开始时间**/
    private long groupEndTime;
    // 是否参与轮灌设置
    private int groupIsJoin;
    /**轮灌是否已经暂停计时**/
    private int groupStop;
    @Generated(hash = 495944433)
    public GroupInfo(Long id, int groupId, long userId, String groupName,
            int groupStatus, int groupImage, int groupLevel, int groupTime,
            int groupRunTime, long groupEndTime, int groupIsJoin, int groupStop) {
        this.id = id;
        this.groupId = groupId;
        this.userId = userId;
        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupImage = groupImage;
        this.groupLevel = groupLevel;
        this.groupTime = groupTime;
        this.groupRunTime = groupRunTime;
        this.groupEndTime = groupEndTime;
        this.groupIsJoin = groupIsJoin;
        this.groupStop = groupStop;
    }
    @Generated(hash = 1250265142)
    public GroupInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getGroupId() {
        return this.groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getGroupStatus() {
        return this.groupStatus;
    }
    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }
    public int getGroupImage() {
        return this.groupImage;
    }
    public void setGroupImage(int groupImage) {
        this.groupImage = groupImage;
    }
    public int getGroupLevel() {
        return this.groupLevel;
    }
    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }
    public int getGroupTime() {
        return this.groupTime;
    }
    public void setGroupTime(int groupTime) {
        this.groupTime = groupTime;
    }
    public int getGroupRunTime() {
        return this.groupRunTime;
    }
    public void setGroupRunTime(int groupRunTime) {
        this.groupRunTime = groupRunTime;
    }
    public int getGroupIsJoin() {
        return this.groupIsJoin;
    }
    public void setGroupIsJoin(int groupIsJoin) {
        this.groupIsJoin = groupIsJoin;
    }
    public int getGroupStop() {
        return this.groupStop;
    }
    public void setGroupStop(int groupStop) {
        this.groupStop = groupStop;
    }
    public long getGroupEndTime() {
        return this.groupEndTime;
    }
    public void setGroupEndTime(long groupEndTime) {
        this.groupEndTime = groupEndTime;
    }


}
