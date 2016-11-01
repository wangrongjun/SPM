package com.homework.constant;

/**
 * by 王荣俊 on 2016/10/5.
 */
public class C {

    public static final String hostUrl = "http://123.207.87.197:8080/homework";

    public static final int CODE_ILLEGAL = -1;//权限错误
    public static final int CODE_OK = 0;//成功
    public static final int CODE_ERROR_NORMAL = 1;//校验错误
    public static final int CODE_ERROR_STORAGE = 2;//存储错误
    public static final int CODE_ERROR_UNKNOWN = 3;//未知错误

    public static final int ROLE_STUDENT = 1;
    public static final int ROLE_TEACHER = 2;

    public static String getVerifyUrl() {
        return hostUrl + "/verifyCode";
    }

    public static String getLoginUrl() {
        return hostUrl + "/client/login";
    }

    public static String getLoginOutput(String number, String password, int role, String verifyCode) {
        return "account=" + number + "&password=" + password + "&role=" + role + "&verifyCode=" + verifyCode;
    }

    public static String getClassmateInfoUrl(int studentClassId) {
        return hostUrl + "/student/studentClass/" + studentClassId;
    }

    public static String getCourseInfoUrl(int studentId) {
        return hostUrl + "/teacherCourse/student/" + studentId;
    }
}
