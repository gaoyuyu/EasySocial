package com.gaoyy.easysoical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ReplyActivity extends AppCompatActivity
{
    private Toolbar replyToolbar;
    private EditText replyEdit;

    private void assignViews()
    {
        replyToolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        replyEdit = (EditText) findViewById(R.id.reply_edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        assignViews();
        initToolbar();
    }

    private void initToolbar()
    {
        replyToolbar.setTitle(getResources().getString(R.string.reply));
        setSupportActionBar(replyToolbar);
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
