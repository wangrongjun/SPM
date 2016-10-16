package com.spm.main;

import android.app.Application;

import com.spm.activity.ShowCrashActivity;
import com.spm.util.P;
import com.wang.android_lib.util.CrashUtil;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        P.context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(CrashUtil.getUncaughtExceptionHandler(
                getApplicationContext(),
                ShowCrashActivity.class,
                "crash_message"
        ));

    }

}
