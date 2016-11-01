package com.homework.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.bean.Msg;
import com.homework.bean.Student;
import com.homework.constant.C;
import com.homework.util.MyNotification;
import com.homework.util.P;
import com.homework.util.Util;
import com.wang.android_lib.helper.AndroidHttpHelper;
import com.wang.android_lib.util.AndroidHttpUtil;
import com.wang.android_lib.util.AnimationUtil;
import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.M;
import com.wang.android_lib.util.WindowUtil;
import com.wang.android_lib.view.BorderEditText;
import com.wang.java_util.HttpUtil;
import com.wang.java_util.JsonFormatUtil;
import com.wang.java_util.Pair;
import com.wang.java_util.StreamUtil;
import com.wang.java_util.TextUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

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
    @Bind(R.id.et_verify_code)
    EditText etVerifyCode;
    @Bind(R.id.iv_verify_code)
    ImageView ivVerifyCode;
    @Bind(R.id.rb_student)
    RadioButton rbStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowUtil.transStateBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String number = getIntent().getStringExtra("account");
        if (!TextUtil.isEmpty(number)) {
            etNumber.setText(number);
        }

    }

    @OnClick({R.id.btn_verify_code, R.id.btn_login, R.id.btn_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_code:
                startGetVerifyCode();
                break;
            case R.id.btn_login:
                String number = etNumber.getText();
                String password = etPassword.getText();
                int role = rbStudent.isChecked() ? C.ROLE_STUDENT : C.ROLE_TEACHER;
                String verifyCode = etVerifyCode.getText().toString();
                if (!TextUtil.isEmpty(number, password, verifyCode)) {
                    startLogin(number, password, role, verifyCode);
                } else {
                    M.t(this, "请填写信息");
                }
                break;
            case R.id.btn_test:
                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                break;
        }
    }

    private void startLogin(final String number, final String password, final int role, final String verifyCode) {
        DialogUtil.showProgressDialog(LoginActivity.this, "正在登录");

        AndroidHttpHelper helper = new AndroidHttpHelper(this);
        helper.setRequestMethod("POST").addRequestProperty("Cookie", P.getCookie());
        helper.setOutput(C.getLoginOutput(number, password, role, verifyCode));
        helper.setOnSucceedListener(new AndroidHttpUtil.OnSucceedListener() {

            @Override
            public void onSucceed(HttpUtil.Result r) {
                DialogUtil.cancelProgressDialog();

                Type type = new TypeToken<Msg<Student>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(LoginActivity.this, r.result, type);
                if (pair.first) {
                    Student student = (Student) pair.second;
//                    P.setCookie(r.setCookie);
                    P.setStudent(student);
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                    finish();
                } else {
                    M.t(LoginActivity.this, pair.second + "");
                }
            }

        }).request(C.getLoginUrl());
    }

    private void startLogin1(final String number, final String password, final int role, final String verifyCode) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                DialogUtil.showProgressDialog(LoginActivity.this, "正在登录");
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpURLConnection conn =
                            (HttpURLConnection) new URL(C.getLoginUrl()).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    String cookie = P.getCookie();
                    MyNotification.showCookie(cookie);
                    if (!TextUtil.isEmpty(cookie)) {
                        conn.setRequestProperty("Cookie", cookie);
                    }
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(
                            C.getLoginOutput(number, password, role, verifyCode).getBytes());

                    return StreamUtil.readInputStream(conn.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }

            }

            @Override
            protected void onPostExecute(String result) {

                DialogUtil.cancelProgressDialog();
                MyNotification.showUserInfo(JsonFormatUtil.formatJson(result));
                Type type = new TypeToken<Msg<Student>>() {
                }.getType();
                Pair<Boolean, Object> pair = Util.handleMsg(LoginActivity.this, result, type);
                if (pair.first) {
                    Student student = (Student) pair.second;
                    P.setStudent(student);
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                    finish();
                } else {
                    M.t(LoginActivity.this, pair.second + "");
                }

            }
        };

        task.execute();

    }

    private void startGetVerifyCode() {

        AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                ivVerifyCode.setImageResource(R.mipmap.ic_loading);
                ivVerifyCode.startAnimation(AnimationUtil.getConstantSpeedRotateAnim(1000));
            }

            @Override
            protected Bitmap doInBackground(Void... params) {

                try {
                    HttpURLConnection conn =
                            (HttpURLConnection) new URL(C.getVerifyUrl()).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);

                    //获取Cookie
                    P.setCookie(conn.getHeaderField("Set-Cookie"));

                    return BitmapFactory.decodeStream(conn.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {

                ivVerifyCode.clearAnimation();
                if (bitmap == null) {
                    ivVerifyCode.setImageResource(R.mipmap.ic_error);
                } else {
                    ivVerifyCode.setImageBitmap(bitmap);
                }

            }
        };

        task.execute();
    }

}
