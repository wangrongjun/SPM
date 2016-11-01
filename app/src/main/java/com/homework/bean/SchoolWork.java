package com.homework.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *  by wangrongjun on 2016/11/1.
 */
public class SchoolWork {
    private int SchoolWorkId;
    private String name;// 作业名
    private String content;// 内容
    private Date finalDate;// 截止日期
    private Set<ExtraFile> extraFiles = new HashSet<>();// 附件
    private Set<CommitSchoolWork> commitSchoolWorks = new HashSet<>();// 提交作业单
    private TeacherCourse teacherCourse;// 所属课程
    private Date updateDate;//修改日期
    private Date createDate;// 创建日期
    private int commitWorkCount = 0;// 提交的数量
}
