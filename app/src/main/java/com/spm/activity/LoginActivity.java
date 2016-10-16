package com.spm.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.spm.R;
import com.spm.bean.Student;
import com.spm.constant.C;
import com.spm.util.P;
import com.wang.android_lib.util.AnimationUtil;
import com.wang.android_lib.util.DialogUtil;
import com.wang.android_lib.util.M;
import com.wang.android_lib.util.NotificationUtil;
import com.wang.android_lib.view.BorderEditText;
import com.wang.java_util.StreamUtil;
import com.wang.java_util.TextUtil;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String number = getIntent().getStringExtra("number");
        String password = getIntent().getStringExtra("password");
        if (!TextUtil.isEmpty(number, password)) {
            etNumber.setText(number);
            etPassword.setText(password);
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
                String verifyCode = etVerifyCode.getText().toString();
                if (!TextUtil.isEmpty(number, password, verifyCode)) {
                    startLogin(number, password, verifyCode);
                } else {
                    M.t(this, "请填写信息");
                }
                break;
            case R.id.btn_test:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
    }

    private void startLogin(final String number, final String password, final String verifyCode) {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                DialogUtil.showProgressDialog(LoginActivity.this, "正在登录");
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(C.loginUrl).openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    String cookie = P.getCookie();
                    NotificationUtil.showNotification(LoginActivity.this, 55, "cookie", cookie);
                    if (!TextUtil.isEmpty(cookie)) {
                        conn.setRequestProperty("Cookie", cookie);
                    }
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(
                            C.getLoginOutput(number, password, verifyCode).getBytes());

                    return StreamUtil.readInputStream(conn.getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }

            }

            @Override
            protected void onPostExecute(String result) {

                DialogUtil.cancelProgressDialog();
                M.t(LoginActivity.this, result);
                NotificationUtil.showNotification(LoginActivity.this, 0, "UserInfo", result);
                Student student = new Gson().fromJson(result, Student.class);
                P.setStudent(student);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

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
                    HttpURLConnection conn = (HttpURLConnection) new URL(C.verifyUrl).openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);

                    //获取
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
