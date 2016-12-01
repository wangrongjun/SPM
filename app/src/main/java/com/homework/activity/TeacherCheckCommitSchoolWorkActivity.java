package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.SchoolWork;
import com.homework.constant.C;
import com.homework.util.P;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.java_util.HttpUtil;

/**
 * by wangrongjun on 2016/11/26.
 */
public class TeacherCheckCommitSchoolWorkActivity extends BaseActivity {

    private SchoolWork schoolWork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO 当前进度
//        initData();
//        startQueryCommitSchoolWork();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
    }

    public static void start(Context context, SchoolWork schoolWork) {
        Intent intent = new Intent(context, TeacherCheckCommitSchoolWorkActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        context.startActivity(intent);
    }

    private void startQueryCommitSchoolWork() {
        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {

            }
        });
        helper.request(C.teacherGetCommitSchoolWorkUrl(schoolWork.getSchoolWorkId()));
    }
}
