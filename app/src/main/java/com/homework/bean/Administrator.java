package com.homework.bean;


public class Administrator {

    private int administratorId;
    private String account;
    private AdminInformation adminInformation;
    private int role;

    public int getAdministratorId() {
        return administratorId;
    }

    public void setAdministratorId(int administratorId) {
        this.administratorId = administratorId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public AdminInformation getAdminInformation() {
        return adminInformation;
    }

    public void setAdminInformation(AdminInformation adminInformation) {
        this.adminInformation = adminInformation;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
