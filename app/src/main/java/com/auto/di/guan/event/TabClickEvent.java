package com.auto.di.guan.event;


/**
 *   tab的点击事件
 */
public class TabClickEvent {
    private int index;

    public TabClickEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
