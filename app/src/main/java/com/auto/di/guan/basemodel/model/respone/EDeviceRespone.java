package com.auto.di.guan.basemodel.model.respone;

public class EDeviceRespone {
    private String timestamp;
    private String datetime;
    private EDeviceDataRespone values;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public EDeviceDataRespone getValues() {
        return values;
    }

    public void setValues(EDeviceDataRespone values) {
        this.values = values;
    }
}
