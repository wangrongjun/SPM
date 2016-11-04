package com.homework.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homework.R;
import com.homework.activity.common.LoginActivity;
import com.homework.bean.Student;
import com.homework.bean.StudentInformation;
import com.homework.util.P;
import com.wang.android_lib.util.DialogUtil;
import com.wang.java_util.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class StudentMeFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_me, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Student student = P.getStudent();
        StudentInformation information = student.getStudentInformation();
        if (information == null) {
            return;
        }

        tvAccount.setText(student.getAccount());
        tvName.setText(information.getRealName());
        String gender = information.getGender();
        String className = "未知";
        if (information.getStudentClass() != null) {
            className = information.getStudentClass().getClassName();
        }

        tvGender.setText(TextUtil.isEmpty(gender) ? "保密" : gender);
        int age = information.getAge();
        tvAge.setText(age < 0 ? "保密" : age + "");
        tvClass.setText(className);
        String email = information.getEmail();
        tvContact.setText(TextUtil.isEmpty(email) ? "未填写" : email);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_logout)
    public void onClick() {
        DialogUtil.showConfirmDialog(getActivity(), "退出登录", "确实要退出登录吗？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Student student = P.getStudent();
                        P.setStudent(null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("account", student.getAccount());
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }
}
