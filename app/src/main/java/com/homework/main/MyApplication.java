package com.homework.main;

import android.app.Application;
import android.os.Process;

import com.homework.util.MyNotification;
import com.homework.util.P;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        P.context = getApplicationContext();
        MyNotification.context = getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ex.printStackTrace(printWriter);

                MyNotification.showException(stringWriter.toString());

                Process.killProcess(Process.myPid());
            }
        });

    }

}
