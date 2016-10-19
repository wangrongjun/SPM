package com.homework.bean;

public class Teacher {

    private int teacherId;
    private String userName;
    private transient String password;
    private TeacherInformation teacherInformation;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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

    public TeacherInformation getTeacherInformation() {
        return teacherInformation;
    }

    public void setTeacherInformation(TeacherInformation teacherInformation) {
        this.teacherInformation = teacherInformation;
    }

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

}
