package com.gaoyy.easysocial.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private boolean isFirstIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);

        SharedPreferences preferences = getSharedPreferences("AppIsFirstIn",
                MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);

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
                if(isFirstIn)
                {
                    startRedirect(GuideActivity.class);
                }
                else
                {
                    startRedirect(MainActivity.class);
                }
            }
        }, DELAY_TIME);
    }


    private void startRedirect(Class cls)
    {
        startActivity(new Intent(SplashActivity.this,cls));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
