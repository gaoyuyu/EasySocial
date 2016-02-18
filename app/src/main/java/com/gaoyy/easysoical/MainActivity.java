package com.gaoyy.easysoical;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.gaoyy.easysoical.adapter.PageAdapter;
import com.gaoyy.easysoical.fragment.PageFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private Toolbar toolbar;
    private ListView mainlist;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    String[] arr = {"item01","item02","item03"};
    String[] pagerTitle = {"Dead Man","Alive Guys","Utila","MassStu","Kssip","Yusuto"};

    List<Fragment> fragmentList = new ArrayList<Fragment>();

    private PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        configViews();
    }
    public void configViews()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
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

        SystemBarTintManager tintManager=new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);

        pageAdapter = new PageAdapter(getSupportFragmentManager(),pagerTitle,fragmentList);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(pagerTitle.length);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                toolbar.setTitle(pagerTitle[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        tabLayout.setupWithViewPager(viewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        tabLayout.setTabsFromPagerAdapter(pageAdapter);

        navigationView.setNavigationItemSelectedListener(this);

    }
    public void initData()
    {

        for(int i=0;i<pagerTitle.length;i++)
        {
            Bundle bundle = new Bundle();
            bundle.putString("title",pagerTitle[i]);
            PageFragment pageFragment = new PageFragment();
            pageFragment.setArguments(bundle);
            fragmentList.add(i,pageFragment);
        }



    }
    public void initViews()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbarlayout);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        navigationView = (NavigationView)findViewById(R.id.nav_view);


    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
