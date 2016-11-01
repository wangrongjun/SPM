package com.homework.bean;

import java.util.HashSet;
import java.util.Set;

/**
 *  by wangrongjun on 2016/11/1.
 */
public class CommitSchoolWork {
    private int commitSchoolWorkId;
    private Set<ExtraFile> extraFiles = new HashSet<>();
    private boolean isCommit;// 是否提交
    private Student student;
    private String remark;// 备注
    private SchoolWork schoolWork;
}
