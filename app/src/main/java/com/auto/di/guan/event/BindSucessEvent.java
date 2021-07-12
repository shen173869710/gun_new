package com.auto.di.guan.event;

/**
 *  绑定设备成功
 */
public class BindSucessEvent {
    private boolean isOk;
    private int type;
    private String result;

    public BindSucessEvent(boolean isOk, int type) {
        this.isOk = isOk;
        this.type = type;
    }

    public BindSucessEvent(boolean isOk, int type, String result) {
        this.isOk = isOk;
        this.type = type;
        this.result = result;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
