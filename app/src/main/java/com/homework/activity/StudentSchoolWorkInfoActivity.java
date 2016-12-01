package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.Msg;
import com.homework.bean.SchoolWork;
import com.homework.constant.C;
import com.homework.util.P;
import com.homework.util.Util;
import com.homework.view.ToolBarView;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.android_lib.util.DialogUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/4.
 */
public class StudentSchoolWorkInfoActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_state)
    TextView tvState;

    private SchoolWork schoolWork;
    private CommitSchoolWork commitSchoolWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_school_work_info);
        ButterKnife.bind(this);

        initData();
        startQueryCommitState();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
    }

    private void startQueryCommitState() {
        DialogUtil.showProgressDialog(this, "正在获取状态");
        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {
                toolBar.getRightBtnTextView().setVisibility(View.VISIBLE);
                handleResult(r.result);
            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                toolBar.getRightBtnTextView().setVisibility(View.GONE);
                tvState.setText("提交状态获取失败");
            }
        });
        helper.request(C.studentGetCommitSchoolWorkUrl(schoolWork.getSchoolWorkId()));
    }

    private void handleResult(String result) {
        Type type = new TypeToken<Msg<CommitSchoolWork>>() {
        }.getType();
        Pair<Boolean, Object> handleMsg = Util.handleMsg(this, result, type);
        if (handleMsg.first) {
            commitSchoolWork = (CommitSchoolWork) handleMsg.second;
            tvState.setText("已提交");
            toolBar.getRightBtnTextView().setVisibility(View.VISIBLE);
            toolBar.getRightBtnTextView().setText("查看");
            toolBar.getRightBtnTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudentCommitSchoolWorkActivity.start(StudentSchoolWorkInfoActivity.this, schoolWork,
                            345, 678);
                }
            });
        } else {
            tvState.setText("未提交");
            toolBar.getRightBtnTextView().setText("提交");
            toolBar.getRightBtnTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudentCommitSchoolWorkActivity.start(StudentSchoolWorkInfoActivity.this, schoolWork,
                            345, 678);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345 && resultCode == 678) {

        }
    }

    public static void start(Context context, SchoolWork schoolWork) {
        Intent intent = new Intent(context, StudentSchoolWorkInfoActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        context.startActivity(intent);
    }

}
