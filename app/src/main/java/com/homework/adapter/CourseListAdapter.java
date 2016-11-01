package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.bean.TeacherInformation;
import com.homework.constant.C;
import com.wang.java_program.video_download.bean.Course;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class CourseListAdapter extends BaseAdapter {

    private Context context;
    private List<TeacherCourse> teacherCourseList;

    public CourseListAdapter(Context context, List<TeacherCourse> teacherCourseList) {
        this.context = context;
        this.teacherCourseList = teacherCourseList;
    }

    public CourseListAdapter(Context context) {
        this.context = context;
        teacherCourseList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Teacher teacher = new Teacher();
            teacher.setTeacherId(i);
            teacher.setAccount("311400_" + i);
            TeacherInformation info = new TeacherInformation();
            info.setAge(i + 5);
            info.setGender(i % 2 == 0 ? "男" : "女");
            info.setRealName("realName_" + i);
            teacher.setTeacherInformation(info);
            teacher.setRole(C.ROLE_STUDENT);

            Course course = new Course();
            course.setCourseName("courseName_" + i);
            course.setCourseHint("courseHint_" + i);

            TeacherCourse teacherCourse = new TeacherCourse();
            teacherCourse.setTeacherCourseId(i);
            teacherCourse.setCourse(course);
            teacherCourse.setTeacher(teacher);

            teacherCourseList.add(teacherCourse);
        }
    }

    @Override
    public int getCount() {
        return teacherCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return teacherCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_course, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        TeacherCourse teacherCourse = teacherCourseList.get(position);
        Course course = teacherCourse.getCourse();
        Teacher teacher = teacherCourse.getTeacher();
        if (course != null) {
            holder.tvCourseName.setText(course.getCourseName());
        }
        if (teacher != null) {
            TeacherInformation info = teacher.getTeacherInformation();
            if (info != null) {
                holder.tvTeacherName.setText(info.getRealName());
            }
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_course_name)
        TextView tvCourseName;
        @Bind(R.id.tv_teacher_name)
        TextView tvTeacherName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
