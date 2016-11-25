package com.homework.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.homework.bean.ExtraFile;
import com.homework.constant.C;
import com.homework.fragment.SchoolWorkInfoFragment;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.java_util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/11/25.
 */
public class DownloadExtraFileService extends Service {

    public static final int STATE_SUCCEED = 1;
    public static final int STATE_FAILED = -1;

    private MyBinder myBinder = new MyBinder();
    private List<ExtraFile> extraFileList = new ArrayList<>();
    private Thread downloadThread;
    private boolean isServiceAlive = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceAlive = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {

        public void addDownloadTask(ExtraFile extraFile) {
            extraFileList.add(extraFile);
            startDownload();
        }

    }

    private void startDownload() {

        if (downloadThread != null && downloadThread.isAlive()) {
            return;
        }
        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isServiceAlive && extraFileList.size() > 0) {
                    ExtraFile extraFile = extraFileList.get(0);
                    extraFileList.remove(0);
                    String filePath = C.cacheDir + Util.getFileName(extraFile.getFileUrl());
                    try {
                        Util.download(C.hostUrl + extraFile.getFileUrl(), P.getCookie(), filePath);
                        String newFilePath = C.extraFileDir + Util.getFileName(extraFile.getFileUrl());
                        FileUtil.copy(filePath, newFilePath);
                        FileUtil.delete(filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent(SchoolWorkInfoFragment.ACTION_UPDATE_DOWNLOAD_STATE);
                        intent.putExtra("state", STATE_FAILED);
                        intent.putExtra("fileUrl", extraFile.getFileUrl());
                        intent.putExtra("fileName", extraFile.getFileName());
                        intent.putExtra("filePath", filePath);
                        intent.putExtra("exception", e.toString());
                        sendBroadcast(intent);
                        continue;
                    }
                    Intent intent = new Intent(SchoolWorkInfoFragment.ACTION_UPDATE_DOWNLOAD_STATE);
                    intent.putExtra("state", STATE_SUCCEED);
                    intent.putExtra("fileUrl", extraFile.getFileUrl());
                    intent.putExtra("fileName", extraFile.getFileName());
                    intent.putExtra("filePath", filePath);
                    sendBroadcast(intent);
                }
            }
        });
        downloadThread.start();
    }

}
