package com.homework.bean;

import com.wang.java_program.video_download.bean.Course;

import java.util.HashSet;
import java.util.Set;

/**
 * by wangrongjun on 2016/11/1.
 */
public class TeacherCourse {

    private int teacherCourseId;// 授课Id
    private Course course;// 课程
    private Teacher teacher;// 上课的老师
    private Set<Student> students = new HashSet<>();// 教导的学生
    private Set<SchoolWork> schoolWorks = new HashSet<>();// 发布的作业

    public int getTeacherCourseId() {
        return teacherCourseId;
    }

    public void setTeacherCourseId(int teacherCourseId) {
        this.teacherCourseId = teacherCourseId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Set<SchoolWork> getSchoolWorks() {
        return schoolWorks;
    }

    public void setSchoolWorks(Set<SchoolWork> schoolWorks) {
        this.schoolWorks = schoolWorks;
    }
}
