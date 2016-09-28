package com.spm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.spm.R;
import com.wang.android_lib.util.M;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/9/28.
 */
public class ExceptionActivity extends Activity {

    @Bind(R.id.tv_exception_log)
    TextView tvExceptionLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        ButterKnife.bind(this);

        String message = getIntent().getStringExtra("message");
        tvExceptionLog.setText(message);
    }

    @OnClick(R.id.btn_send_exception_log)
    public void onClick() {
        M.t(this, "已发送，即将关闭界面");
    }
}
