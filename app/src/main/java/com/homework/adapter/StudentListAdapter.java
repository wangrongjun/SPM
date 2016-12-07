package com.homework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homework.R;
import com.homework.bean.Student;
import com.homework.bean.StudentClass;
import com.homework.bean.StudentInformation;
import com.homework.constant.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/10/31.
 */
public class StudentListAdapter extends BaseAdapter {

    private Context context;
    private List<Student> students;

    public StudentListAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
    }

    public StudentListAdapter(Context context) {
        this.context = context;
        students = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Student student = new Student();
            student.setAccount("311400_" + i);
            student.setRole(C.ROLE_STUDENT);
            student.setStudentId(i);
            StudentInformation info = new StudentInformation();
            info.setAge(i + 5);
            info.setEmail("email_" + i);
            info.setGender(i % 2 == 0 ? "男" : "女");
            info.setStudentClass(new StudentClass());
            info.setRealName("realname_" + i);
            student.setStudentInformation(info);
            students.add(student);
        }
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_student, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        updateView(holder, position);
        return convertView;
    }

    private void updateView(ViewHolder holder, int position) {
        Student student = students.get(position);
        StudentInformation info = student.getStudentInformation();
        if (info != null) {
            holder.tvStudentName.setText(info.getRealName());
            holder.tvStudentNumber.setText(student.getAccount());
            holder.tvGender.setText(info.getGender());
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_student_number)
        TextView tvStudentNumber;
        @Bind(R.id.tv_student_name)
        TextView tvStudentName;
        @Bind(R.id.tv_gender)
        TextView tvGender;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
