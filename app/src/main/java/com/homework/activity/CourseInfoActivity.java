package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;
import com.wang.test.ISort;
import com.wang.test.SortHelper;

import java.lang.reflect.Type;
import java.util.List;

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
    @Bind(R.id.btn_add_school_work)
    LinearLayout btnAddSchoolWork;

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

        if (P.getRole() == C.ROLE_STUDENT) {
            btnAddSchoolWork.setVisibility(View.GONE);
        }

        toolBar.setOnBtnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseInfoActivity.this, CourseDataFileListActivity.class));
            }
        });

        lvSchoolWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != NullAdapter.NULL_ADAPTER_ID && id != LoadingAdapter.LOADING_ADAPTER_ID) {
                    SchoolWork schoolWork = adapter.getSchoolWorkList().get(position);
                    SchoolWorkInfoActivity.start(CourseInfoActivity.this, schoolWork);
                }
            }
        });

        Course course = teacherCourse.getCourse();
        if (course != null) {
            toolBar.setTitleText(course.getCourseName() + " 所有作业");
        }

    }

    private void startGetSchoolWork() {

        lvSchoolWork.setAdapter(new LoadingAdapter(this));

        String url = C.getSchoolWorkUrl(teacherCourse.getTeacherCourseId(), "2000-01-01 12:00");
        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {

                Type type = new TypeToken<Msg<List<SchoolWork>>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(CourseInfoActivity.this, r.result, type);
                if (pair.first) {
                    List<SchoolWork> schoolWorkList = (List<SchoolWork>) pair.second;
                    if (schoolWorkList != null && schoolWorkList.size() > 0) {
                        showSchoolWorkList(schoolWorkList);
                    } else {
                        lvSchoolWork.setAdapter(new NullAdapter(CourseInfoActivity.this, "暂无作业"));
                    }
                } else {//因为Util.handleMsg会在错误情况下进行提示，所以这里不用提示了
                    lvSchoolWork.setAdapter(adapter);
                }

            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvSchoolWork.setAdapter(adapter);
            }
        });
        helper.request(url);

    }

    private void showSchoolWorkList(List<SchoolWork> schoolWorkList) {
        SortHelper.sortMerge(schoolWorkList, new ISort<SchoolWork>() {
            @Override
            public int compare(SchoolWork schoolWork1, SchoolWork schoolWork2) {
                long time1 = schoolWork1.getCreateDate().getTime();
                long time2 = schoolWork2.getCreateDate().getTime();
                return time1 <= time2 ? 1 : -1;
            }
        });
        adapter = new SchoolWorkListAdapter(this, schoolWorkList);
        lvSchoolWork.setAdapter(adapter);
    }

    @OnClick(R.id.btn_add_school_work)
    public void onClick() {
        TeacherAddSchoolWorkActivity.start(this, teacherCourse, 3333, 44);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3333 && resultCode == 44) {
            startGetSchoolWork();
        }
    }

}
