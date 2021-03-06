package com.homework.constant;

import android.os.Environment;

import java.io.File;

/**
 * by 王荣俊 on 2016/10/5.
 */
public class C {

    //    public static final String hostUrl = "http://123.207.87.197:8080/homework";
    public static final String hostUrl = "http://coolgmr.com:8080/homework";

    public static String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "homework" + File.separator;

    public static String cacheDir = dir + ".cache" + File.separator;
    public static String extraFileDir = dir + "extraFiles" + File.separator;

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

    public static String getStudentCourseInfoUrl(int studentId) {
        return hostUrl + "/teacherCourse/student/" + studentId;
    }

    /**
     * 获取指定编号老师上的课程的学生班级
     * 请求方式:GET  url示例:123.207.87.197:8080/homework/teacherCourse/studentClasses/1
     *
     * @return Map<Integer, new Object[]>转成的json(键为课程编号,值Object[]第一个元素为课程名，
     * 第二个元素表示该课程下的班级编号集合)
     * {"code":0,"message":{"1":["软件工程",[1]]}}
     */
    public static String teacherGetCourseInfoListUrl(int teacherId) {
        return hostUrl + "/teacherCourse/studentClasses/" + teacherId;
    }

    /**
     * 获取课程作业  请求方式:GET  权限：教师，学生
     *
     * @param date 表示获取该日期后的作业(时间格式:yyyy-MM-dd HH:mm)
     * @return SchoolWork转成的json(作业名, 内容, 截止日期, 附件, 修改日期, 创建日期, 提交的数量)
     */
    public static String getSchoolWorkUrl(int teacherCourseId, String date) {
        //后台规定了空格在url中编码为%20
        return hostUrl + "/schoolWork/" + teacherCourseId + "/" + date.replace(" ", "%20");
    }

    /**
     * 教师发布课程作业
     */
    public static String teacherAddSchoolWorkUrl() {
        return hostUrl + "/schoolWork";
    }

    /**
     * 学生提交（修改）作业
     */
    public static String studentCommitSchoolWorkUrl() {
        return hostUrl + "/commitWork";
    }

    /**
     * 获取某学生某次作业下的作业:/commitWork/{schoolWorkId}
     * 返回值：CommitSchoolWork转成的json
     */
    public static String studentGetCommitSchoolWorkUrl(int schoolWorkId) {
        return hostUrl + "/commitWork/" + schoolWorkId;
    }

    /**
     * 获取某老师某次作业下的所有提交的作业
     * 返回值  List<CommitSchoolWork>转成的json(此次作业的全部已提交的作业)
     */
    public static String teacherGetCommitSchoolWorkUrl(int schoolWorkId) {
        return hostUrl + "/commitWorks/" + schoolWorkId;
    }

    /**
     * 教师删除作业附件
     * <p/>
     * 请求方式:DELETE(非GET,POST，都一律用POST请求加参数_method="参数类型(如这里为DELETE)")
     * 权限:教师
     * 请求参数:{
     * _method:DELETE(以后不再说明)
     * extraFileId:删除的附件编号
     * }
     * 返回值{
     * success
     * }
     * 失败{
     * }
     */
    public static String teacherDeleteExtraFileUrl() {
        return hostUrl + "/schoolWork/extraFile";
    }

    public static String getTeacherDeleteExtraFileUrlOutput(int extraFileId) {
        return "_method=DELETE&extraFileId=" + extraFileId;
    }

}
