package com.homework.model.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.constant.StateCode;
import com.homework.model.Response;
import com.homework.util.P;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * by wangrongjun on 2016/12/6.
 * 接口层的具体实现（使用HttpUtil或HttpClient进行数据的获取，返回Response对象）
 */
public class HttpImpl implements IHttp {

    /**
     * @param entityType 例如，Student类型或者List<Student>类型
     */
    private <T> Response<T> parseMsgJson(String msgJson, Type entityType) {

        int code = MsgV2.CODE_ERROR_UNKNOWN;
        String message = "";
        T entity = null;

        try {
            JSONObject jsonObject = new JSONObject(msgJson);
            code = jsonObject.getInt("code");
            try {
                message = jsonObject.getJSONObject("message").toString();
            } catch (JSONException e) {//如果解析出错，则说明期望返回一个对象，但实际返回字符串
                message = jsonObject.getString("message");
            }
            entity = new Gson().fromJson(message, entityType);

            //TODO delete
            DebugUtil.println(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (code) {
            case MsgV2.CODE_OK:
                if (entityType.toString().contains("java.util.List")) {//如果指定返回的是数组
                    return new Response<>(StateCode.OK, null, entity);
                } else {
                    return new Response<>(StateCode.OK, entity, null);
                }
            case MsgV2.CODE_ERROR_NORMAL:
                return new Response<>(StateCode.ERROR_NORMAL, message);
            case MsgV2.CODE_ERROR_STORAGE:
                return new Response<>(StateCode.ERROR_STORAGE, message);
            case MsgV2.CODE_ILLEGAL:
                return new Response<>(StateCode.ERROR_ILLEGAL, message);
            case MsgV2.CODE_ERROR_UNKNOWN:
                return new Response<>(StateCode.ERROR_UNKNOWN, message);
            default:
                return new Response<>(StateCode.ERROR_UNKNOWN, message);
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
            return new Response<>(StateCode.INTERNET_FAIL, r.result);
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
            return new Response<>(StateCode.INTERNET_FAIL, r.result);
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
            return new Response<>(StateCode.INTERNET_FAIL, r.result);
        }
    }

}
