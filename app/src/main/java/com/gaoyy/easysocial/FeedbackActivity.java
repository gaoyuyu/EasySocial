package com.gaoyy.easysocial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gaoyy.easysoical.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class FeedbackActivity extends AppCompatActivity
{
    private Toolbar feedbackToolbar;

    private void assignViews()
    {
        feedbackToolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        assignViews();
        initToolbar();

    }

    public void initToolbar()
    {
        feedbackToolbar.setTitle(R.string.feedback);
        setSupportActionBar(feedbackToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
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
