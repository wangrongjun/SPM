package com.homework.model.api;

import android.graphics.Bitmap;

import com.homework.bean.CommitSchoolWork;
import com.homework.bean.SchoolWork;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.model.Response;
import com.wang.java_util.Pair;

import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2016/12/6.
 * 后台接口层
 */
public interface IHttp {

    /**
     * Pair<String,Bitmap>：第一项为服务器返回的Cookie，第二项为服务器返回的验证码图片
     */
    Response<Pair<String, Bitmap>> getVerifyCodeBitmap();

    Response<Student> studentLogin(String account, String password, String verifyCode, String cookie);

    Response<Teacher> teacherLogin(String account, String password, String verifyCode, String cookie);

    Response<List<TeacherCourse>> studentGetCourseInfo(int studentId, String cookie);

    Response<List<Student>> studentGetClassmateList(int studentId, String cookie);

    Response<Map<Integer, Object[]>> teacherGetCourseInfoList(int teacherId, String cookie);

    Response<List<SchoolWork>> getSchoolWorkList(int teacherCourseId, String cookie);

    Response<CommitSchoolWork> getCommitSchoolWork(int schoolWorkId, String cookie);

}
