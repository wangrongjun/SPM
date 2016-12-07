package com.homework.model;

import android.os.AsyncTask;

import com.homework.bean.SchoolWork;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.constant.StateCode;
import com.homework.model.api.HttpImpl;
import com.homework.model.api.IHttp;
import com.homework.util.P;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.TextUtil;

import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2016/12/6.
 * 业务逻辑层。
 * 负责对输入数据进行边界检查，合法性检查，启动新线程异步访问后台，根据后台接口层返回的状态码返回提示信息或数据。
 */
public class DataModel {

    public static final String INTERNET_UNABLE_HINT = "服务器连接失败";

    private IHttp iHttp;

    public DataModel() {
        iHttp = new HttpImpl();
    }

    public void setCookie(String cookie) {
        P.setCookie(cookie);
    }

    public String getCookie() {
        return P.getCookie();
    }

    public Student getStudent() {
        return P.getStudent();
    }

    public Teacher getTeacher() {
        return P.getTeacher();
    }

    public int getRole() {
        return P.getRole();
    }

    public void studentLogin(final String account, final String password, final String verifyCode,
                             final CallBack<Student> callBack) {
//        边界、合法性检查
        if (TextUtil.isEmpty(account, password, verifyCode)) {
            callBack.onFailure(StateCode.PARAM_NULL, "请填写完整信息");
            return;
        }

        new AsyncTask<Void, Void, Response<Student>>() {
            @Override
            protected Response<Student> doInBackground(Void... params) {
                return iHttp.studentLogin(account, password, verifyCode, getCookie());
            }

            @Override
            protected void onPostExecute(Response<Student> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        Student student = response.getEntity();
                        P.setStudent(student);
                        callBack.onSucceed(student);
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

    public void teacherLogin(final String account, final String password, final String verifyCode,
                             final CallBack<Teacher> callBack) {
//        边界、合法性检查
        if (TextUtil.isEmpty(account, password, verifyCode)) {
            callBack.onFailure(StateCode.PARAM_NULL, "请填写完整信息");
            return;
        }

        new AsyncTask<Void, Void, Response<Teacher>>() {
            @Override
            protected Response<Teacher> doInBackground(Void... params) {
                return iHttp.teacherLogin(account, password, verifyCode, getCookie());
            }

            @Override
            protected void onPostExecute(Response<Teacher> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        Teacher teacher = response.getEntity();
                        P.setTeacher(teacher);
                        callBack.onSucceed(teacher);
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

    public void studentGetCourseInfo(final CallBack<List<TeacherCourse>> callBack) {

        new AsyncTask<Void, Void, Response<List<TeacherCourse>>>() {
            @Override
            protected Response<List<TeacherCourse>> doInBackground(Void... params) {
                return iHttp.studentGetCourseInfo(getStudent().getStudentId(), getCookie());
            }

            @Override
            protected void onPostExecute(Response<List<TeacherCourse>> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        callBack.onSucceed(response.getEntityList());
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

    public void studentGetClassmateList(final CallBack<List<Student>> callBack) {

        new AsyncTask<Void, Void, Response<List<Student>>>() {
            @Override
            protected Response<List<Student>> doInBackground(Void... params) {
                return iHttp.studentGetClassmateList(getStudent().getStudentId(), getCookie());
            }

            @Override
            protected void onPostExecute(Response<List<Student>> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        callBack.onSucceed(response.getEntityList());
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

    public void teacherGetCourseInfoList(final CallBack<Map<Integer, Object[]>> callBack) {

        new AsyncTask<Void, Void, Response<Map<Integer, Object[]>>>() {
            @Override
            protected Response<Map<Integer, Object[]>> doInBackground(Void... params) {
                return iHttp.teacherGetCourseInfoList(getTeacher().getTeacherId(), getCookie());
            }

            @Override
            protected void onPostExecute(Response<Map<Integer, Object[]>> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        callBack.onSucceed(response.getEntity());
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

    public void getSchoolWorkList(final int teacherCourseId,
                                  final CallBack<List<SchoolWork>> callBack) {

        new AsyncTask<Void, Void, Response<List<SchoolWork>>>() {
            @Override
            protected Response<List<SchoolWork>> doInBackground(Void... params) {
                return iHttp.getSchoolWorkList(teacherCourseId, getCookie());
            }

            @Override
            protected void onPostExecute(Response<List<SchoolWork>> response) {
                //TODO delete
                DebugUtil.printlnEntity(response);

                switch (response.getStateCode()) {
                    case OK:
                        callBack.onSucceed(response.getEntityList());
                        break;
                    case INTERNET_UNABLE:
                        callBack.onFailure(StateCode.INTERNET_UNABLE, INTERNET_UNABLE_HINT);
                        break;
                    default:
                        callBack.onFailure(response.getStateCode(), response.getMessage());
                        break;
                }
            }
        }.execute();

    }

}
