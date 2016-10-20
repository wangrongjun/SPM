package com.homework.util;

import android.content.Context;

import com.wang.android_lib.util.NotificationUtil;

/**
 * by 王荣俊 on 2016/10/20.
 */
public class MyNotification {

    public static Context context;

    public static void showText(String text) {
        NotificationUtil.showNotification(context, 0, "Text", text);
    }

    public static void showUserInfo(String userInfo) {
        NotificationUtil.showNotification(context, 1, "UserInfo", userInfo);
    }

    public static void showCookie(String cookie) {
        NotificationUtil.showNotification(context, 2, "Cookie", cookie);
    }

    public static void showError(String error) {
        NotificationUtil.showNotification(context, 10, "Error", error);
    }

    public static void showException(String exception) {
        NotificationUtil.showNotification(context, 20, "Exception", exception);
    }
}
