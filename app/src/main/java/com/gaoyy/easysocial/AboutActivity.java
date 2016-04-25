package com.gaoyy.easysocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysocial.utils.Tool;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class AboutActivity extends AppCompatActivity
{
    private Toolbar aboutToolbar;
    private TextView aboutAppName;
    private ImageView aboutAppLogo;
    private TextView aboutVersionName;
    private TextView aboutCopyright;

    private void assignViews()
    {
        aboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        aboutAppName = (TextView) findViewById(R.id.about_app_name);
        aboutAppLogo = (ImageView) findViewById(R.id.about_app_logo);
        aboutVersionName = (TextView) findViewById(R.id.about_version_name);
        aboutCopyright = (TextView) findViewById(R.id.about_copyright);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        assignViews();
        initToolbar();
        configViews();
    }

    public void initToolbar()
    {
        aboutToolbar.setTitle(R.string.about);
        setSupportActionBar(aboutToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }
    private void configViews()
    {
        aboutAppName.setText(R.string.app_name);
        aboutAppLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        aboutVersionName.setText("version "+Tool.getVersionName(this));
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
