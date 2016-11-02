package com.homework.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * by wangrongjun on 2016/11/1.
 */
public class CommitSchoolWork {//学生提交的

    private int commitSchoolWorkId;
    private Set<ExtraFile> extraFiles = new HashSet<>();
    private boolean isCommit;// 是否提交
    private Student student;
    private String remark;// 备注
    private SchoolWork schoolWork;

    public int getCommitSchoolWorkId() {
        return commitSchoolWorkId;
    }

    public void setCommitSchoolWorkId(int commitSchoolWorkId) {
        this.commitSchoolWorkId = commitSchoolWorkId;
    }

    public Set<ExtraFile> getExtraFiles() {
        return extraFiles;
    }

    public void setExtraFiles(Set<ExtraFile> extraFiles) {
        this.extraFiles = extraFiles;
    }

    public boolean isCommit() {
        return isCommit;
    }

    public void setCommit(boolean commit) {
        isCommit = commit;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SchoolWork getSchoolWork() {
        return schoolWork;
    }

    public void setSchoolWork(SchoolWork schoolWork) {
        this.schoolWork = schoolWork;
    }
}
