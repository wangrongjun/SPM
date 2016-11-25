package com.homework.util;

import com.homework.constant.C;
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

        return stateMessage;
    }

}
