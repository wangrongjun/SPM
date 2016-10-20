package com.homework.bean;

/**
 * by 王荣俊 on 2016/10/20.
 */
public class TeacherInformation {

    private int teacherInformationId;
    private String realName;// 真实姓名
    private String gender;// 性别
    private int age;// 年龄

    public int getTeacherInformationId() {
        return teacherInformationId;
    }

    public void setTeacherInformationId(int teacherInformationId) {
        this.teacherInformationId = teacherInformationId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
