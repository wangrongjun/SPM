package com.homework.bean;

/**
 * by 王荣俊 on 2016/10/20.
 */
public class StudentInformation {

    private int studentInformationId;
    private String realName;// 真实姓名
    private String gender;// 性别
    private int age;// 年龄
    private String email;// 邮箱
//    private StudentClass studentClass;// 所在班级

    public int getStudentInformationId() {
        return studentInformationId;
    }

    public void setStudentInformationId(int studentInformationId) {
        this.studentInformationId = studentInformationId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
