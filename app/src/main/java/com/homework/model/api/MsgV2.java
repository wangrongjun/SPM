package com.homework.model.api;

import com.google.gson.Gson;
import com.homework.constant.StateCode;

import java.lang.reflect.Type;

public class MsgV2 {

    public static final int CODE_ILLEGAL = -1;//权限错误
    public static final int CODE_OK = 0;//成功
    public static final int CODE_ERROR_NORMAL = 1;//校验错误
    public static final int CODE_ERROR_STORAGE = 2;//存储错误
    public static final int CODE_ERROR_UNKNOWN = 3;//未知错误

    private int code;
    private Object message;

    public <T> Response<T> toResponse(Type entityType) {
        switch (code) {
            case CODE_OK:
                try {
                    T entity = new Gson().fromJson(message.toString(), entityType);
                    return new Response<>(StateCode.OK, entity);
                } catch (Exception e) {//如果gson无法解析，则说明正常返回的就是字符串
                    e.printStackTrace();
                    return new Response<>(StateCode.PARAM_NULL, message.toString());
                }

            case CODE_ILLEGAL:
                return new Response<>(StateCode.ERROR_ILLEGAL, message.toString());

            case CODE_ERROR_NORMAL:
                return new Response<>(StateCode.ERROR_NORMAL, message.toString());

            case CODE_ERROR_STORAGE:
                return new Response<>(StateCode.ERROR_STORAGE, message.toString());

            case CODE_ERROR_UNKNOWN:
                return new Response<>(StateCode.ERROR_UNKNOWN, message.toString());

            default:
                return new Response<>(StateCode.ERROR_UNKNOWN, message.toString());
        }
    }

}
