package com.spm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spm.R;
import com.spm.activity.CourseIndustryInfoActivity;
import com.spm.activity.CourseTeachDocumentActivity;
import com.spm.activity.CourseVideoListActivity;
import com.wang.android_lib.util.M;
import com.wang.java_util.StreamUtil;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class MainCourseFragment extends Fragment {

    @Bind(R.id.tv_course_introduction)
    TextView tvCourseIntroduction;
    @Bind(R.id.tv_teach_plan)
    TextView tvTeachPlan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        try {
            String courseIntroduction = StreamUtil.readInputStream(
                    getActivity().getAssets().open("course_introduction.txt"));
            String teachPlan = StreamUtil.readInputStream(
                    getActivity().getAssets().open("teach_plan.txt"));
            tvCourseIntroduction.setText(courseIntroduction);
            tvTeachPlan.setText(teachPlan);
        } catch (IOException e) {
            e.printStackTrace();
            M.t(getActivity(), e.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_teach_document, R.id.btn_industry_info, R.id.btn_course_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_teach_document:
                startActivity(new Intent(getActivity(), CourseTeachDocumentActivity.class));
                break;
            case R.id.btn_industry_info:
                startActivity(new Intent(getActivity(), CourseIndustryInfoActivity.class));
                break;
            case R.id.btn_course_video:
                startActivity(new Intent(getActivity(), CourseVideoListActivity.class));
                break;
        }
    }
}
