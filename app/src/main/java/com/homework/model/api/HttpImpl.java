package com.homework.model.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.homework.bean.CommitSchoolWork;
import com.homework.bean.SchoolWork;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.constant.StateCode;
import com.homework.model.Response;
import com.homework.util.P;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2016/12/6.
 * 后台接口层的具体实现（使用HttpUtil或HttpClient进行数据的获取，返回Response对象）
 */
public class HttpImpl implements IHttp {

    /**
     * 把后台返回的json（Msg对象的json）转化为Response对象
     *
     * @param entityType 例如，Student类型或者List<Student>类型
     */
    private <T> Response<T> parseMsgJson(String msgJson, Type entityType) {

        Response<T> response;

        int code;
        String message;
        T entity = null;

        try {
            JSONObject jsonObject = new JSONObject(msgJson);
            code = jsonObject.getInt("code");
            try {
                message = jsonObject.getJSONObject("message").toString();
                entity = new Gson().fromJson(message, entityType);
            } catch (JSONException e) {//如果解析出错，则说明返回对象数组或字符串
                try {
                    message = jsonObject.getJSONArray("message").toString();
                    entity = new Gson().fromJson(message, entityType);
                } catch (JSONException e1) {//如果解析出错，则说明期望返回一个对象，但实际返回字符串
                    // （如登录时，期望返回Student对象，但如果失败，实际会返回失败提示信息）
                    message = jsonObject.getString("message");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            code = MsgV2.CODE_ERROR_UNKNOWN;
            message = "JSON解析出错，" + e.toString();
        }

        switch (code) {
            case MsgV2.CODE_OK:
                if (entity != null) {
                    if (entityType.toString().contains("java.util.List")) {//如果指定返回的是数组
                        response = new Response<>(StateCode.OK, null, entity);
                    } else {
                        response = new Response<>(StateCode.OK, entity, null);
                    }
                } else {
                    response = new Response<>(StateCode.OK, message);
                }
                break;
            case MsgV2.CODE_ERROR_NORMAL:
                response = new Response<>(StateCode.ERROR_NORMAL, message);
                break;
            case MsgV2.CODE_ERROR_STORAGE:
                response = new Response<>(StateCode.ERROR_STORAGE, message);
                break;
            case MsgV2.CODE_ILLEGAL:
                response = new Response<>(StateCode.ERROR_ILLEGAL, message);
                break;
            case MsgV2.CODE_ERROR_UNKNOWN:
                response = new Response<>(StateCode.ERROR_UNKNOWN, message);
                break;
            default:
                response = new Response<>(StateCode.ERROR_UNKNOWN, message);
                break;
        }

        //TODO delete
        DebugUtil.printlnEntity(response);

        return response;
    }

    @Override
    public Response<Pair<String, Bitmap>> getVerifyCodeBitmap() {
        try {
            HttpURLConnection conn =
                    (HttpURLConnection) new URL(C.getVerifyUrl()).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            String cookie = conn.getHeaderField("Set-Cookie");
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());

            return new Response<>(StateCode.OK, new Pair<>(cookie, bitmap), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new Response<>(StateCode.INTERNET_UNABLE, e.toString());
        }
    }

    @Override
    public Response<Student> studentLogin(String account, String password,
                                          String verifyCode, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("POST").addRequestProperty("Cookie", cookie);
        request.setOutput(C.getLoginOutput(account, password, C.ROLE_STUDENT, verifyCode));
        HttpUtil.Result r = request.request(C.getLoginUrl());
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<Student>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<Teacher> teacherLogin(String account, String password,
                                          String verifyCode, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("POST").addRequestProperty("Cookie", cookie);
        request.setOutput(C.getLoginOutput(account, password, C.ROLE_TEACHER, verifyCode));
        HttpUtil.Result r = request.request(C.getLoginUrl());
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<Teacher>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<List<TeacherCourse>> studentGetCourseInfo(int studentId, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("GET").addRequestProperty("Cookie", cookie);
        HttpUtil.Result r = request.request(C.getStudentCourseInfoUrl(P.getStudent().getStudentId()));
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<List<TeacherCourse>>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<List<Student>> studentGetClassmateList(int studentId, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("GET").addRequestProperty("Cookie", cookie);
        HttpUtil.Result r = request.request(C.getClassmateInfoUrl(studentId));
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<List<Student>>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<Map<Integer, Object[]>> teacherGetCourseInfoList(int teacherId, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("GET").addRequestProperty("Cookie", cookie);
        HttpUtil.Result r = request.request(C.teacherGetCourseInfoListUrl(teacherId));
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<Map<Integer, Object[]>>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<List<SchoolWork>> getSchoolWorkList(int teacherCourseId, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("GET").addRequestProperty("Cookie", cookie);
        HttpUtil.Result r = request.request(C.getSchoolWorkUrl(teacherCourseId, "2000-01-01 12:00"));
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            Type type = new TypeToken<List<SchoolWork>>() {
            }.getType();
            return parseMsgJson(r.result, type);
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

    @Override
    public Response<CommitSchoolWork> getCommitSchoolWork(int schoolWorkId, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("GET").addRequestProperty("Cookie", cookie);
        HttpUtil.Result r = request.request(C.studentGetCommitSchoolWorkUrl(schoolWorkId));
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
//            TODO 叫后台返回Msg<CommitSchoolWork>，而不是CommitSchoolWork
            try {
                CommitSchoolWork commitSchoolWork =
                        new Gson().fromJson(r.result, CommitSchoolWork.class);
                return new Response<>(StateCode.OK, commitSchoolWork, null);

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                Type type = new TypeToken<CommitSchoolWork>() {
                }.getType();
                return parseMsgJson(r.result, type);
            }
        } else {
            return new Response<>(StateCode.INTERNET_UNABLE, r.result);
        }
    }

}
