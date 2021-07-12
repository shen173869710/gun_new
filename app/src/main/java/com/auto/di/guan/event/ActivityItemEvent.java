package com.auto.di.guan.event;

/**
 *   Activity 点击或者关闭事件
 */
public class ActivityItemEvent {
    private int index;
    private String sn;
    private String snType;

    public ActivityItemEvent(int index) {
        this.index = index;
    }

    public ActivityItemEvent(int index, String sn, String snType) {
        this.index = index;
        this.sn = sn;
        this.snType = snType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSnType() {
        return snType;
    }

    public void setSnType(String snType) {
        this.snType = snType;
    }
}
