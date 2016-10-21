package com.homework.bean;

public class Msg<T> {

    private int code;//返回码
    private T message;//返回内容

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
