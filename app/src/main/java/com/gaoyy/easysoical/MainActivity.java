package com.gaoyy.easysoical;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gaoyy.easysoical.fragment.LoginFragment;
import com.gaoyy.easysoical.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private NavigationView navigationView;
    private MainFragment mainFragment;

    //记录当前正在使用的fragment
    private Fragment isFragment;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView img = (ImageView) headerView.findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (loginFragment == null)
                {
                    loginFragment = new LoginFragment();
                }
                ((MainFragment)isFragment).getDrawerLayout().closeDrawer(GravityCompat.START);
                switchContent(isFragment, loginFragment);
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
            isFragment = mainFragment;
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
        if (isFragment != to)
        {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded())
            {    // 先判断是否被add过
                ft.hide(from).add(R.id.layout_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }


//    @Override
//    public void onBackPressed()
//    {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START))
//        {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else
//        {
//            super.onBackPressed();
//        }
//    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
            Toast.makeText(MainActivity.this,"activity",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
