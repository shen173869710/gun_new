package com.auto.di.guan.event;

/**
 *   Activity 点击或者关闭事件
 */
public class ActivityEvent {
    private int index;

    public ActivityEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
