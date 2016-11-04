package com.homework.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.homework.R;
import com.homework.activity.common.BaseActivity;
import com.homework.bean.Student;
import com.homework.bean.StudentClass;
import com.homework.bean.StudentInformation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * by wangrongjun on 2016/11/1.
 */
public class StudentInfoActivity extends BaseActivity {

    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_contact)
    TextView tvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        ButterKnife.bind(this);
        Student student = new Gson().fromJson(getIntent().getStringExtra("student"), Student.class);
        initView(student);
    }

    public static void start(Context context, Student student) {
        Intent intent = new Intent(context, StudentInfoActivity.class);
        intent.putExtra("student", new Gson().toJson(student));
        context.startActivity(intent);
    }

    private void initView(Student student) {
        if (student == null) {
            return;
        }
        tvAccount.setText(student.getAccount());
        StudentInformation info = student.getStudentInformation();
        if (info == null) {
            return;
        }
        tvName.setText(info.getRealName());
        tvGender.setText(info.getGender());
        tvAge.setText(info.getAge() + "");
        tvContact.setText(info.getEmail());
        StudentClass studentClass = info.getStudentClass();
        if (studentClass == null) {
            return;
        }
        tvClass.setText(studentClass.getClassName());
    }
}
