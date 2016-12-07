package com.homework.model.api;

import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.model.Response;

import java.util.List;

/**
 * by wangrongjun on 2016/12/6.
 * 接口层
 */
public interface IHttp {

    Response<Student> studentLogin(String account, String password, String verifyCode, String cookie);

    Response<Teacher> teacherLogin(String account, String password, String verifyCode, String cookie);

    Response<List<TeacherCourse>> studentGetCourseInfo(int studentId, String cookie);

}
