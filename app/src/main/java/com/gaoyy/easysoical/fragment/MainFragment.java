package com.gaoyy.easysoical.fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.BlankFragment;
import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.PageAdapter;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{
    private CoordinatorLayout mainCoordinatorLayout;
    private AppBarLayout mainAppbarlayout;
    private Toolbar mainToolbar;
    private TabLayout mainTablayout;
    private ViewPager mainViewpager;
    private DrawerLayout mainDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    String[] pagerTitle = {"首页", "排行榜"};

    List<Fragment> fragmentList = new ArrayList<Fragment>();

    private PageAdapter pageAdapter;

    private View rootView;


    private void assignViews(View rootView)
    {
        mainCoordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_coordinatorLayout);
        mainDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.main_drawerlayout);
        mainAppbarlayout = (AppBarLayout) rootView.findViewById(R.id.main_appbarlayout);
        mainToolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        mainTablayout = (TabLayout) rootView.findViewById(R.id.main_tablayout);
        mainViewpager = (ViewPager) rootView.findViewById(R.id.main_viewpager);
    }


    public MainFragment()
    {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initData();
        assignViews(rootView);
        configViews();
        return rootView;
    }


    public void initData()
    {
        for (int i = 0; i < pagerTitle.length; i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("title", pagerTitle[i]);
            Fragment fragment = null;
            if (pagerTitle[i].equals("首页"))
            {
                fragment = new HomeFragment();
            }
            else if (pagerTitle[i].equals("排行榜"))
            {
                fragment = new RankFragment();
            }
            else
            {
                fragment = new BlankFragment();
            }
            fragment.setArguments(bundle);
            fragmentList.add(i, fragment);
        }
    }

    public void configViews()
    {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        mainToolbar.setTitle(R.string.app_name);
        activity.setSupportActionBar(mainToolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, mainDrawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        mainDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);

        pageAdapter = new PageAdapter(activity.getSupportFragmentManager(), pagerTitle, fragmentList);
        mainViewpager.setAdapter(pageAdapter);
        mainViewpager.setOffscreenPageLimit(3);

        mainTablayout.setTabMode(TabLayout.MODE_FIXED);
        mainTablayout.setupWithViewPager(mainViewpager);
        mainTablayout.setTabsFromPagerAdapter(pageAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(Global.TAG, "onResume");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.main_nav);
        View headerView = navigationView.getHeaderView(0);
        SharedPreferences account = getActivity().getSharedPreferences("account", Activity.MODE_PRIVATE);
        SimpleDraweeView img = (SimpleDraweeView) headerView.findViewById(R.id.nav_header_ava);
        TextView name = (TextView) headerView.findViewById(R.id.nav_header_account);
        TextView signature = (TextView) headerView.findViewById(R.id.nav_header_signature);

        if (account != null)
        {
            if (Tool.isLogin(getActivity()))
            {
                img.setImageURI(Uri.parse(account.getString("avatar", "")));
                name.setText(account.getString("username", ""));
                signature.setText(account.getString("signature", ""));
            }
            else
            {
                name.setText("未登录");
                signature.setText("");
            }
        }


    }
}
