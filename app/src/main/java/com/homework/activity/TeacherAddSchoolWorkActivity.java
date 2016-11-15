package com.homework.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.TeacherCourse;
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
    @Bind(R.id.tv_file_path_list)
    TextView tvFilePathList;
    @Bind(R.id.btn_final_date)
    TextView btnFinalDate;
    @Bind(R.id.btn_final_time)
    TextView btnFinalTime;

    private List<String> filePathList;

    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private Calendar calendar;

    private TeacherCourse teacherCourse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_school_work);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    public static void start(Context context, TeacherCourse teacherCourse) {
        Intent intent = new Intent(context, TeacherAddSchoolWorkActivity.class);
        intent.putExtra("teacherCourse", new Gson().toJson(teacherCourse));
        context.startActivity(intent);
    }

    private void initData() {
        String teacherCourseJson = getIntent().getStringExtra("teacherCourse");
        teacherCourse = new Gson().fromJson(teacherCourseJson, TeacherCourse.class);
        if (teacherCourse == null) {
            teacherCourse = new TeacherCourse();
        }
        filePathList = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);//一周后
    }

    private void initView() {
        etContent.setText("没有作业内容");
        btnFinalDate.setText(sdfDate.format(new Date(calendar.getTimeInMillis())));
        btnFinalTime.setText(sdfTime.format(new Date(calendar.getTimeInMillis())));
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
                if (!TextUtil.isEmpty(name, content)) {
                    if (name.length() > 36) {
                        M.t(this, "作业标题不能超过36个字符");
                    } else if (content.length() > 512) {
                        M.t(this, "作业内容不能超过512个字符");
                    } else {
                        startAddSchoolWork(name, content);
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
                btnFinalDate.setText(sdfDate.format(new Date(calendar.getTimeInMillis())));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择截止日期");
        dialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                btnFinalTime.setText(sdfTime.format(new Date(calendar.getTimeInMillis())));
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.setTitle("请选择截止时间");
        dialog.show();
    }

    private void startAddSchoolWork(String name, String content) {
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
