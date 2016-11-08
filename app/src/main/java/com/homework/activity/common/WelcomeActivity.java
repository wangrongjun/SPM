package com.homework.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.homework.R;
import com.homework.activity.StudentMainActivity;
import com.homework.activity.TeacherMainActivity;
import com.homework.util.P;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (P.isFirst()) {
                startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
            } else {

                String studentAccount = P.getStudent().getAccount();
                String teacherAccount = P.getTeacher().getAccount();
                if (studentAccount == null) {
                    if (teacherAccount == null) {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, TeacherMainActivity.class));
                    }
                } else {
                    startActivity(new Intent(WelcomeActivity.this, StudentMainActivity.class));
                }

            }
            finish();
            return true;
        }
    });

}
