package com.homework.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.adapter.ExtraFileListAdapter;
import com.homework.bean.ExtraFile;
import com.homework.bean.SchoolWork;
import com.homework.constant.C;
import com.homework.service.DownloadExtraFileService;
import com.homework.util.P;
import com.homework.util.Util;
import com.homework.view.ToolBarView;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.util.IntentOpenFileUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.Pair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/4.
 */
public class SchoolWorkInfoActivity extends BaseActivity {

    @Bind(R.id.tv_school_work_name)
    TextView tvSchoolWorkName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_school_work_content)
    TextView tvSchoolWorkContent;
    @Bind(R.id.lv_extra_file)
    ListView lvExtraFile;
    @Bind(R.id.tool_bar)
    ToolBarView toolBar;

    private SchoolWork schoolWork;
    private ExtraFileListAdapter adapter;
    private ServiceConnection serviceConnection;
    private DownloadExtraFileService.MyBinder myBinder;

    public static final String ACTION_UPDATE_DOWNLOAD_STATE = "com.homework.update_download_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_work_info);
        ButterKnife.bind(this);

        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);

        initView();
        initService();
        initReceiver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345 && resultCode == 678) {
            
        }
    }

    private void initView() {

        if (P.getRole() == C.ROLE_STUDENT) {
            toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudentCommitSchoolWorkActivity.start(SchoolWorkInfoActivity.this, schoolWork,
                            345, 678);
                }
            });
        } else {
            toolBar.getRightBtnTextView().setVisibility(View.GONE);
        }

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

        tvSchoolWorkName.setText(schoolWork.getName());
        tvSchoolWorkContent.setText(schoolWork.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s1 = sdf.format(schoolWork.getCreateDate());
        String s2 = sdf.format(schoolWork.getFinalDate());
        tvTime.setText(s1 + " 到 " + s2);

        List<ExtraFile> extraFileList = new ArrayList<>();
        Set<ExtraFile> extraFiles = schoolWork.getExtraFiles();
        Iterator<ExtraFile> iterator = extraFiles.iterator();
        while (iterator.hasNext()) {
            extraFileList.add(iterator.next());
        }
        if (extraFileList.size() > 0) {
            showExtraFileList(extraFileList);
        } else {
            lvExtraFile.setAdapter(new NullAdapter(this, "暂无附件"));
        }
    }

    private void initService() {
        Intent intent = new Intent(this, DownloadExtraFileService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myBinder = (DownloadExtraFileService.MyBinder) service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_UPDATE_DOWNLOAD_STATE);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(serviceConnection);
        stopService(new Intent(this, DownloadExtraFileService.class));
    }

    public static void start(Context context, SchoolWork schoolWork) {
        Intent intent = new Intent(context, SchoolWorkInfoActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        context.startActivity(intent);
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
                        M.t(SchoolWorkInfoActivity.this, "文件" + fileName + "下载成功");
                        updateDownloadButton(fileUrl, ExtraFileListAdapter.State.already_download);
                    } else if (state == DownloadExtraFileService.STATE_FAILED) {
                        String e = intent.getStringExtra("exception");
                        M.t(SchoolWorkInfoActivity.this, "文件" + fileName + "下载失败，" + e);
                    }
                    break;
            }
        }
    };

    private void updateDownloadButton(String fileUrl, ExtraFileListAdapter.State state) {
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
        adapter = new ExtraFileListAdapter(this, extraFileStateList);
        lvExtraFile.setAdapter(adapter);
    }

}
