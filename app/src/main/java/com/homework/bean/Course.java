package com.homework.bean;

/**
 * by wangrongjun on 2016/11/7.
 */
public class Course {

    private int courseId;
    private String courseName;

    public Course( String courseName) {
        this.courseName = courseName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
