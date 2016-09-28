package com.spm.bean;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class Student {

    private String number;
    private String name;
    private String password;
    private int gender;
    private int age;
    private String phone;

    public Student() {

    }

    public Student(String number, String name, String password, int gender, int age, String phone) {
        this.number = number;
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
