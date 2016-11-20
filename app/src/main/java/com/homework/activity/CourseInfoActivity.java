package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.adapter.SchoolWorkListAdapter;
import com.homework.bean.Course;
import com.homework.bean.Msg;
import com.homework.bean.SchoolWork;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.util.P;
import com.homework.util.Util;
import com.homework.view.ToolBarView;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by wangrongjun on 2016/11/1.
 */
public class CourseInfoActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.lv_school_work)
    ListView lvSchoolWork;

    private TeacherCourse teacherCourse;

    private SchoolWorkListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        initData();
        initView();
        startGetSchoolWork();
    }

    public static void start(Context context, TeacherCourse teacherCourse) {
        Intent intent = new Intent(context, CourseInfoActivity.class);
        intent.putExtra("teacherCourse", new Gson().toJson(teacherCourse));
        context.startActivity(intent);
    }

    private void initData() {
        String json = getIntent().getStringExtra("teacherCourse");
        teacherCourse = new Gson().fromJson(json, TeacherCourse.class);
    }

    private void initView() {
        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseInfoActivity.this, CourseDataFileListActivity.class));
            }
        });

        lvSchoolWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(CourseInfoActivity.this, SchoolWorkInfoActivity.class));
            }
        });

        Course course = teacherCourse.getCourse();
        if (course != null) {
            toolBar.setTitleText(course.getCourseName());
        }

    }

    private void startGetSchoolWork() {

        lvSchoolWork.setAdapter(new LoadingAdapter(this));

        String date = "";
        try {
            date = URLEncoder.encode("2000-01-01 12:00", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = C.getSchoolWorkUrl(teacherCourse.getTeacherCourseId(), date);
        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {

                Type type = new TypeToken<Msg<SchoolWork>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(CourseInfoActivity.this, r.result, type);
                if (pair.first) {
                    Object second = pair.second;
                    M.t(CourseInfoActivity.this, second + "");
                } else {
                    lvSchoolWork.setAdapter(null);
                }

            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvSchoolWork.setAdapter(null);
            }
        });
        helper.request(url);

    }

    @OnClick(R.id.btn_add_school_work)
    public void onClick() {
        TeacherAddSchoolWorkActivity.start(this, teacherCourse);
    }
}
