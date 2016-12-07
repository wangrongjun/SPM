package com.homework.model.api;

import com.homework.bean.SchoolWork;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.model.Response;

import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2016/12/6.
 * 后台接口层
 */
public interface IHttp {

    Response<Student> studentLogin(String account, String password, String verifyCode, String cookie);

    Response<Teacher> teacherLogin(String account, String password, String verifyCode, String cookie);

    Response<List<TeacherCourse>> studentGetCourseInfo(int studentId, String cookie);

    Response<List<Student>> studentGetClassmateList(int studentId, String cookie);

    Response<Map<Integer, Object[]>> teacherGetCourseInfoList(int teacherId, String cookie);

    Response<List<SchoolWork>> getSchoolWorkList(int teacherCourseId, String cookie);

}
