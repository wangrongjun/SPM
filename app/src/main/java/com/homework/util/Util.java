package com.homework.util;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.homework.bean.Msg;
import com.homework.constant.C;
import com.wang.android_lib.util.M;

import java.lang.reflect.Type;

/**
 * by wangrongjun on 2016/10/31.
 */
public class Util {

    public static Pair<Boolean, Object> handleMsg(Context context, String result, Type type) {
        Msg msg;
        try {
            msg = new Gson().fromJson(result, type);
        } catch (JsonSyntaxException e) {
            msg = new Gson().fromJson(result, Msg.class);
            return new Pair<>(false, msg.getMessage());
        }
        switch (msg.getCode()) {
            case C.CODE_OK:
                return new Pair<>(true, msg.getMessage());
            case C.CODE_ERROR_NORMAL:
            case C.CODE_ERROR_STORAGE:
            case C.CODE_ILLEGAL:
            case C.CODE_ERROR_UNKNOWN:
                M.t(context, msg.getMessage() + "");
                break;
        }
        Object o = "不存在的状态码：" + msg.getCode();
        return new Pair<>(false, o);
    }

}
