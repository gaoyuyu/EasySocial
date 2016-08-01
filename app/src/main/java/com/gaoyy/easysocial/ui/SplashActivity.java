package com.gaoyy.easysocial.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.utils.Tool;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SplashActivity extends AppCompatActivity
{
    private static final long DELAY_TIME = 1000L;
    private RelativeLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);

        int[] colors = Tool.getThemeColors(this);
        splashLayout.setBackgroundColor(getResources().getColor(colors[0]));
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(colors[1]);
        tintManager.setStatusBarTintEnabled(true);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, DELAY_TIME);
    }
}
