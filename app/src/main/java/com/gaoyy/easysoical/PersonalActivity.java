package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class PersonalActivity extends AppCompatActivity
{
    private CoordinatorLayout personalCoordinatorlayout;
    private AppBarLayout personalAppbarlayout;
    private CollapsingToolbarLayout personalCollapsinglayout;
    private Toolbar personalToolbar;
    private TabLayout personalTablayout;
    private ViewPager personalViewpager;

    private ImageView personalHeaderBg;
    private SimpleDraweeView personalHeaderAvatar;
    private TextView personalHeaderName;
    private TextView personalHeaderTweetCount;
    private TextView personalHeaderFavoriteCount;



    private void assignViews()
    {
        personalCoordinatorlayout = (CoordinatorLayout) findViewById(R.id.personal_coordinatorlayout);
        personalAppbarlayout = (AppBarLayout) findViewById(R.id.personal_appbarlayout);
        personalCollapsinglayout = (CollapsingToolbarLayout) findViewById(R.id.personal_collapsinglayout);
        personalToolbar = (Toolbar) findViewById(R.id.personal_toolbar);
        personalTablayout = (TabLayout) findViewById(R.id.personal_tablayout);
        personalViewpager = (ViewPager) findViewById(R.id.personal_viewpager);

        personalHeaderBg = (ImageView) findViewById(R.id.personal_header_bg);
        personalHeaderAvatar = (SimpleDraweeView) findViewById(R.id.personal_header_avatar);
        personalHeaderName = (TextView) findViewById(R.id.personal_header_name);
        personalHeaderTweetCount = (TextView) findViewById(R.id.personal_header_tweet_count);
        personalHeaderFavoriteCount = (TextView) findViewById(R.id.personal_header_favorite_count);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        assignViews();
        initToolbar();
        configViews();
    }


    private void initToolbar()
    {
        personalCollapsinglayout.setTitleEnabled(false);
        personalToolbar.setTitle("asda");
        personalToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(personalToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void configViews()
    {
        SharedPreferences account = getSharedPreferences("account", Activity.MODE_PRIVATE);
        personalHeaderAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
    }
}
