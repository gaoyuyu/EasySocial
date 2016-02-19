package com.gaoyy.easysoical;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.gaoyy.easysoical.fragment.MainFragment;

public class MainActivity extends AppCompatActivity
{

    private NavigationView navigationView;
    private MainFragment mainFragment;
    private DrawerLayout drawerLayout;

    //记录当前正在使用的fragment
    private Fragment isFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        initFragment(savedInstanceState);


    }

    public void initFragment(Bundle savedInstanceState)
    {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if(savedInstanceState==null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(mainFragment==null) {
                mainFragment = new MainFragment();
            }
            isFragment = mainFragment;
            ft.replace(R.id.main, mainFragment).commit();
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

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
