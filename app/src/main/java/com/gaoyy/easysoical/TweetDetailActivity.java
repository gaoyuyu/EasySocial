package com.gaoyy.easysoical;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.adapter.TweetDetailAdapter;
import com.gaoyy.easysoical.bean.Tweet;
import com.gaoyy.easysoical.fragment.CommentFragment;
import com.gaoyy.easysoical.fragment.FavoriteFragment;
import com.gaoyy.easysoical.utils.Global;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class TweetDetailActivity extends AppCompatActivity
{
    private Toolbar tweetDetailToolbar;
    private TabLayout tweetDetailTablayout;
    private ViewPager tweetDetailViewpager;
    private CoordinatorLayout tweetDetailCoordinatorLayout;
    private TweetDetailAdapter tweetDetailAdapter;
    private String[] detailTitle = {"评论", "赞"};
    private List<Fragment> detailFragmentList;


    private SimpleDraweeView itemHomeAvatar;
    private TextView itemHomeAccount;
    private TextView itemHomeDetail;
    private TextView itemHomeTweet;
    private SimpleDraweeView itemHomeTweimg;


    private Tweet tweet;

    private void assignViews()
    {
        tweetDetailToolbar = (Toolbar) findViewById(R.id.tweet_detail_toolbar);
        tweetDetailTablayout = (TabLayout) findViewById(R.id.tweet_detail_tablayout);
        tweetDetailViewpager = (ViewPager) findViewById(R.id.tweet_detail_viewpager);
        tweetDetailCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.tweet_detail_coordinatorLayout);
        assignHeader();
    }

    private void assignHeader()
    {
        itemHomeAvatar = (SimpleDraweeView) findViewById(R.id.item_home_avatar);
        itemHomeAccount = (TextView) findViewById(R.id.item_home_account);
        itemHomeDetail = (TextView) findViewById(R.id.item_home_detail);
        itemHomeTweet = (TextView) findViewById(R.id.item_home_tweet);
        itemHomeTweimg = (SimpleDraweeView) findViewById(R.id.item_home_tweimg);
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
        initFragmentList();
        configViews();

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

    public void initFragmentList()
    {
        detailFragmentList = new ArrayList<Fragment>();
        CommentFragment commentFragment = new CommentFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("tweet",tweet);
        commentFragment.setArguments(bundle);
        favoriteFragment.setArguments(bundle);
        detailFragmentList.add(commentFragment);
        detailFragmentList.add(favoriteFragment);
    }

    public void configViews()
    {
        initHeader();
        tweetDetailAdapter = new TweetDetailAdapter(getSupportFragmentManager(), detailTitle, detailFragmentList);
        tweetDetailViewpager.setAdapter(tweetDetailAdapter);
        tweetDetailTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tweetDetailTablayout.setupWithViewPager(tweetDetailViewpager);
        tweetDetailTablayout.setTabsFromPagerAdapter(tweetDetailAdapter);
    }


    private void initHeader()
    {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .build();

        Uri avaUri = Uri.parse(tweet.getAvatar());
        Uri picUri = Uri.parse(tweet.getPicture());
        itemHomeAvatar.setImageURI(avaUri);
        itemHomeTweimg.setHierarchy(hierarchy);
        itemHomeTweimg.setController(controller);
        itemHomeTweimg.setImageURI(picUri);


        itemHomeAccount.setText(tweet.getUsername());
        itemHomeDetail.setText(tweet.getCreate_time());
        itemHomeTweet.setText(tweet.getContent());

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
