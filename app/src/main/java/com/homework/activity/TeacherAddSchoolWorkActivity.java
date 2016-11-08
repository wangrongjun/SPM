package com.homework.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.wang.android_lib.util.IntentUtil;
import com.wang.android_lib.util.M;
import com.wang.android_lib.view.BorderEditText;
import com.wang.java_util.TextUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2016/11/8.
 */
public class TeacherAddSchoolWorkActivity extends BaseActivity {

    @Bind(R.id.et_name)
    BorderEditText etName;
    @Bind(R.id.et_content)
    BorderEditText etContent;
    @Bind(R.id.btn_select_file)
    Button btnSelectFile;
    @Bind(R.id.btn_add_school_work)
    Button btnAddSchoolWork;
    @Bind(R.id.tv_file_path_list)
    TextView tvFilePathList;
    @Bind(R.id.btn_final_date)
    TextView btnFinalDate;
    @Bind(R.id.btn_final_time)
    TextView btnFinalTime;

    private List<String> filePathList;

    private SimpleDateFormat sdf;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_school_work);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        filePathList = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);//一周后
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        sdf.format(new Date(calendar.getTimeInMillis()));
    }

    private void initView() {
        etContent.setText("没有作业内容");
//        tvFinalDate.setText(sdf.format(date));
    }

    private void updateFileListView() {
        tvFilePathList.setText("");
        for (int i = 0; i < filePathList.size(); i++) {
            tvFilePathList.append((i + 1) + ". " + filePathList.get(i) + "\n\n");
        }
    }

    @OnClick({R.id.btn_select_file, R.id.btn_add_school_work, R.id.btn_final_date, R.id.btn_final_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_file:
                startActivityForResult(IntentUtil.getFileIntent(), 0);
                break;
            case R.id.btn_final_date:
                showDatePickerDialog();
                break;
            case R.id.btn_final_time:
                showTimePickerDialog();
                break;
            case R.id.btn_add_school_work:
                String name = etName.getText();
                String content = etContent.getText();
                String finalDate = btnFinalDate.getText().toString();
                if (!TextUtil.isEmpty(name, content, finalDate)) {
                    if (name.length() > 36) {
                        M.t(this, "作业标题不能超过36个字符");
                    } else if (content.length() > 512) {
                        M.t(this, "作业内容不能超过512个字符");
                    } else {
                        startAddSchoolWork(name, content, finalDate);
                    }
                } else {
                    M.t(this, "请填写信息");
                }
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                btnFinalDate.setText(sdf.format(new Date(calendar.getTimeInMillis())));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择截止日期");
        dialog.show();
    }

    private void showTimePickerDialog() {

    }

    private void startAddSchoolWork(String name, String content, String finalDate) {
        M.t(this, "开始发布");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        Uri uri = data.getData();
        if (uri == null) return;

        filePathList.add(uri.getPath());
        updateFileListView();
    }
}
