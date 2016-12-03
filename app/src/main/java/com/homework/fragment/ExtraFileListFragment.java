package com.homework.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.adapter.ExtraFileListAdapter;
import com.homework.bean.ExtraFile;
import com.homework.constant.C;
import com.homework.service.DownloadExtraFileService;
import com.homework.util.Util;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.util.IntentOpenFileUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.Pair;
import com.wang.java_util.TextUtil;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/26.
 */
public class ExtraFileListFragment extends Fragment {

    @Bind(R.id.lv_extra_file)
    ListView lvExtraFile;

    public static final String ACTION_UPDATE_DOWNLOAD_STATE = "com.homework.update_download_state";

    private List<ExtraFile> extraFileList;
    private ExtraFileListAdapter adapter;
    private ServiceConnection serviceConnection;
    private DownloadExtraFileService.MyBinder myBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DebugUtil.println("onCreateView");
        View view = inflater.inflate(R.layout.fragment_school_work_info, container, false);
        ButterKnife.bind(this, view);
        initData();
        initReceiver();
        initView();
        initService();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(receiver);
        getActivity().unbindService(serviceConnection);
        getActivity().stopService(new Intent(getActivity(), DownloadExtraFileService.class));
    }

    private void initData() {
        String json = getActivity().getIntent().getStringExtra("extraFileList");
        DebugUtil.println(json);
        if (!TextUtil.isEmpty(json)) {
            Type type = new TypeToken<List<ExtraFile>>() {
            }.getType();
            extraFileList = new Gson().fromJson(json, type);
        }
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_UPDATE_DOWNLOAD_STATE);
        getActivity().registerReceiver(receiver, filter);
    }

    private void initView() {

        lvExtraFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (id == NullAdapter.NULL_ADAPTER_ID) {
                    return;
                }

                Pair<ExtraFile, ExtraFileListAdapter.State> extraFileState =
                        adapter.getExtraFileStateList().get(position);

                switch (extraFileState.second) {
                    case already_download:
                        String fileName = Util.getFileName(extraFileState.first.getFileUrl());
                        Intent intent =
                                IntentOpenFileUtil.getRandomFileIntent(C.extraFileDir + fileName);
                        startActivity(intent);
                        break;
                    case downloading:
                        break;
                    case download_failed:
                    case not_exists:
                        myBinder.addDownloadTask(extraFileState.first);
                        updateDownloadButton(extraFileState.first.getFileUrl(),
                                ExtraFileListAdapter.State.downloading);
                        break;
                }
            }
        });

        if (extraFileList != null && extraFileList.size() > 0) {
            showExtraFileList(extraFileList);
        } else {
            lvExtraFile.setAdapter(new NullAdapter(getActivity(), "暂无附件"));
        }

    }

    private void initService() {
        Intent intent = new Intent(getActivity(), DownloadExtraFileService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (DownloadExtraFileService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void updateDownloadButton(String fileUrl, ExtraFileListAdapter.State state) {

        if (adapter == null) {//设A，B页面都包含该Fragment,从A页面打开B页面（此时A页面被覆盖），
            // 如果在B页面进行下载，下载会发出广播通知B更新，但同时A也收到广播，从而执行A的
            // onReceive。但是此时A处于onStop状态，A的fragment的adapter为空，就会抛出空指针异常。
            return;
        }

        List<Pair<ExtraFile, ExtraFileListAdapter.State>> extraFileStateList =
                adapter.getExtraFileStateList();
        for (Pair<ExtraFile, ExtraFileListAdapter.State> extraFileState : extraFileStateList) {
            if (extraFileState.first.getFileUrl().equals(fileUrl)) {
                extraFileState.second = state;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showExtraFileList(List<ExtraFile> extraFileList) {
        List<Pair<ExtraFile, ExtraFileListAdapter.State>> extraFileStateList = new ArrayList<>();
        for (ExtraFile extraFile : extraFileList) {
            String fileName = C.extraFileDir + Util.getFileName(extraFile.getFileUrl());
            if (new File(fileName).exists()) {
                extraFileStateList.add(
                        new Pair<>(extraFile, ExtraFileListAdapter.State.already_download));
            } else {
                extraFileStateList.add(
                        new Pair<>(extraFile, ExtraFileListAdapter.State.not_exists));
            }
        }
        adapter = new ExtraFileListAdapter(getActivity(), extraFileStateList);
        lvExtraFile.setAdapter(adapter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_UPDATE_DOWNLOAD_STATE:
                    int state = intent.getIntExtra("state", DownloadExtraFileService.STATE_FAILED);
                    String fileUrl = intent.getStringExtra("fileUrl");
                    String fileName = intent.getStringExtra("fileName");
                    String filePath = intent.getStringExtra("filePath");
                    if (state == DownloadExtraFileService.STATE_SUCCEED) {
                        M.t(getActivity(), "文件" + fileName + "下载成功");
                        updateDownloadButton(fileUrl, ExtraFileListAdapter.State.already_download);
                    } else if (state == DownloadExtraFileService.STATE_FAILED) {
                        String e = intent.getStringExtra("exception");
                        M.t(getActivity(), "文件" + fileName + "下载失败，" + e);
                    }
                    break;
            }
        }
    };

}