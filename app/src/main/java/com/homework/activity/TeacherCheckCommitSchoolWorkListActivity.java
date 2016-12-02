package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.adapter.TeacherCommitSchoolWorkListAdapter;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.SchoolWork;
import com.homework.constant.C;
import com.homework.util.P;
import com.homework.view.ToolBarView;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.java_util.HttpUtil;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/26.
 */
public class TeacherCheckCommitSchoolWorkListActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.lv_commit_school_work)
    ListView lvCommitSchoolWork;

    private SchoolWork schoolWork;
    private TeacherCommitSchoolWorkListAdapter adapter;
    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check_school_work_list);
        ButterKnife.bind(this);
        initData();
        initView();
        startQueryCommitSchoolWork();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
    }

    private void initView() {
        toolBar.setTitleText(schoolWork.getName());
        lvCommitSchoolWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID) {
                    startQueryCommitSchoolWork();
                } else if (id != LoadingAdapter.LOADING_ADAPTER_ID) {
                    TeacherCheckCommitSchoolWorkActivity.start(
                            context,
                            adapter.getCommitSchoolWorkList().get(position),
                            schoolWork.getName()
                    );
                }
            }
        });
    }

    private void startQueryCommitSchoolWork() {
        lvCommitSchoolWork.setAdapter(new LoadingAdapter(this));

        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {
                handleResult(r.result);
            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvCommitSchoolWork.setAdapter(new NullAdapter(context, "点击重新加载"));
            }
        });
        helper.request(C.teacherGetCommitSchoolWorkUrl(schoolWork.getSchoolWorkId()));
    }

    private void handleResult(String result) {
//        Type type = new TypeToken<Msg<List<CommitSchoolWork>>>() {
//        }.getType();
//        Pair<Boolean, Object> msg = Util.handleMsg(this, result, type);
//        TODO 叫后台成功时返回Msg<List<CommitSchoolWork>>对象，而不是List<CommitSchoolWork>！！！
        Type type = new TypeToken<List<CommitSchoolWork>>() {
        }.getType();
        List<CommitSchoolWork> commitSchoolWorkList = new Gson().fromJson(result, type);
        if (commitSchoolWorkList != null && commitSchoolWorkList.size() > 0) {
            adapter = new TeacherCommitSchoolWorkListAdapter(this, commitSchoolWorkList);
            lvCommitSchoolWork.setAdapter(adapter);
        } else {
            lvCommitSchoolWork.setAdapter(new NullAdapter(this, "没有提交的作业"));
        }
    }

    public static void start(Context context, SchoolWork schoolWork) {
        Intent intent = new Intent(context, TeacherCheckCommitSchoolWorkListActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        context.startActivity(intent);
    }

}
