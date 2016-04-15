package com.gaoyy.easysoical;

import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.adapter.PageAdapter;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private ImageView personalHeaderGender;

    private String[] personalTitles = {"我发表的","赞"};
    private List<Fragment> fragmentList;

    private SharedPreferences account;

    private PageAdapter pageAdapter;

    private final OkHttpClient client = new OkHttpClient();



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
        personalHeaderGender = (ImageView) findViewById(R.id.personal_header_gender);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        account= getSharedPreferences("account", Activity.MODE_PRIVATE);
        assignViews();
        initData();
        initToolbar();
        configViews();
        new PersonalCountTask().execute();

    }

    private void initData()
    {
        fragmentList = new ArrayList<Fragment>();
        for (int i=0;i<personalTitles.length;i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("aid",account.getString("aid",""));
            Fragment fragment = null;
            if(personalTitles[i].equals("我发表的"))
            {
                fragment = new BlankFragment();
            }
            else
            {
                fragment = new BlankFragment();
            }
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
    }


    private void initToolbar()
    {
        personalCollapsinglayout.setTitleEnabled(false);
        personalToolbar.setTitle(getResources().getString(R.string.person_center));
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
        personalHeaderAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
        personalHeaderName.setText(account.getString("username",""));


        pageAdapter = new PageAdapter(getSupportFragmentManager(),personalTitles,fragmentList);
        personalViewpager.setAdapter(pageAdapter);

        personalTablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        personalTablayout.setupWithViewPager(personalViewpager);
        personalTablayout.setTabsFromPagerAdapter(pageAdapter);


        String gender =account.getString("gender","");
        if(gender.equals("1"))
        {
            personalHeaderGender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_male));
        }
        else
        {
            personalHeaderGender.setImageDrawable(getResources().getDrawable(R.mipmap.ic_female));
        }
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

    class PersonalCountTask  extends AsyncTask<String, String, String>
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
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG,body);
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "doInBackground e-->" + e.toString());
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
                    personalHeaderTweetCount.setText(tweetCount+"");
                    personalHeaderFavoriteCount.setText(favCount+"");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
