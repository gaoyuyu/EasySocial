package com.gaoyy.easysocial.ui;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Tool;

/**
 * 反馈
 */
public class FeedbackActivity extends BaseActivity
{
    private Toolbar feedbackToolbar;

    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        feedbackToolbar = (Toolbar) findViewById(R.id.feedback_toolbar);
    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(feedbackToolbar,R.string.feedback,true,colors);
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
