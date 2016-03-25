package com.gaoyy.easysoical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class TweetDetailActivity extends AppCompatActivity
{
    private Toolbar tweetDetailToolbar;

    private void assignViews()
    {
        tweetDetailToolbar = (Toolbar) findViewById(R.id.tweet_detail_toolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        assignViews();
        initToolbar();
    }

    public void initToolbar()
    {
        setSupportActionBar(tweetDetailToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.tweet_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case R.id.menu_tweet_detail_comment:
                Toast.makeText(TweetDetailActivity.this, "menu_tweet_detail_comment", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
