package com.homework.bean;

/**
 * by wangrongjun on 2016/10/31.
 */
public class StudentClass {

    private int studentClassId;// 班级Id
    private String className;// 班级名

    public StudentClass() {
    }

    public StudentClass(int studentClassId, String className) {
        this.studentClassId = studentClassId;
        this.className = className;
    }

    public int getStudentClassId() {
        return studentClassId;
    }

    public void setStudentClassId(int studentClassId) {
        this.studentClassId = studentClassId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
