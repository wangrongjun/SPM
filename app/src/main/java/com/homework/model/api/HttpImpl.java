package com.homework.model.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.constant.C;
import com.homework.constant.StateCode;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.HttpUtil;

import java.lang.reflect.Type;

/**
 * by wangrongjun on 2016/12/6.
 * 接口层的具体实现（主要是使用HttpUtil进行数据的获取）
 */
public class HttpImpl implements IHttp {

    @Override
    public Response<Student> studentLogin(String account, String password, String verifyCode, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("POST").addRequestProperty("Cookie", cookie);
        request.setOutput(C.getLoginOutput(account, password, C.ROLE_STUDENT, verifyCode));
        HttpUtil.Result r = request.request(C.getLoginUrl());
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            MsgV2 msg = new Gson().fromJson(r.result, MsgV2.class);
            Type type = new TypeToken<Student>() {
            }.getType();
            return msg.toResponse(type);
        } else {
            return new Response<>(StateCode.INTERNET_FAIL, r.result);
        }
    }

    @Override
    public Response<Teacher> teacherLogin(String account, String password, String verifyCode, String cookie) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setRequestMethod("POST").addRequestProperty("Cookie", cookie);
        request.setOutput(C.getLoginOutput(account, password, C.ROLE_STUDENT, verifyCode));
        HttpUtil.Result r = request.request(C.getLoginUrl());
        //TODO delete
        DebugUtil.printlnEntity(r);
        if (r.state == HttpUtil.OK) {
            MsgV2 msg = new Gson().fromJson(r.result, MsgV2.class);
            Type type = new TypeToken<Teacher>() {
            }.getType();
            return msg.toResponse(type);
        } else {
            return new Response<>(StateCode.INTERNET_FAIL, r.result);
        }
    }

}
