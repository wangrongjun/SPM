package com.homework.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.homework.bean.Msg;
import com.homework.constant.C;
import com.wang.android_lib.util.M;
import com.wang.java_util.Pair;

import java.lang.reflect.Type;

/**
 * by wangrongjun on 2016/10/31.
 */
public class Util {
    /**
     * 使用样例：
     * <p/>
     * Type type = new TypeToken<Msg<List<Student>>>() {
     * }.getType();
     * Pair<Boolean, Object> pair = Util.handleMsg(getActivity(), r.result, type);
     * if (pair.first) {
     * List<Student> studentList = (List<Student>) pair.second;
     * lvStudent.setAdapter(new StudentListAdapter(getActivity(), studentList));
     * } else {
     * M.t(getActivity(), pair.second + "");
     * }
     *
     * @param context
     * @param result
     * @param type
     * @return
     */
    public static Pair<Boolean, Object> handleMsg(Context context, String result, Type type) {
        Msg msg;
        try {
            msg = new Gson().fromJson(result, type);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
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
