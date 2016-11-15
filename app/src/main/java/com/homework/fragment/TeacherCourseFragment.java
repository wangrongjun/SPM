package com.homework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.CourseInfoActivity;
import com.homework.bean.Course;
import com.homework.bean.Msg;
import com.homework.bean.StudentClass;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.util.DefaultListAdapter;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.android_lib.util.M;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/7.
 */
public class TeacherCourseFragment extends Fragment {

    @Bind(R.id.lv_course)
    ListView lvCourse;

    private List<Pair<TeacherCourse, List<StudentClass>>> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume-------------------------------");
    }

    private void initView() {
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID) {//如果是空适配器，即加载失败
                    startGetCourseInfo();
                } else if (id != LoadingAdapter.LOADING_ADAPTER_ID) {//若当前不是查询中，即已加载完成
                    TeacherCourse tc = (TeacherCourse) lvCourse.getAdapter().getItem(position);
                    CourseInfoActivity.start(getActivity(), tc);
                }
            }
        });
    }

    public void startGetCourseInfo() {
        lvCourse.setAdapter(new LoadingAdapter(getActivity()));

        AndroidHttpHelper helper = new AndroidHttpHelper(getActivity());
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {
                Type type = new TypeToken<Msg<Map<Integer, Object[]>>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(getActivity(), r.result, type);
                if (pair.first) {
                    Map<Integer, Object[]> map = (Map<Integer, Object[]>) pair.second;
                    showListData(map);
                } else {
                    M.t(getActivity(), pair.second + "");
                    lvCourse.setAdapter(new NullAdapter(getActivity(), "重新获取"));
                }
            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvCourse.setAdapter(new NullAdapter(getActivity(), "重新获取"));
            }
        });
        helper.request(C.getTeacherClassListUrl(P.getTeacher().getTeacherId()));
    }

    /**
     * Map<Integer, new Object[]>(键为课程编号,值Object[]第一个元素为课程名，
     * 第二个元素表示该课程下的班级编号集合)
     * {"code":0,"message":{"1":["软件工程",[1]]}}
     */
    private void showListData(Map<Integer, Object[]> map) {
        dataList = new ArrayList<>();

        List<Pair<String, String>> showList = new ArrayList<>();

        try {
            Iterator<Map.Entry<Integer, Object[]>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Object[]> entry = iterator.next();
                Integer teacherCourseId = entry.getKey();
                String courseName = (String) entry.getValue()[0];
                List<Double> studentClassIdList = (List<Double>) entry.getValue()[1];

                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setTeacherCourseId(teacherCourseId);
                teacherCourse.setCourse(new Course(courseName));

                List<StudentClass> studentClassList = new ArrayList<>();
                for (double d : studentClassIdList) {
                    //TODO classId+""改为className
                    int classId = (int) d;
                    studentClassList.add(new StudentClass(classId, classId + ""));
                }

                dataList.add(new Pair<>(teacherCourse, studentClassList));

                showList.add(new Pair<>(courseName, ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            M.t(getActivity(), "程序异常，" + e.toString());
        }

        lvCourse.setAdapter(new DefaultListAdapter(getActivity(), showList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
