package com.spm.main;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.spm.activity.ExceptionActivity;
import com.spm.util.P;
import com.wang.android_lib.util.NotificationUtil;
import com.wang.java_util.GsonUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class MyApplication extends Application {

    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        P.context = getApplicationContext();
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(myHandler);
    }

    private Thread.UncaughtExceptionHandler myHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            String error = getError(ex);
            String packageInfo = getPackageInfo();
            String mobileInfo = getMobileInfo();

            String s = packageInfo + "\n\n\n" + mobileInfo + "\n\n\n" + error;

            NotificationUtil.showNotification(MyApplication.this, 0, "Error", s);

            final Intent intent = new Intent(MyApplication.this, ExceptionActivity.class);
            intent.putExtra("message", s);
//            startActivity(intent);

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    private String getMobileInfo() {

        StringBuilder builder = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String name = fields[i].getName();
            String value = null;
            try {
                value = fields[i].get(null).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            builder.append(name + " : " + value + "\n");
        }
        return builder.toString();

    }

    private String getPackageInfo() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return GsonUtil.formatJson(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getError(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
