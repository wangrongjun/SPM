package com.homework;

import com.homework.constant.C;
import com.wang.java_util.FileUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.StreamUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * by wangrongjun on 2016/11/7.
 */
public class Test {

    public static final String cookiePath = "E:/cookie.txt";

    public static void main(String[] a) throws IOException {
//        verify();
        login("6154");
    }

    private static void login(String verifyCode) throws IOException {
        String cookie = FileUtil.read(cookiePath);
        HttpUtil.Result r = loginTeacher("teacher1", "123456", verifyCode, cookie);
        String s = GsonUtil.formatJson(r);
        System.out.println(s);
        FileUtil.write(s, "E:/login.txt");
    }

    public static HttpUtil.Result loginTeacher(String account, String password, String verifyCode, String cookie) {
        String output = C.getLoginOutput(account, password, C.ROLE_TEACHER, verifyCode);
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setOutput(output).setRequestMethod("POST").addRequestProperty("Cookie", cookie);
        return request.request(C.getLoginUrl());
    }

    public static void verify() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(C.getVerifyUrl()).openConnection();
        FileOutputStream fos = new FileOutputStream("E:/verify.jpg");
        fos.write(StreamUtil.toBytes(conn.getInputStream()));
        fos.close();
        FileUtil.write(conn.getHeaderField("Set-Cookie"), cookiePath);
    }

}
