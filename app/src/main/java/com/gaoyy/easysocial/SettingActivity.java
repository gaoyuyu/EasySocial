package com.gaoyy.easysocial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
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
        setListener();
    }

    private void initToolbar()
    {
        settingToolbar.setTitle(R.string.setting);
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void configViews()
    {

        if(Tool.isLogin(this))
        {
            settingLogout.setVisibility(View.VISIBLE);
            settingBg.setImageURI(Uri.parse(account.getString("avatar", "")));
            settingAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
            settingUsername.setText(account.getString("username", ""));
            settingEmail.setText(account.getString("email", ""));
        }
        else
        {
            settingLogout.setVisibility(View.GONE);
            settingUsername.setText("未登录");
            settingEmail.setText("");
        }
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
                if(Tool.isLogin(this))
                {
                    intent.setClass(SettingActivity.this,ModifyActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Tool.showSnackbar(v,"请先登录 : )");
                }
                break;
            case R.id.setting_feedback:
                intent.setClass(SettingActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_about:
                intent.setClass(SettingActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_logout:
                SharedPreferences account = getSharedPreferences("account",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = account.edit();
                editor.putString("loginKey","0");
                editor.commit();
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
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

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(Global.TAG,"SettingActivity onResume");
        configViews();
    }
}
