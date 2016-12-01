package com.homework.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * by wangrongjun on 2016/11/1.
 */
public class SchoolWork {//教师发布的

    private int schoolWorkId;
    private String name;// 作业名
    private String content;// 内容
    private Date finalDate;// 截止日期
    private Set<ExtraFile> extraFiles = new HashSet<>();// 附件
    private Set<CommitSchoolWork> commitSchoolWorks = new HashSet<>();// 提交作业单
    private TeacherCourse teacherCourse;// 所属课程
    private Date updateDate;//修改日期
    private Date createDate;// 创建日期
    private int commitWorkCount = 0;// 提交的数量

    public int getSchoolWorkId() {
        return schoolWorkId;
    }

    public void setSchoolWorkId(int schoolWorkId) {
        this.schoolWorkId = schoolWorkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public Set<ExtraFile> getExtraFiles() {
        return extraFiles;
    }

    public void setExtraFiles(Set<ExtraFile> extraFiles) {
        this.extraFiles = extraFiles;
    }

    public Set<CommitSchoolWork> getCommitSchoolWorks() {
        return commitSchoolWorks;
    }

    public void setCommitSchoolWorks(Set<CommitSchoolWork> commitSchoolWorks) {
        this.commitSchoolWorks = commitSchoolWorks;
    }

    public TeacherCourse getTeacherCourse() {
        return teacherCourse;
    }

    public void setTeacherCourse(TeacherCourse teacherCourse) {
        this.teacherCourse = teacherCourse;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCommitWorkCount() {
        return commitWorkCount;
    }

    public void setCommitWorkCount(int commitWorkCount) {
        this.commitWorkCount = commitWorkCount;
    }
}
