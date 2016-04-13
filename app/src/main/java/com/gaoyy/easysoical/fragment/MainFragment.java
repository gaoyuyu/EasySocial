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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.BlankFragment;
import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.PageAdapter;
import com.gaoyy.easysoical.utils.Global;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment
{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

//    String[] pagerTitle = {"首页","Alive Guys","Utila"};
    String[] pagerTitle = {"首页","活动","排行榜"};

    List<Fragment> fragmentList = new ArrayList<Fragment>();

    private PageAdapter pageAdapter;

    private View rootView;

    public MainFragment()
    {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initData();
        initViews(rootView);
        configViews();
        return rootView;
    }


    public void initViews(View rootView)
    {
        toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)getActivity().findViewById(R.id.drawerlayout);
        coordinatorLayout = (CoordinatorLayout)rootView.findViewById(R.id.coordinatorLayout);
        appBarLayout = (AppBarLayout)rootView.findViewById(R.id.appbarlayout);
        tabLayout = (TabLayout)rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
    }
    public void initData()
    {
        for(int i=0;i<pagerTitle.length;i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("title",pagerTitle[i]);
            Fragment fragment = null;
            if(pagerTitle[i].equals("首页"))
            {
                fragment = new BlankFragment();
            }
            else
            {
                fragment = new BlankFragment();
            }
            fragment.setArguments(bundle);
            fragmentList.add(i,fragment);
        }
    }
    public void configViews()
    {
        AppCompatActivity activity = ((AppCompatActivity)getActivity());
        toolbar.setTitle(activity.getResources().getString(R.string.app_name));
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        SystemBarTintManager tintManager=new SystemBarTintManager(activity);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);

        pageAdapter = new PageAdapter(activity.getSupportFragmentManager(),pagerTitle,fragmentList);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        tabLayout.setupWithViewPager(viewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        tabLayout.setTabsFromPagerAdapter(pageAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(Global.TAG,"onResume");
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        SharedPreferences account= getActivity().getSharedPreferences("account",Activity.MODE_PRIVATE);
        if(account != null)
        {
            String loginKey = account.getString("loginKey", "");
            if(loginKey.equals("1"))
            {
                GenericDraweeHierarchyBuilder builder =new GenericDraweeHierarchyBuilder(getActivity().getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setFadeDuration(300)
                        .setPlaceholderImage(new ProgressBarDrawable())
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setTapToRetryEnabled(true)
                        .build();
                SimpleDraweeView img = (SimpleDraweeView)headerView.findViewById(R.id.nav_header_ava);
                TextView name = (TextView)headerView.findViewById(R.id.nav_header_account);
                TextView signature = (TextView)headerView.findViewById(R.id.nav_header_signature);
                img.setImageURI(Uri.parse(account.getString("avatar","")));

                name.setText(account.getString("username",""));
                signature.setText(account.getString("signature",""));

            }
        }



    }
}
