package com.homework.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.SchoolWork;
import com.homework.util.HttpUtil;
import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.IntentUtil;
import com.wang.android_lib.util.M;
import com.wang.android_lib.util.PathUtil;
import com.wang.android_lib.view.BorderEditText;
import com.wang.java_util.Pair;

import org.apache.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2016/11/25.
 */
public class StudentCommitSchoolWorkActivity extends BaseActivity {

    @Bind(R.id.tv_school_work_name)
    TextView tvSchoolWorkName;
    @Bind(R.id.et_remark)
    BorderEditText etRemark;
    @Bind(R.id.tv_file_path_list)
    TextView tvFilePathList;

    private SchoolWork schoolWork;
    private List<String> filePathList;
    private static int resultCode;//该界面消失时通知上一界面刷新状态时用到

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_commit_school_work);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
        filePathList = new ArrayList<>();
    }

    private void initView() {
        tvSchoolWorkName.setText("作业题目：" + schoolWork.getName());
    }

    public static void start(Activity activity, SchoolWork schoolWork,
                             int requestCode, int resultCode) {
        Intent intent = new Intent(activity, StudentCommitSchoolWorkActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        activity.startActivityForResult(intent, requestCode);
        StudentCommitSchoolWorkActivity.resultCode = resultCode;
    }

    @OnClick({R.id.btn_add_school_work_file, R.id.btn_commit_school_work})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_school_work_file:
                startActivityForResult(IntentUtil.getFileIntent(), 0);
                break;
            case R.id.btn_commit_school_work:
                int schoolWorkId = schoolWork.getSchoolWorkId();
                String remark = etRemark.getText();
                if (filePathList.size() > 0) {
                    startCommitSchoolWork(schoolWorkId, remark, filePathList);
                } else {
                    M.t(this, "请选择提交的文件");
                }
                break;
        }
    }

    private void startCommitSchoolWork(final int schoolWorkId,
                                       final String remark,
                                       final List<String> filePathList) {

        DialogUtil.showProgressDialog(this, "正在提交");

        new AsyncTask<Void, Void, Pair<Integer, String>>() {
            @Override
            protected Pair<Integer, String> doInBackground(Void... params) {
                return HttpUtil.studentCommitSchoolWork(schoolWorkId, remark, filePathList);
            }

            @Override
            protected void onPostExecute(Pair<Integer, String> stateResult) {
                DialogUtil.cancelProgressDialog();
                if (stateResult.first == HttpStatus.SC_OK) {
                    M.t(StudentCommitSchoolWorkActivity.this, "提交成功");
                    setResult(resultCode);
                    finish();
                } else {
                    M.t(StudentCommitSchoolWorkActivity.this, "提交失败，" + stateResult.second);
                }
            }
        }.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        Uri uri = data.getData();
        if (uri == null) return;

        filePathList.add(PathUtil.getRealFilePath(this, uri));
        String s = "";
        for (String path : filePathList) {
            s += path + "\n";
        }
        tvFilePathList.setText(s);
    }
}
