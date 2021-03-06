package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.PageAdapter;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.fragment.FavoriteFragment;
import com.gaoyy.easysocial.fragment.HomeFragment;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 个人中心
 */
public class PersonalActivity extends BaseActivity
{
    private CoordinatorLayout personalCoordinatorlayout;
    private AppBarLayout personalAppbarlayout;
    private CollapsingToolbarLayout personalCollapsinglayout;
    private Toolbar personalToolbar;
    private TabLayout personalTablayout;
    private ViewPager personalViewpager;


    private SimpleDraweeView personalHeaderBg;
    private SimpleDraweeView personalHeaderAvatar;
    private TextView personalHeaderName;
    private TextView personalHeaderTweetCount;
    private TextView personalHeaderFavoriteCount;
    private ImageView personalHeaderGender;

    private String[] personalTitles = {"我发表的", "赞"};
    private List<Fragment> fragmentList;

    private SharedPreferences account;

    private PageAdapter pageAdapter;

    private static final int PERSONAL_SET_BG = 450;


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_personal);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        personalCoordinatorlayout = (CoordinatorLayout) findViewById(R.id.personal_coordinatorlayout);
        personalAppbarlayout = (AppBarLayout) findViewById(R.id.personal_appbarlayout);
        personalCollapsinglayout = (CollapsingToolbarLayout) findViewById(R.id.personal_collapsinglayout);
        personalToolbar = (Toolbar) findViewById(R.id.personal_toolbar);
        personalTablayout = (TabLayout) findViewById(R.id.personal_tablayout);
        personalViewpager = (ViewPager) findViewById(R.id.personal_viewpager);

        personalHeaderBg = (SimpleDraweeView) findViewById(R.id.personal_header_bg);
        personalHeaderAvatar = (SimpleDraweeView) findViewById(R.id.personal_header_avatar);
        personalHeaderName = (TextView) findViewById(R.id.personal_header_name);
        personalHeaderTweetCount = (TextView) findViewById(R.id.personal_header_tweet_count);
        personalHeaderFavoriteCount = (TextView) findViewById(R.id.personal_header_favorite_count);
        personalHeaderGender = (ImageView) findViewById(R.id.personal_header_gender);

    }


    @Override
    protected void initData()
    {
        super.initData();
        fragmentList = new ArrayList<Fragment>();
        for (int i = 0; i < personalTitles.length; i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("isPersonal", account.getString("aid", ""));
            bundle.putString("aid", account.getString("aid", ""));
            Fragment fragment = null;
            if (personalTitles[i].equals("我发表的"))
            {
                fragment = new HomeFragment();
            }
            else
            {
                fragment = new FavoriteFragment();
            }
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
    }


    @Override
    protected void executeTask()
    {
        super.executeTask();
        new PersonalCountTask().execute();
    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        personalCollapsinglayout.setTitleEnabled(false);
        personalCollapsinglayout.setContentScrimColor(getResources().getColor(colors[0]));
        personalToolbar.setTitle(R.string.person_center);
        personalToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
        setSupportActionBar(personalToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tool.setStatusBarColor(this,colors[1]);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        personalHeaderAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
        personalHeaderName.setText(account.getString("username", ""));

        if (!account.getString("personalbg", "").equals(""))
        {
            personalHeaderBg.setImageURI(Uri.parse("file://" + account.getString("personalbg", "")));
        }


        pageAdapter = new PageAdapter(getSupportFragmentManager(), personalTitles, fragmentList);
        personalViewpager.setAdapter(pageAdapter);

        personalTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        personalTablayout.setupWithViewPager(personalViewpager);
        personalTablayout.setTabsFromPagerAdapter(pageAdapter);


        String gender = account.getString("gender", "");
        if (gender.equals("1"))
        {
            personalHeaderGender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_male));
        }
        else
        {
            personalHeaderGender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_female));
        }
    }

    @Override
    protected void setListener()
    {
        super.setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
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
            case R.id.menu_personal_setbg:
                Intent intent = new Intent();
                intent.setClass(this, SetPicActivity.class);
                intent.putExtra("isPublish", true);
                startActivityForResult(intent, PERSONAL_SET_BG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERSONAL_SET_BG)
        {
            if (resultCode == RESULT_OK)
            {
                String path = data.getStringExtra("tweimg");
                SharedPreferences.Editor editor = account.edit();
                editor.putString("personalbg", path);
                editor.commit();
                personalHeaderBg.setImageURI(Uri.parse("file://" + path));
            }
        }
    }


    class PersonalCountTask extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/getCountInfo")
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, "===========================================");
                Log.i(Global.TAG, "PersonalCountTask code-->" + Tool.getRepCode(body));
                Log.i(Global.TAG, "PersonalCountTask body-->" + body);
                Log.i(Global.TAG, "===========================================");
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "PersonalCountTask doInBackground Exception-->" + e.toString());
            }
            return body;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try
            {
                if (0 == Tool.getRepCode(s))
                {
                    JSONObject dataJsonObj = Tool.getDataJsonObj(s);
                    int tweetCount = dataJsonObj.getInt("personal_tweet_count");
                    int favCount = dataJsonObj.getInt("personal_fav_count");
                    personalHeaderTweetCount.setText(tweetCount + "");
                    personalHeaderFavoriteCount.setText(favCount + "");
                }
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "PersonalCountTask onPostExecute Exception-->" + e.toString());
            }

        }
    }
}
