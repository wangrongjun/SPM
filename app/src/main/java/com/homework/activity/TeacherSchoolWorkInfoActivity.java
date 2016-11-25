package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.SchoolWork;
import com.homework.view.ToolBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/4.
 */
public class TeacherSchoolWorkInfoActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_state)
    TextView tvState;

    private SchoolWork schoolWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_school_work_info);
        ButterKnife.bind(this);

        initData();
        initToolBar();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
    }

    private void initToolBar() {

        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherCheckCommitSchoolWorkActivity.start(
                        TeacherSchoolWorkInfoActivity.this, schoolWork);
            }
        });

    }

    public static void start(Context context, SchoolWork schoolWork) {
        Intent intent = new Intent(context, TeacherSchoolWorkInfoActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        context.startActivity(intent);
    }

}
