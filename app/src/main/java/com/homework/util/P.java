package com.homework.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.wang.android_lib.util.PrefUtil;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class P extends PrefUtil {

    public static Context context;
    private static final String prefName = "homework";

    /**
     * 用于判断是否第一次使用
     *
     * @return 第一次调用返回true，之后调用返回false
     */
    public static boolean isFirst() {
        return isFirst(context, prefName);
    }

    /**
     * 设置到Cookie
     */
    public static String getCookie() {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return pref.getString("cookie", null);
    }

    /**
     * 在获取验证码和登录成功时从Set-Cookie中获取
     */
    public static void setCookie(String cookie) {
        SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        pref.edit().putString("cookie", cookie).apply();
    }

    public static Student getStudent() {
        return getEntity(context, prefName, Student.class);
    }

    public static void setStudent(Student student) {
        if (student == null) {
            student = new Student();
        }
        setEntity(context, prefName, student);
    }

    public static Teacher getTeacher() {
        return getEntity(context, prefName, Teacher.class);
    }

    public static void setTeacher(Teacher teacher) {
        if (teacher == null) {
            teacher = new Teacher();
        }
        setEntity(context, prefName, teacher);
    }

}
