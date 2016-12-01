package com.homework.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.Msg;
import com.homework.bean.TeacherCourse;
import com.homework.util.HttpUtil;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.IntentUtil;
import com.wang.android_lib.util.M;
import com.wang.android_lib.util.PathUtil;
import com.wang.android_lib.view.BorderEditText;
import com.wang.java_util.Pair;
import com.wang.java_util.TextUtil;

import org.apache.http.HttpStatus;

import java.lang.reflect.Type;
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
    @Bind(R.id.tv_teacher_course_name)
    TextView tvTeacherCourseName;

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

    private static int resultCode;

    public static void start(Activity activity, TeacherCourse teacherCourse,
                             int requestCode, int resultCode) {
        TeacherAddSchoolWorkActivity.resultCode = resultCode;
        Intent intent = new Intent(activity, TeacherAddSchoolWorkActivity.class);
        intent.putExtra("teacherCourse", new Gson().toJson(teacherCourse));
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 初始化teacherCourse和calendar
     */
    private void initData() {
        filePathList = new ArrayList<>();

        String teacherCourseJson = getIntent().getStringExtra("teacherCourse");
        teacherCourse = new Gson().fromJson(teacherCourseJson, TeacherCourse.class);
        if (teacherCourse == null) {
            teacherCourse = new TeacherCourse();
        }
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);//一周后
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);//默认的截止时间是一周后的中午12点
    }

    private void initView() {
        tvTeacherCourseName.setText(teacherCourse.getCourse().getCourseName());
        etContent.setText("没有作业内容");
        btnFinalDate.setText(sdfDate.format(new Date(calendar.getTimeInMillis())));
        btnFinalTime.setText(sdfTime.format(new Date(calendar.getTimeInMillis())));
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

    private static final int ERROR = 35344;

    private void startAddSchoolWork(final String name, final String content) {

        DialogUtil.showProgressDialog(this, "正在发布");

        new AsyncTask<Void, Void, Pair<Integer, String>>() {

            @Override
            protected Pair<Integer, String> doInBackground(Void... params) {
                try {
                    return HttpUtil.addSchoolWork(
                            teacherCourse.getTeacherCourseId(),
                            name,
                            content,
                            sdfDate.format(new Date(calendar.getTimeInMillis())) + " " +
                                    sdfTime.format(new Date(calendar.getTimeInMillis())),
                            filePathList,
                            P.getCookie()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Pair<>(ERROR, e.toString());
                }
            }

            @Override
            protected void onPostExecute(Pair<Integer, String> scResultPair) {

                DialogUtil.cancelProgressDialog();

                int code = scResultPair.first;
                String result = scResultPair.second;
                switch (code) {
                    case ERROR:
                        M.t(TeacherAddSchoolWorkActivity.this, "网络异常，" + result);
                        break;
                    default:
                        M.t(TeacherAddSchoolWorkActivity.this, "服务器异常，" + result);
                        break;
                    case HttpStatus.SC_OK:
                        handleResult(result);
                        break;
                }
            }

        }.execute();
    }

    private void handleResult(String result) {
        Type type = new TypeToken<Msg<String>>() {
        }.getType();
        Pair<Boolean, Object> booleanObjectPair = Util.handleMsg(this, result, type);
        if (booleanObjectPair.first) {
            M.t(this, "发布成功，" + booleanObjectPair.second);
            setResult(resultCode);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        Uri uri = data.getData();
        if (uri == null) return;

        filePathList.add(PathUtil.getRealFilePath(this, uri));
        String s = "";
        for (String path : filePathList) {
            s += path + "\n";
        }
        tvFilePathList.setText(s);
    }

}
