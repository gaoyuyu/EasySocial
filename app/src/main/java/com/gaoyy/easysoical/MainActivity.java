package com.gaoyy.easysoical;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private NavigationView navigationView;
    private MainFragment mainFragment;
    private DrawerLayout drawerLayout;

    //记录当前正在使用的fragment
    private Fragment currentFragment;

    public Fragment getCurrentFragment()
    {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment)
    {
        this.currentFragment = currentFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        View headerView = navigationView.getHeaderView(0);
        SimpleDraweeView img = (SimpleDraweeView) headerView.findViewById(R.id.nav_header_ava);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        initFragment(savedInstanceState);


    }

    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (mainFragment == null)
            {
                mainFragment = new MainFragment();
            }
            setCurrentFragment(mainFragment);
            ft.replace(R.id.layout_main, mainFragment).commit();
        }
    }

    /**
     * 当fragment进行切换时，采用隐藏与显示的方法加载fragment以防止数据的重复加载
     *
     * @param from
     * @param to
     */
    public void switchContent(Fragment from, Fragment to)
    {
        if (currentFragment != to)
        {
            //设置当前的Fragment
            setCurrentFragment(to);

            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out);
            if (!to.isAdded())
            {    // 先判断是否被add过
                ft.hide(from).add(R.id.layout_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            }
            else
            {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.nav_home:

                break;
            case R.id.nav_personal:
                intent.setClass(MainActivity.this,PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_setting:
                intent.setClass(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_discussion:
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {

        }
        return super.onOptionsItemSelected(item);
    }

}
