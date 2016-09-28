package com.spm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spm.R;
import com.spm.activity.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class GuideThreeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_three, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_learn)
    public void onClick() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
