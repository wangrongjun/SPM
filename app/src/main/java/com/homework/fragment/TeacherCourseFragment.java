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
import com.homework.adapter.DefaultListAdapter;
import com.homework.bean.StudentClass;
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
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/7.
 */
public class TeacherCourseFragment extends Fragment {

    @Bind(R.id.lv_course)
    ListView lvCourse;

    private DataModel dataModel = new DataModel();
    private List<Pair<TeacherCourse, List<StudentClass>>> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_course, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        lvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == NullAdapter.NULL_ADAPTER_ID) {//如果是空适配器，即加载失败
                    getCourseInfoListAndShow();
                } else if (id != LoadingAdapter.LOADING_ADAPTER_ID) {//若当前不是查询中，即已加载完成
                    TeacherCourse tc = dataList.get(position).first;
                    CourseInfoActivity.start(getActivity(), tc);
                }
            }
        });
    }

    public void getCourseInfoListAndShow() {
        lvCourse.setAdapter(new LoadingAdapter(getActivity()));

        dataModel.teacherGetCourseInfoList(new CallBack<Map<Integer, Object[]>>() {
            @Override
            public void onSucceed(Map<Integer, Object[]> data) {
                showListData(data);
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                lvCourse.setAdapter(new NullAdapter(getActivity(), "重新获取"));
                M.t(getActivity(), errorMsg);
            }
        });
    }

    private void showListData(Map<Integer, Object[]> data) {
        Pair<List<Pair<TeacherCourse, List<StudentClass>>>, List<Pair<String, String>>> pair =
                Util.courseInfoMapToList(data);
        if (pair == null) {
            M.t(getActivity(), "程序异常，课程列表获取失败");
        } else {
            dataList = pair.first;
            List<Pair<String, String>> showList = pair.second;
            lvCourse.setAdapter(new DefaultListAdapter(getActivity(), showList));
        }
    }

    //TODO delete
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
        helper.request(C.teacherGetCourseInfoListUrl(P.getTeacher().getTeacherId()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
