package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.ExtraFile;
import com.homework.bean.SchoolWork;
import com.homework.view.ToolBarView;

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
public class TeacherSchoolWorkInfoActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.tv_school_work_name)
    TextView tvSchoolWorkName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_school_work_content)
    TextView tvSchoolWorkContent;

    private SchoolWork schoolWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();//必须放在setContentView之前，因为setContentView执行时会导致fragment的
        // onCreateView执行，从而使fragment无法获取在initData中设置的extraFileList
        setContentView(R.layout.activity_teacher_school_work_info);
        ButterKnife.bind(this);

        initData();
        initView();
        initToolBar();
    }

    private void initView() {
        tvSchoolWorkName.setText("作业题目：" + schoolWork.getName());
        tvSchoolWorkContent.setText("作业内容：" + schoolWork.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s1 = sdf.format(schoolWork.getCreateDate());
        String s2 = sdf.format(schoolWork.getFinalDate());
        tvTime.setText(s1 + " 到 " + s2);
    }

    private void initData() {
        String json = getIntent().getStringExtra("schoolWork");
        schoolWork = new Gson().fromJson(json, SchoolWork.class);
        Set<ExtraFile> extraFiles = schoolWork.getExtraFiles();
        if (extraFiles != null) {
            List<ExtraFile> extraFileList = new ArrayList<>();
            Iterator<ExtraFile> iterator = extraFiles.iterator();
            while (iterator.hasNext()) {
                extraFileList.add(iterator.next());
            }
            // 给SchoolWorkInfoFragment使用
            getIntent().putExtra("extraFileList", new Gson().toJson(extraFileList));
        }
    }

    private void initToolBar() {

        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherCheckCommitSchoolWorkListActivity.start(
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
