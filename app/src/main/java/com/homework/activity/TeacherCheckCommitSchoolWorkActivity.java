package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.ExtraFile;
import com.homework.view.ToolBarView;
import com.wang.java_util.TextUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2016/12/2.
 */
public class TeacherCheckCommitSchoolWorkActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_school_work_name)
    TextView tvSchoolWorkName;
    @Bind(R.id.tv_remark)
    TextView tvRemark;

    private CommitSchoolWork commitSchoolWork;
    private String schoolWorkName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_teacher_check_school_work);
        ButterKnife.bind(this);
        initView();
    }

    private void initData() {
        schoolWorkName = getIntent().getStringExtra("schoolWorkName");
        String json = getIntent().getStringExtra("commitSchoolWork");
        if (!TextUtil.isEmpty(json)) {
            commitSchoolWork = new Gson().fromJson(json, CommitSchoolWork.class);
            Set<ExtraFile> extraFiles = commitSchoolWork.getExtraFiles();
            List<ExtraFile> extraFileList = new ArrayList<>();
            if (extraFiles != null) {
                Iterator<ExtraFile> iterator = extraFiles.iterator();
                while (iterator.hasNext()) {
                    extraFileList.add(iterator.next());
                }
                getIntent().putExtra("extraFileList", new Gson().toJson(extraFileList));
            }
        }
    }

    private void initView() {
        tvSchoolWorkName.setText(schoolWorkName);
        if (commitSchoolWork != null) {
            toolBar.setTitleText(commitSchoolWork.getStudent().getStudentInformation().getRealName());
            tvRemark.setText(commitSchoolWork.getRemark());
        }
    }

    public static void start(Context context, CommitSchoolWork commitSchoolWork,
                             String schoolWorkName) {
        Intent intent = new Intent(context, TeacherCheckCommitSchoolWorkActivity.class);
        intent.putExtra("commitSchoolWork", new Gson().toJson(commitSchoolWork));
        intent.putExtra("schoolWorkName", schoolWorkName);
        context.startActivity(intent);
    }

    @OnClick(R.id.btn_set_grade)
    public void onClick() {
        //TODO 当前进度
    }
}
