package com.homework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.homework.R;

/**
 * by 王荣俊 on 2016/10/11.
 */
public class ShowCrashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wang.android_lib.R.layout.activity_show_notification);

        TextView tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setText(getIntent().getStringExtra("crash_message"));

    }
}
