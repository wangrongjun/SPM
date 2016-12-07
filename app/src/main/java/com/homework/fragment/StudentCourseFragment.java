package com.homework.fragment;

import android.content.Context;
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
import com.homework.adapter.TeacherCourseListAdapter;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.constant.StateCode;
import com.homework.model.CallBack;
import com.homework.model.DataModel;
import com.homework.model.api.Msg;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class StudentCourseFragment extends Fragment {

    @Bind(R.id.lv_course)
    ListView lvCourse;

    private DataModel dataModel = new DataModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        getTeacherCourseListAndShow();
        return view;
    }

    private void initView() {
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID) {//如果是空适配器，即加载失败
//                    startGetCourseInfo(getActivity(), lvCourse);
                    getTeacherCourseListAndShow();
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

    //TODO delete
    private void startGetCourseInfo(final Context context, final ListView lvCourse) {
        lvCourse.setAdapter(new LoadingAdapter(context));

        AndroidHttpHelper helper = new AndroidHttpHelper(context);
        helper.addRequestProperty("Cookie", P.getCookie());
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {
            @Override
            public void onSucceed(HttpUtil.Result r) {
                Type type = new TypeToken<Msg<List<TeacherCourse>>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(context, r.result, type);
                if (pair.first) {
                    List<TeacherCourse> teacherCourseList = (List<TeacherCourse>) pair.second;
                    lvCourse.setAdapter(new TeacherCourseListAdapter(context, teacherCourseList));
                } else {
                    lvCourse.setAdapter(new NullAdapter(context, "重新获取"));
                }
            }
        });
        helper.setOnFailedListener(new AndroidHttpUtil.OnFailedListener() {
            @Override
            public void onFailed(HttpUtil.Result r) {
                lvCourse.setAdapter(new NullAdapter(context, "重新获取"));
            }
        });
        helper.request(C.getStudentCourseInfoUrl(P.getStudent().getStudentId()));
    }

    private void getTeacherCourseListAndShow() {
        dataModel.studentGetCourseInfo(new CallBack<List<TeacherCourse>>() {
            @Override
            public void onSucceed(List<TeacherCourse> data) {
                lvCourse.setAdapter(new TeacherCourseListAdapter(getActivity(), data));
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                M.t(getActivity(), errorMsg);
            }
        });
    }

}
