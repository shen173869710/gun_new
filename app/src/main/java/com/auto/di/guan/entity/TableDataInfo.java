package com.auto.di.guan.entity;

import java.io.Serializable;

public class TableDataInfo implements Serializable {
    /** 总记录数 */
    private long total;

    /** 消息状态码 */
    private int code;
    /** 消息内容 */
    private int msg;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
