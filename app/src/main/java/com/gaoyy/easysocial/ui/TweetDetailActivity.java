package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.CommentAdapter;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.bean.Comment;
import com.gaoyy.easysocial.bean.Tweet;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TweetDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, CommentAdapter.OnItemClickListener, CommentAdapter.OnTouchListener, View.OnClickListener
{
    private LinearLayout tweetDetailLayout;
    private Toolbar tweetDetailToolbar;
    private SwipeRefreshLayout tweetDetailSrlayout;
    private RecyclerView tweetDetailRv;
    private CommentAdapter commentAdapter;
    private LinkedList<Comment> commentList;
    private LinearLayoutManager linearLayoutManager;

    private BasicProgressDialog basicProgressDialog;

    private LinearLayout toolbarDetailLayout;
    private LinearLayout toolbarFavoriteLayout;
    private ImageView toolbarFavoriteLabel;
    private TextView toolbarFavoriteCount;
    private LinearLayout toolbarCommentLayout;
    private TextView toolbarCommentCount;
    private LinearLayout toolbarShareLayout;

    private Tweet tweet;

    private int pageCount = -1;
    private int currentPage = 1;

    private int rawX = -1;
    private int rawY = -1;

    private int lastVisibleItemPosition;


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_tweet_detail);
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        Log.i(Global.TAG, "TWEET-->" + tweet.toString());
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        tweetDetailLayout = (LinearLayout) findViewById(R.id.tweet_detail_layout);
        tweetDetailToolbar = (Toolbar) findViewById(R.id.tweet_detail_toolbar);
        tweetDetailSrlayout = (SwipeRefreshLayout) findViewById(R.id.tweet_detail_srlayout);
        tweetDetailRv = (RecyclerView) findViewById(R.id.tweet_detail_rv);

        basicProgressDialog = BasicProgressDialog.create(this);
        toolbarDetailLayout = (LinearLayout) tweetDetailToolbar.findViewById(R.id.toolbar_detail_layout);
        toolbarFavoriteLabel = (ImageView) tweetDetailToolbar.findViewById(R.id.toolbar_favorite_label);
        toolbarFavoriteLayout = (LinearLayout) tweetDetailToolbar.findViewById(R.id.toolbar_favorite_layout);
        toolbarFavoriteCount = (TextView) tweetDetailToolbar.findViewById(R.id.toolbar_favorite_count);
        toolbarCommentLayout = (LinearLayout) tweetDetailToolbar.findViewById(R.id.toolbar_comment_layout);
        toolbarCommentCount = (TextView) tweetDetailToolbar.findViewById(R.id.toolbar_comment_count);
        toolbarShareLayout = (LinearLayout) tweetDetailToolbar.findViewById(R.id.toolbar_share_layout);

        if(tweet.getIsfavor().equals("1"))
        {
            toolbarFavoriteLabel.setImageDrawable(this.getResources().getDrawable(R.mipmap.ic_favorite_press));
        }
        else
        {
            toolbarFavoriteLabel.setImageDrawable(TweetDetailActivity.this.getResources().getDrawable(R.mipmap.ic_favorite_white));
        }

    }


    @Override
    protected void configViewsOnResume()
    {
        super.configViewsOnResume();
        toolbarDetailLayout.setVisibility(View.VISIBLE);
        toolbarFavoriteCount.setText(tweet.getFavorite_count());
        toolbarCommentCount.setText(tweet.getComment_count());
        new CommentTask(true).execute(String.valueOf(currentPage));
    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(tweetDetailToolbar, R.string.none_title, true, colors);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        tweetDetailSrlayout.setOnRefreshListener(this);
        commentAdapter = new CommentAdapter(this, commentList, tweet);
        tweetDetailRv.setAdapter(commentAdapter);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tweetDetailRv.setLayoutManager(linearLayoutManager);

        tweetDetailRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == commentAdapter.getItemCount())
                {
                    if (currentPage <= pageCount)
                    {
                        currentPage = currentPage + 1;
                        new CommentTask(false).execute(String.valueOf(currentPage));
                    }
                    else
                    {
                        Tool.showSnackbar(recyclerView, ":)到底啦");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        new CommentTask(true).execute(String.valueOf(currentPage));
    }

    @Override
    protected void setListener()
    {
        super.setListener();
        commentAdapter.setOnTouchListener(this);
        commentAdapter.setOnItemClickListener(this);
        toolbarFavoriteLayout.setOnClickListener(this);
        toolbarCommentLayout.setOnClickListener(this);
        toolbarShareLayout.setOnClickListener(this);

    }

    @Override
    protected void initData()
    {
        super.initData();
        commentList = new LinkedList<Comment>();
    }

    @Override
    public void onRefresh()
    {
        currentPage = 1;
        new CommentTask(true).execute(String.valueOf(currentPage));
    }

    @Override
    public void onItemClick(View view, int position)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.item_comment_layout:
                showPopupWindow(view, position, rawX, rawY);
                break;
            case R.id.item_home_tweimg:
                Intent intent = new Intent();
                intent.putExtra("url", tweet.getPicture());
                intent.setClass(this, PhotoActivity.class);
                startActivity(intent);
        }
    }

    private void showPopupWindow(View view, int position, int rawX, int rawY)
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_comment_popwindow, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        TextView tv = (TextView) contentView.findViewById(R.id.item_comment_pop_tv);
        tv.setOnClickListener(new PopOnClickListener(popupWindow, position));
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, rawX, rawY);
    }

    @Override
    public void onTouch(View v, MotionEvent event)
    {
        rawX = (int) event.getRawX();
        rawY = (int) event.getRawY();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.toolbar_favorite_layout:
                if(!tweet.getIsfavor().equals("1"))
                {
                    SharedPreferences account = this.getSharedPreferences("account", Activity.MODE_PRIVATE);
                    String[] params = {tweet.getTid(), account.getString("aid", "")};
                    new doFavorTask(tweet).execute(params);
                }
                else
                {
                    Tool.showSnackbar(toolbarDetailLayout,"已赞过~");
                }

                break;
            case R.id.toolbar_comment_layout:
                Intent intent = new Intent();
                Comment comment = new Comment();
                comment.setTid(tweet.getTid());
                intent.putExtra("comment", comment);
                intent.setClass(TweetDetailActivity.this, ReplyActivity.class);
                startActivity(intent);
                break;
            case R.id.toolbar_share_layout:
                Tool.showShare(this,tweet.getPicture(),tweet.getContent());
                break;
        }
    }


    private class PopOnClickListener implements View.OnClickListener
    {
        int position;
        PopupWindow popupWindow;

        public PopOnClickListener(PopupWindow popupWindow, int position)
        {
            this.position = position;
            this.popupWindow = popupWindow;
        }

        @Override
        public void onClick(View v)
        {
            popupWindow.dismiss();
            Intent intent = new Intent();
            intent.putExtra("comment", commentList.get(position - 1));
            intent.putExtra("isChild", true);
            intent.setClass(TweetDetailActivity.this, ReplyActivity.class);
            startActivity(intent);

        }
    }


    class CommentTask extends AsyncTask<String, String, LinkedList<Comment>>
    {
        private boolean status;

        public CommentTask(boolean status)
        {
            this.status = status;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            tweetDetailSrlayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Comment> doInBackground(String... params)
        {
            String tid = tweet.getTid();

            LinkedList<Comment> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("tid", tid)
                    .add("pageNum", params[0])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showCommentList")
                    .post(formBody)
                    .build();
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(body);
                    pageCount = dataJsonObj.getInt("pageCount");
                    Log.i(Global.TAG, "comment count-->" + pageCount);
                    list = gson.fromJson(dataJsonObj.get("commentData").toString(),
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
            Log.i(Global.TAG, "comment list-->" + s.toString());
            tweetDetailSrlayout.setRefreshing(false);
            if (s != null)
            {
                if (status)
                {
                    commentAdapter.addItem(s);
                }
                else
                {
                    commentAdapter.addMoreItem(s);
                }
            }
            else
            {
                Log.i(Global.TAG, "内部错误");
            }
        }
    }

    class doFavorTask extends AsyncTask<String, String, String>
    {
        private Tweet currentTweet;

        public doFavorTask(Tweet currentTweet)
        {
            this.currentTweet = currentTweet;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog(getResources().getString(R.string.request), basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("tid", params[0])
                    .add("aid", params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/doFavor")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, "body--->" + body);
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

            return body;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if (0 == Tool.getRepCode(s))
            {
                Tool.showSnackbar(toolbarDetailLayout, "点赞成功 :)");
                currentTweet.setIsfavor("1");
                currentTweet.setFavorite_count((Integer.valueOf(currentTweet.getFavorite_count()) + 1) + "");
                toolbarFavoriteCount.setText(tweet.getFavorite_count());
                toolbarFavoriteLabel.setImageDrawable(TweetDetailActivity.this.getResources().getDrawable(R.mipmap.ic_favorite_press));
            }
            else
            {
                Tool.showSnackbar(toolbarDetailLayout, "点赞失败 :(");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
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
