package com.spm.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.spm.R;
import com.wang.android_lib.util.M;
import com.wang.java_util.TextUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * by 王荣俊 on 2016/10/5.
 */
public class CourseVideoPlayActivity extends Activity {

    @Bind(R.id.et_video_url)
    EditText etVideoUrl;
    @Bind(R.id.video_view)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
    }

    @OnClick({R.id.btn_start, R.id.btn_pause, R.id.btn_stop, R.id.btn_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                videoView.start();
                break;
            case R.id.btn_pause:
                videoView.pause();
                break;
            case R.id.btn_stop:
                break;
            case R.id.btn_play:
                play();
                break;
        }
    }

    private void play() {
        String url = etVideoUrl.getText().toString();
        if (TextUtil.isEmpty(url)) {
            M.t(this, "不能为空");
            return;
        }
        videoView.setVideoURI(Uri.parse(url));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        videoView.start();
    }
}
