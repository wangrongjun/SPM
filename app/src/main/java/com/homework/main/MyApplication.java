package com.homework.main;

import android.app.Application;
import android.os.Process;

import com.homework.constant.C;
import com.homework.util.MyNotification;
import com.homework.util.P;

import java.io.File;
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
        new File(C.dir).mkdirs();
        new File(C.cacheDir).mkdirs();
        new File(C.extraFileDir).mkdirs();
//        initDownload();

//        MyNotification.context = getApplicationContext();

        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {

                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                ex.printStackTrace(printWriter);

                MyNotification.showException(stringWriter.toString());

                Process.killProcess(Process.myPid());
            }
        };

//        Thread.setDefaultUncaughtExceptionHandler(handler);

    }
/*
    private void initDownload() {
//        1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

//      2.配置Builder
//      配置下载文件保存的文件夹
        builder.configFileDownloadDir(C.extraFileDir);
//      配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(3);
//      配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
//      开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
//       配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

//      3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }
*/
}
