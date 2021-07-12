package com.auto.di.guan.entity;

public class BaseMessage {
    private int event;
    private MessageInfo data;

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public MessageInfo getData() {
        return data;
    }

    public void setData(MessageInfo data) {
        this.data = data;
    }
}
