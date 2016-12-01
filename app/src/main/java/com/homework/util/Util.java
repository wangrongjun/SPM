package com.homework.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.homework.bean.Msg;
import com.homework.constant.C;
import com.wang.android_lib.util.M;
import com.wang.java_util.CharsetUtil;
import com.wang.java_util.DebugUtil;
import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.Pair;
import com.wang.java_util.StreamUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

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
     * 返回编码后的文件名（用于访问本地文件和下载文件到本地）
     */
    public static String getFileName(String fileUrl) {
        return CharsetUtil.encode(fileUrl);
    }

    /**
     * 下载文件
     */
    public static void download(String fileUrl, String cookie, String filePath) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);
        conn.addRequestProperty("Cookie", cookie);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(StreamUtil.toBytes(conn.getInputStream()));
        } catch (IOException e) {
            if (fos != null) {
                fos.close();
            }
            throw e;
        }
        fos.close();
    }

}
