package com.spm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.spm.R;
import com.wang.android_lib.view.BorderEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class LoginActivity extends Activity {

    @Bind(R.id.et_number)
    BorderEditText etNumber;
    @Bind(R.id.et_password)
    BorderEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.btn_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_test:
                createException();
                break;
        }
    }

    private void createException() {
        int i = 1 / 0;
    }
}
