package com.homework.bean;

/**
 * by wangrongjun on 2016/11/1.
 */
public class Teacher {

    private int teacherId;
    private String account;
    private TeacherInformation teacherInformation;
    private int role;

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public TeacherInformation getTeacherInformation() {
        return teacherInformation;
    }

    public void setTeacherInformation(TeacherInformation teacherInformation) {
        this.teacherInformation = teacherInformation;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
