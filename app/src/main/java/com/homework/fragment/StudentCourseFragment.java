package com.homework.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.homework.R;
import com.homework.activity.CourseInfoActivity;
import com.homework.bean.TeacherCourse;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.adapter.NullAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class StudentCourseFragment extends Fragment {

    @Bind(R.id.lv_course)
    ListView lvCourse;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        StudentClassFragmentP.startGetCourseInfo(getActivity(), lvCourse);
        return view;
    }

    private void initView() {
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID) {//如果是空适配器，即加载失败
                    StudentClassFragmentP.startGetCourseInfo(getActivity(), lvCourse);
                } else if (id != LoadingAdapter.LOADING_ADAPTER_ID) {//若当前不是查询中，即已加载完成
                    TeacherCourse tc = (TeacherCourse) lvCourse.getAdapter().getItem(position);
                    CourseInfoActivity.start(getActivity(), tc);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
