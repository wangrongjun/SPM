package com.homework.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.homework.bean.Msg;
import com.homework.constant.C;
import com.wang.android_lib.util.M;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.Pair;
import com.wang.java_util.TextUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.lang.reflect.Type;

/**
 * by wangrongjun on 2016/10/31.
 */
public class Util {
    /**
     * 注意，第一个返回值为false默认已经弹出提示，无需再次再提示。
     * 使用样例：
     * <p/>
     * Type type = new TypeToken<Msg<List<Student>>>() {
     * }.getType();
     * Pair<Boolean, Object> pair = Util.handleMsg(getActivity(), r.result, type);
     * if (pair.first) {
     * List<Student> studentList = (List<Student>) pair.second;
     * lvStudent.setAdapter(new StudentListAdapter(getActivity(), studentList));
     * } else {
     * M.t(getActivity(), pair.second + "");
     * }
     */
    public static Pair<Boolean, Object> handleMsg(Context context, String result, Type type) {

        DebugUtil.println(JsonFormatUtil.formatJson(result));

        try {
            Msg msg = new Gson().fromJson(result, Msg.class);
            switch (msg.getCode()) {
                case C.CODE_OK:
                    msg = new Gson().fromJson(result, type);
                    return new Pair<>(true, msg.getMessage());
                case C.CODE_ERROR_NORMAL:
                case C.CODE_ERROR_STORAGE:
                case C.CODE_ILLEGAL:
                case C.CODE_ERROR_UNKNOWN:
                    M.t(context, msg.getMessage() + "");
                    return new Pair<>(false, null);
                default:
                    M.t(context, "没有该状态码：" + msg.getCode());
                    return new Pair<>(false, null);
            }

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            M.t(context, "程序出错，json解析失败");
            return new Pair<>(false, null);
        }

    }

    /**
     * 教师发布课程作业
     * http://blog.csdn.net/Just_szl/article/details/7659347
     *
     * @return HttpStatus类的状态码 + 服务器返回的结果字符串
     */
    public static Pair<Integer, String> addSchoolWork(int teacherCourseId,
                                                      String name,
                                                      String content,
                                                      String finalDate,
                                                      String extraFilePath,
                                                      String cookie) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(C.addSchoolWorkUrl());

        try {
            MultipartEntity requestEntity = new MultipartEntity();
            requestEntity.addPart("teacherCourse.teacherCourseId", new StringBody(teacherCourseId + ""));
            requestEntity.addPart("name", new StringBody(name));
            if (TextUtil.isEmpty(content)) {
                content = "null";
            }
            requestEntity.addPart("content", new StringBody(content));
            requestEntity.addPart("finalDate", new StringBody(finalDate));
            if (!TextUtil.isEmpty(extraFilePath)) {
                requestEntity.addPart("extraFile", new FileBody(new File(extraFilePath)));
            }

            httpPost.addHeader("Cookie", cookie);
            httpPost.setEntity(requestEntity);
            //TODO 若文件大小在100k以上，报错：IllegalArgumentException: can't serialize to outbuffer
            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);//httpclient的工具类读取返回数据

            return new Pair<>(statusCode, result);

        } catch (Exception e) {
            e.printStackTrace();
            httpClient.getConnectionManager().shutdown();
            throw e;
        }

    }

}
