package com.homework.bean;


public class Student {

    private int studentId;
    private String account;
    private StudentInformation studentInformation;
    private int role;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public StudentInformation getStudentInformation() {
        return studentInformation;
    }

    public void setStudentInformation(StudentInformation studentInformation) {
        this.studentInformation = studentInformation;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
