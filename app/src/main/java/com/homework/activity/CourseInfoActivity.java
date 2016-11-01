package com.homework.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.bean.TeacherCourse;
import com.homework.view.ToolBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class CourseInfoActivity extends Activity {

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
        initView();

    }

    public static void start(Context context, TeacherCourse teacherCourse) {
        Intent intent = new Intent(context, CourseInfoActivity.class);
        intent.putExtra("teacherCourse", new Gson().toJson(teacherCourse));
        context.startActivity(intent);
    }

    private void initView() {
        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseInfoActivity.this, CourseDataFileListActivity.class));
            }
        });
//        toolBar.setTitleText();
    }
}
