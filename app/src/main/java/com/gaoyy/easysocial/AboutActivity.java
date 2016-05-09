package com.gaoyy.easysocial;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Tool;

public class AboutActivity extends BaseActivity
{
    private Toolbar aboutToolbar;
    private TextView aboutAppName;
    private ImageView aboutAppLogo;
    private TextView aboutVersionName;
    private TextView aboutCopyright;


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        aboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        aboutAppName = (TextView) findViewById(R.id.about_app_name);
        aboutAppLogo = (ImageView) findViewById(R.id.about_app_logo);
        aboutVersionName = (TextView) findViewById(R.id.about_version_name);
        aboutCopyright = (TextView) findViewById(R.id.about_copyright);
    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(aboutToolbar,R.string.about,true,colors);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        aboutAppName.setText(R.string.app_name);
        aboutAppLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        aboutVersionName.setText("version " + Tool.getVersionName(this));
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
