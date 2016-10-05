package com.spm.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.spm.bean.Student;
import com.wang.android_lib.util.PrefUtil;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class P extends PrefUtil {

    public static Context context;
    private static final String prefName = "spm";

    /**
     * 用于判断是否第一次使用
     *
     * @return 第一次调用返回true，之后调用返回false
     */
    public static boolean isFirst() {
        return isFirst(context, prefName);
//        return true;
    }

    public static String getCookie() {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString("cookie", null);
    }

    public static void setCookie(String cookie) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        pref.edit().putString("cookie", cookie).apply();
    }

    public static Student getStudent() {
        return getEntity(context, prefName, Student.class);
    }

    public static void setStudent(Student student) {
        setEntity(context, prefName, student);
    }

}
