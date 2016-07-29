package com.gaoyy.easysocial;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;

public class VideoPlayActivity extends BaseActivity
{
    private VideoView videoView;
    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_video_play);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        videoView = (VideoView) findViewById(R.id.video_view);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        Uri uri = Uri.parse(getIntent().getStringExtra("video"));
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        if(!videoView.isPlaying())
        {
            videoView.start();
        }
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener()
        {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                Tool.showToast(VideoPlayActivity.this,"视频打开失败");
                finish();
                return false;
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener()
        {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra)
            {
                Tool.showToast(VideoPlayActivity.this,"what==>"+what);
                return false;
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
