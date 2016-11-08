package com.homework.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.homework.R;
import com.homework.activity.common.LoginActivity;
import com.homework.bean.Teacher;
import com.homework.bean.TeacherInformation;
import com.homework.constant.C;
import com.homework.util.P;
import com.wang.android_lib.util.DialogUtil;
import com.wang.java_util.TextUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class TeacherMeFragment extends Fragment {

    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_age)
    TextView tvAge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_me, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Teacher teacher = P.getTeacher();
        TeacherInformation info = teacher.getTeacherInformation();
        if (info == null) {
            return;
        }

        tvAccount.setText(teacher.getAccount());
        tvName.setText(info.getRealName());
        String gender = info.getGender();
        tvGender.setText(TextUtil.isEmpty(gender) ? "保密" : gender);
        int age = info.getAge();
        tvAge.setText(age < 0 ? "保密" : age + "");
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
                        Teacher teacher = P.getTeacher();
                        P.setTeacher(null);
                        LoginActivity.start(getActivity(), teacher.getAccount(), C.ROLE_TEACHER);
                        getActivity().finish();
                    }
                });
    }
}
