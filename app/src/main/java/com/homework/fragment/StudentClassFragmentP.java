package com.homework.fragment;

import android.content.Context;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.homework.adapter.TeacherCourseListAdapter;
import com.homework.model.api.Msg;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.android_lib.adapter.LoadingAdapter;
import com.wang.android_lib.adapter.NullAdapter;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import java.lang.reflect.Type;
import java.util.List;

/**
 * by wangrongjun on 2016/12/5.
 */
public class StudentClassFragmentP {

    public static void startGetCourseInfo(final Context context, final ListView lvCourse) {
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

}
