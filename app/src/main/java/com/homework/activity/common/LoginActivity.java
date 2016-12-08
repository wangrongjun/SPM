package com.homework.activity.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.homework.R;
import com.homework.activity.StudentMainActivity;
import com.homework.activity.TeacherMainActivity;
import com.homework.model.api.Msg;
import com.homework.bean.Student;
import com.homework.bean.Teacher;
import com.homework.constant.C;
import com.homework.constant.StateCode;
import com.homework.model.DataModel;
import com.homework.model.CallBack;
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
import com.wang.java_util.Pair;
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

    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowUtil.transStateBar(this);
        dataModel = new DataModel();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String account = getIntent().getStringExtra("account");
        int role = getIntent().getIntExtra("role", C.ROLE_STUDENT);
        if (!TextUtil.isEmpty(account)) {
            rbStudent.setChecked(role == C.ROLE_STUDENT);
            etNumber.setText(account);
        }

        //TODO delete
        rbStudent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etNumber.setText("3114006500");
                    etPassword.setText("111111");
                } else {
                    etNumber.setText("teacher1");
                    etPassword.setText("123456");
                }
            }
        });

    }

    public static void start(Context context, String account, int role) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("account", account);
        intent.putExtra("role", role);
        context.startActivity(intent);
    }

    @OnClick({R.id.btn_verify_code, R.id.btn_login, R.id.btn_test})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_code:
//                startGetVerifyCode();
                getVerifyCodeBitmap();
                break;
            case R.id.btn_login:
                String account = etNumber.getText();
                String password = etPassword.getText();
                int role = rbStudent.isChecked() ? C.ROLE_STUDENT : C.ROLE_TEACHER;
                String verifyCode = etVerifyCode.getText().toString();
                if (role == C.ROLE_STUDENT) {
                    studentLogin(account, password, verifyCode);
                } else {
                    teacherLogin(account, password, verifyCode);
                }
                break;
            case R.id.btn_test:
                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                break;
        }
    }

    private void getVerifyCodeBitmap() {
        ivVerifyCode.setImageResource(R.mipmap.ic_loading);
        ivVerifyCode.startAnimation(AnimationUtil.getConstantSpeedRotateAnim(1000));

        dataModel.getVerifyCodeBitmap(new CallBack<Bitmap>() {
            @Override
            public void onSucceed(Bitmap data) {
                ivVerifyCode.clearAnimation();
                ivVerifyCode.setImageBitmap(data);
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                ivVerifyCode.clearAnimation();
                ivVerifyCode.setImageResource(R.mipmap.ic_error);
                M.t(LoginActivity.this, errorMsg);
            }
        });
    }

    private void studentLogin(final String account, final String password, final String verifyCode) {
        DialogUtil.showProgressDialog(LoginActivity.this, "正在登录");

        dataModel.studentLogin(account, password, verifyCode, new CallBack<Student>() {
            @Override
            public void onSucceed(Student data) {
                DialogUtil.cancelProgressDialog();
                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                DialogUtil.cancelProgressDialog();
                M.t(LoginActivity.this, errorMsg);
            }
        });
    }

    private void teacherLogin(final String account, final String password, final String verifyCode) {
        DialogUtil.showProgressDialog(LoginActivity.this, "正在登录");

        dataModel.teacherLogin(account, password, verifyCode, new CallBack<Teacher>() {
            @Override
            public void onSucceed(Teacher data) {
                DialogUtil.cancelProgressDialog();
                startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
            }

            @Override
            public void onFailure(StateCode stateCode, String errorMsg) {
                DialogUtil.cancelProgressDialog();
                M.t(LoginActivity.this, errorMsg);
            }
        });
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

                if (role == C.ROLE_STUDENT) {
                    Type type = new TypeToken<Msg<Student>>() {
                    }.getType();
                    Pair<Boolean, Object> pair = Util.handleMsg(LoginActivity.this, r.result, type);
                    if (pair.first) {
                        Student student = (Student) pair.second;
                        P.setStudent(student);
                        startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                        finish();
                    }

                } else if (role == C.ROLE_TEACHER) {
                    Type type = new TypeToken<Msg<Teacher>>() {
                    }.getType();
                    Pair<Boolean, Object> pair = Util.handleMsg(LoginActivity.this, r.result, type);
                    if (pair.first) {
                        Teacher teacher = (Teacher) pair.second;
                        P.setTeacher(teacher);
                        startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "role=" + role, Toast.LENGTH_SHORT).show();
                }

            }

        }).request(C.getLoginUrl());
    }

    //TODO delete
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
                    String cookie = conn.getHeaderField("Set-Cookie");
                    P.setCookie(cookie);

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
