package com.networklib;

/**
 * Created by zwb on 20/1/2.
 */

public class BaseResponse<T> {

    private String code;
    private String msg;
    private T obj;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return obj;
    }

    public void setData(T data) {
        this.obj = data;
    }

    public boolean isOk() {
        return "0".equals(code) || "200".equals(code);
    }
}
