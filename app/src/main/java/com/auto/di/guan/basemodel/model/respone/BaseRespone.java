package com.auto.di.guan.basemodel.model.respone;

/**
 * Created by Administrator on 2017/6/26.
 */

public class BaseRespone<T>{

    private int code;
    private T data;
    private String message;
    private String token;

    public boolean messageOk() {
        if ("ok".equals(message)) {
            return true;
        }else {
            return false;
        }
    }

    public boolean isOk(){
        if(code == 200) {
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
