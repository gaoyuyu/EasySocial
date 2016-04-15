package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener
{
    private Toolbar settingToolbar;
    private SimpleDraweeView settingBg;
    private SimpleDraweeView settingAvatar;
    private TextView settingUsername;
    private TextView settingEmail;
    private RelativeLayout settingModify;
    private RelativeLayout settingFeedback;
    private RelativeLayout settingAbout;
    private Button settingLogout;

    private void assignViews()
    {
        settingToolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        settingBg = (SimpleDraweeView) findViewById(R.id.setting_bg);
        settingAvatar = (SimpleDraweeView) findViewById(R.id.setting_avatar);
        settingUsername = (TextView) findViewById(R.id.setting_username);
        settingEmail = (TextView) findViewById(R.id.setting_email);
        settingModify = (RelativeLayout) findViewById(R.id.setting_modify);
        settingFeedback = (RelativeLayout) findViewById(R.id.setting_feedback);
        settingAbout = (RelativeLayout) findViewById(R.id.setting_about);
        settingLogout = (Button) findViewById(R.id.setting_logout);
    }


    private SharedPreferences account;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
        assignViews();
        initToolbar();
        configViews();
        setListener();
    }

    private void initToolbar()
    {
        settingToolbar.setTitle("设置");
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void configViews()
    {
        settingBg.setImageURI(Uri.parse(account.getString("avatar", "")));
        settingAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
        settingUsername.setText(account.getString("username", ""));
        settingEmail.setText(account.getString("email", ""));
    }

    private void setListener()
    {
        settingModify.setOnClickListener(this);
        settingFeedback.setOnClickListener(this);
        settingAbout.setOnClickListener(this);
        settingLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int  id= v.getId();
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.setting_modify:
                intent.setClass(SettingActivity.this,ModifyActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_feedback:

                break;
            case R.id.setting_about:

                break;
            case R.id.setting_logout:

                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
