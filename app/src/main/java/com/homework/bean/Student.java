package com.homework.bean;

public class Student {

    private int studentId;
    private String userName;
    private String password;
    private StudentInformation studentInformation;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentInformation getStudentInformation() {
        return studentInformation;
    }

    public void setStudentInformation(StudentInformation studentInformation) {
        this.studentInformation = studentInformation;
    }

    public class StudentInformation {
        private int studentInformationId;
        private String realName;// 真实姓名
        private String gender;// 性别
        private int age;// 年龄

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
    }

}

