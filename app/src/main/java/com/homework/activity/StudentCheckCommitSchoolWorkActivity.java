package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.adapter.ExtraFileListAdapter;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.ExtraFile;
import com.homework.view.ToolBarView;
import com.wang.android_lib.adapter.NullAdapter;
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
    @Bind(R.id.lv_extra_file)
    ListView lvExtraFile;

    private CommitSchoolWork commitSchoolWork;
    private ExtraFileListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_school_work);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        if (commitSchoolWork == null) {
            lvExtraFile.setAdapter(new NullAdapter(this, "没有作业附件1"));
            return;
        }
        tvRemark.setText(commitSchoolWork.getRemark());

        List<ExtraFile> extraFileList = new ArrayList<>();
        Set<ExtraFile> extraFiles = commitSchoolWork.getExtraFiles();
        if (extraFiles != null) {
            Iterator<ExtraFile> iterator = extraFiles.iterator();
            while (iterator.hasNext()) {
                extraFileList.add(iterator.next());
            }
            //TODO 当前进度
//            adapter=new ExtraFileListAdapter(this,extraFileList);
        } else {
            lvExtraFile.setAdapter(new NullAdapter(this, "没有作业附件2"));
        }
    }

    private void initData() {
        String json = getIntent().getStringExtra("commitSchoolWork");
        if (!TextUtil.isEmpty(json)) {
            commitSchoolWork = new Gson().fromJson(json, CommitSchoolWork.class);
        }
    }

    public static void start(Context context, CommitSchoolWork commitSchoolWork) {
        Intent intent = new Intent(context, StudentCheckCommitSchoolWorkActivity.class);
        intent.putExtra("commitSchoolWork", new Gson().toJson(commitSchoolWork));
        context.startActivity(intent);
    }

}
