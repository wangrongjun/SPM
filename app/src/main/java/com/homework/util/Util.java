package com.homework.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.homework.bean.Course;
import com.homework.bean.StudentClass;
import com.homework.bean.TeacherCourse;
import com.homework.constant.C;
import com.homework.model.api.Msg;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * Map<Integer, new Object[]>(键为课程编号,值Object[]第一个元素为课程名，
     * 第二个元素表示该课程下的班级编号集合)
     * {"code":0,"message":{"1":["软件工程",[1]]}}
     */
    public static Pair<List<Pair<TeacherCourse, List<StudentClass>>>, List<Pair<String, String>>>
    courseInfoMapToList(Map<Integer, Object[]> map) {

        List<Pair<TeacherCourse, List<StudentClass>>> dataList = new ArrayList<>();
        List<Pair<String, String>> showList = new ArrayList<>();

        try {
            Iterator<Map.Entry<Integer, Object[]>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Object[]> entry = iterator.next();
                Integer teacherCourseId = entry.getKey();
                String courseName = (String) entry.getValue()[0];
                List<Double> studentClassIdList = (List<Double>) entry.getValue()[1];

                TeacherCourse teacherCourse = new TeacherCourse();
                teacherCourse.setTeacherCourseId(teacherCourseId);
                teacherCourse.setCourse(new Course(courseName));

                List<StudentClass> studentClassList = new ArrayList<>();
                for (double d : studentClassIdList) {
                    //TODO classId+""改为className
                    int classId = (int) d;
                    studentClassList.add(new StudentClass(classId, classId + ""));
                }

                dataList.add(new Pair<>(teacherCourse, studentClassList));
                showList.add(new Pair<>(courseName, ""));
            }

            return new Pair<>(dataList, showList);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
