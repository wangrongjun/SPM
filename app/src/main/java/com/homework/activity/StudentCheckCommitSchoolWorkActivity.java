package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.ExtraFile;
import com.homework.bean.SchoolWork;
import com.homework.view.ToolBarView;
import com.wang.java_util.TextUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/12/1.
 */
public class StudentCheckCommitSchoolWorkActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_school_work_name)
    TextView tvSchoolWorkName;
    @Bind(R.id.tv_remark)
    TextView tvRemark;

    private SchoolWork schoolWork;
    private CommitSchoolWork commitSchoolWork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_student_check_school_work);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        if (!TextUtil.isEmpty(json)) {
            schoolWork = new Gson().fromJson(json, SchoolWork.class);
        }

        json = getIntent().getStringExtra("commitSchoolWork");
        if (!TextUtil.isEmpty(json)) {
            commitSchoolWork = new Gson().fromJson(json, CommitSchoolWork.class);
            Set<ExtraFile> extraFiles = commitSchoolWork.getExtraFiles();
            if (extraFiles != null) {
                List<ExtraFile> extraFileList = new ArrayList<>();
                Iterator<ExtraFile> iterator = extraFiles.iterator();
                while (iterator.hasNext()) {
                    extraFileList.add(iterator.next());
                }
                getIntent().putExtra("extraFileList", new Gson().toJson(extraFileList));
            }
        }
    }

    private void initToolBar() {
        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentCommitSchoolWorkActivity.start(StudentCheckCommitSchoolWorkActivity.this,
                        schoolWork, 345, 678);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345 && resultCode == 678) {
            finish();
        }
    }

    private void initView() {
        if (schoolWork != null) {
            tvSchoolWorkName.setText(schoolWork.getName());
        }
        if (commitSchoolWork != null) {
            tvRemark.setText(commitSchoolWork.getRemark());
        }
    }

    public static void start(Context context, SchoolWork schoolWork,
                             CommitSchoolWork commitSchoolWork) {
        Intent intent = new Intent(context, StudentCheckCommitSchoolWorkActivity.class);
        intent.putExtra("schoolWork", new Gson().toJson(schoolWork));
        intent.putExtra("commitSchoolWork", new Gson().toJson(commitSchoolWork));
        context.startActivity(intent);
    }

}
