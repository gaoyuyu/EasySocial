package com.gaoyy.easysoical;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gaoyy.easysoical.adapter.CommentAdapter;
import com.gaoyy.easysoical.bean.Comment;
import com.gaoyy.easysoical.bean.Tweet;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TweetDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private Toolbar tweetDetailToolbar;
    private SwipeRefreshLayout tweetDetailSrlayout;
    private RecyclerView tweetDetailRv;
    private CommentAdapter commentAdapter;
    private LinkedList<Comment> commentList;
    private LinearLayoutManager linearLayoutManager;

    private Tweet tweet;

    private final OkHttpClient client = new OkHttpClient();

    private void assignViews()
    {
        tweetDetailToolbar = (Toolbar) findViewById(R.id.tweet_detail_toolbar);
        tweetDetailSrlayout = (SwipeRefreshLayout) findViewById(R.id.tweet_detail_srlayout);
        tweetDetailRv = (RecyclerView) findViewById(R.id.tweet_detail_rv);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        Log.i(Global.TAG, "TWEET-->" + tweet.toString());
        assignViews();
        initToolbar();
        initData();
        configViews();
        new CommentTask().execute();


    }

    public void initToolbar()
    {
        tweetDetailToolbar.setTitle("");
        setSupportActionBar(tweetDetailToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }


    private void configViews()
    {
        tweetDetailSrlayout.setOnRefreshListener(this);
        commentAdapter = new CommentAdapter(this,commentList,tweet);
        tweetDetailRv.setAdapter(commentAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetDetailRv.setLayoutManager(linearLayoutManager);

        commentAdapter.setOnTabSelectedListener(new CommentAdapter.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(0 == tab.getPosition())
                {
                    new CommentTask().execute();
                }
                else
                {
                    Tool.showToast(TweetDetailActivity.this,"none fav");
                }
            }
        });
    }

    private void initData()
    {
        commentList = new LinkedList<Comment>();
    }

    @Override
    public void onRefresh()
    {

    }

    class CommentTask extends AsyncTask<String, String, LinkedList<Comment>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected LinkedList<Comment> doInBackground(String... params)
        {
            String tid = tweet.getTid();

            LinkedList<Comment> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("tid", tid)
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showCommentList")
                    .post(formBody)
                    .build();
            try
            {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject jsonObj = Tool.getMainJsonObj(body);
                    list = gson.fromJson(jsonObj.get("data").toString(),
                            new TypeToken<LinkedList<Comment>>()
                            {
                            }.getType());
                    Log.i(Global.TAG, "list-->" + list.toString());
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(LinkedList<Comment> s)
        {
            super.onPostExecute(s);
            commentAdapter.addItem(s);
        }
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
