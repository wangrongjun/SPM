package com.homework.model.api;

public class MsgV2 {

    public static final int CODE_OK = 0;//成功
    public static final int CODE_ILLEGAL = -1;//权限错误
    public static final int CODE_ERROR_NORMAL = 1;//校验错误
    public static final int CODE_ERROR_STORAGE = 2;//存储错误
    public static final int CODE_ERROR_UNKNOWN = 3;//未知错误

    private int code;
    private String message;

}
