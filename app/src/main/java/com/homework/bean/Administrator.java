package com.homework.bean;

public class Administrator {

    private int administratorId;
    private String userName;
    private transient String password;
    private AdminInformation adminInformation;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
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

    public AdminInformation getAdminInformation() {
        return adminInformation;
    }

    public void setAdminInformation(AdminInformation adminInformation) {
        this.adminInformation = adminInformation;
    }

    public class AdminInformation {
        private int adminInformationId;
        private String realName;// 真实姓名
        private String gender;// 性别
        private int age;// 年龄

        public int getAdminInformationId() {
            return adminInformationId;
        }

        public void setAdminInformationId(int adminInformationId) {
            this.adminInformationId = adminInformationId;
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
