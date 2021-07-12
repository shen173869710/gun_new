package com.auto.di.guan.event;

public class UserStatusEvent {

    private String peerId;
    private int status;

    public UserStatusEvent(String peerId, int status) {
        this.peerId = peerId;
        this.status = status;
    }

    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
