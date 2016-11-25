package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.Course;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherCourse;
import com.homework.bean.TeacherInformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class TeacherCourseListAdapter extends BaseAdapter {

    private Context context;
    private List<TeacherCourse> teacherCourseList;

    public TeacherCourseListAdapter(
            Context context, List<TeacherCourse> teacherCourseList) {
        this.context = context;
        this.teacherCourseList = teacherCourseList;
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
