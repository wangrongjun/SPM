package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.adapter.SchoolWorkListAdapter;
import com.homework.bean.SchoolWork;
import com.homework.bean.TeacherCourse;
import com.homework.view.ToolBarView;
import com.wang.android_lib.util.M;
import com.wang.java_program.video_download.bean.Course;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class CourseInfoActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    ToolBarView toolBar;
    @Bind(R.id.tv_course_hint)
    TextView tvCourseHint;
    @Bind(R.id.tv_chapter_name)
    TextView tvChapterName;
    @Bind(R.id.lv_homework)
    ListView lvHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ButterKnife.bind(this);
        String json = getIntent().getStringExtra("teacherCourse");
        TeacherCourse teacherCourse = new Gson().fromJson(json, TeacherCourse.class);
        initView(teacherCourse);
    }

    public static void start(Context context, TeacherCourse teacherCourse) {
        Intent intent = new Intent(context, CourseInfoActivity.class);
        intent.putExtra("teacherCourse", new Gson().toJson(teacherCourse));
        context.startActivity(intent);
    }

    private void initView(TeacherCourse teacherCourse) {
        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseInfoActivity.this, CourseDataFileListActivity.class));
            }
        });

        lvHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(CourseInfoActivity.this, SchoolWorkInfoActivity.class));
            }
        });

        if (teacherCourse != null) {
            Course course = teacherCourse.getCourse();
            if (course != null) {
                toolBar.setTitleText(course.getCourseName());
                tvCourseHint.setText(course.getCourseHint());
                List<String> chapterNames = course.getChapterNames();
                if (chapterNames != null && chapterNames.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (String s : chapterNames) {
                        builder.append(s).append("\n");
                    }
                    tvChapterName.setText(builder.toString());
                }
            }

            Set<SchoolWork> schoolWorks = teacherCourse.getSchoolWorks();
            if (schoolWorks != null) {
                List<SchoolWork> schoolWorkList = new ArrayList<>();
                Iterator<SchoolWork> iterator = schoolWorks.iterator();
                while (iterator.hasNext()) {
                    schoolWorkList.add(iterator.next());
                }
//                lvHomework.setAdapter(new SchoolWorkListAdapter(this, schoolWorkList));
                lvHomework.setAdapter(new SchoolWorkListAdapter(this));
            } else {
                M.t(this, "暂无作业");
                lvHomework.setAdapter(new SchoolWorkListAdapter(this));
            }
        }

    }
}
