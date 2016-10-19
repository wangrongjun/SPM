package com.homework.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.homework.R;
import com.homework.activity.LoginActivity;
import com.homework.bean.Student;
import com.homework.util.P;
import com.wang.android_lib.util.DialogUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/4.
 */
public class MainMeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me, container, false);
        ButterKnife.bind(this, view);
        return view;
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
