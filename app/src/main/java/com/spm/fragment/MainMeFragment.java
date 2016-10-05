package com.spm.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spm.R;
import com.spm.activity.LoginActivity;
import com.spm.bean.Student;
import com.spm.util.P;
import com.wang.android_lib.util.DialogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class MainMeFragment extends Fragment {

    @Bind(R.id.tv_real_name)
    TextView tvRealName;
    @Bind(R.id.tv_student_number)
    TextView tvStudentNumber;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Student student = P.getStudent();
        Student.StudentInformation information = student.getStudentInformation();
        tvStudentNumber.setText(student.getUserName());
        tvRealName.setText(information.getRealName());
        tvAge.setText(information.getAge() + "");
        tvGender.setText(information.getGender());
        tvPhone.setText("15521331234");
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
                        intent.putExtra("number", student.getUserName());
                        intent.putExtra("password", student.getPassword());
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }
}
