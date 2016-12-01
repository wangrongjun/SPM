package com.homework.util;

import com.homework.constant.C;
import com.wang.android_lib.util.NotificationUtil;
import com.wang.java_util.GsonUtil;
import com.wang.java_util.Pair;
import com.wang.java_util.TextUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.List;

/**
 * by wangrongjun on 2016/11/25.
 */
public class HttpUtil {

    public static Pair<Integer, String> studentCommitSchoolWork(int schoolWorkId,
                                                                String remark,
                                                                List<String> filePathList) {
        HttpClient httpClient = new DefaultHttpClient();
        Pair<Integer, String> stateMessage;

        String content = "schoolWorkId: " + schoolWorkId + "\nremark: " + remark +
                "\nextraFiles:\n" + GsonUtil.formatJson(filePathList);
        NotificationUtil.showNotification(P.context, 12345, "studentCommit-request", content);

        try {
            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("schoolWorkId", new StringBody(schoolWorkId + ""));
            if (!TextUtil.isEmpty(remark)) {
                multipartEntity.addPart("remark", new StringBody(remark));
            }
            if (filePathList != null && filePathList.size() > 0) {
                for (String filePath : filePathList) {
                    multipartEntity.addPart("extraFile", new FileBody(new File(filePath)));
                }
            }

            HttpPost httpPost = new HttpPost(C.studentCommitSchoolWorkUrl());
            httpPost.addHeader("Cookie", P.getCookie());
            httpPost.setEntity(multipartEntity);
            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            stateMessage = new Pair<>(statusCode, result);

        } catch (Exception e) {
            e.printStackTrace();
            stateMessage = new Pair<>(HttpStatus.SC_NOT_FOUND, e.toString());

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        content = GsonUtil.formatJson(stateMessage);
        NotificationUtil.showNotification(P.context, 123456, "studentCommit-response", content);

        return stateMessage;
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
                                                      List<String> extraFilePath,
                                                      String cookie) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(C.teacherAddSchoolWorkUrl());

        try {
            MultipartEntity requestEntity = new MultipartEntity();
            requestEntity.addPart("teacherCourse.teacherCourseId", new StringBody(teacherCourseId + ""));
            requestEntity.addPart("name", new StringBody(name));
            if (TextUtil.isEmpty(content)) {
                content = "null";
            }
            requestEntity.addPart("content", new StringBody(content));
            requestEntity.addPart("finalDate", new StringBody(finalDate));
            for (String filePath : extraFilePath) {
                if (!TextUtil.isEmpty(filePath)) {
                    requestEntity.addPart("extraFile", new FileBody(new File(filePath)));
                }
            }

            httpPost.addHeader("Cookie", cookie);
            httpPost.setEntity(requestEntity);
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
