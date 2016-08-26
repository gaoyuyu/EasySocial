package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.fragment.MainFragment;
import com.gaoyy.easysocial.utils.Tool;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.morgoo.droidplugin.pm.PluginManager;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener
{
    private DrawerLayout mainDrawerlayout;
    private static NavigationView mainNav;
    private MainFragment mainFragment;
    private SharedPreferences sbc;
    private SharedPreferences account;

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


//    public static class NavMenuReceiver extends BroadcastReceiver
//    {
//
//        public NavMenuReceiver()
//        {
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            if(intent.getAction().equals("android.intent.action.NAV_MENU_BROADCAST"))
//            {
//                switch (intent.getIntExtra("pluginTag",-1))
//                {
//                    case Global.INSTALL_TAG:
//                        mainNav.getMenu().add(R.id.main,1234,10,"NewsReader");
//                        mainNav.getMenu().findItem(1234).setIcon(context.getResources().getDrawable(R.mipmap.ic_default_plugin));
//                        break;
//                    case Global.DELETE_TAG:
//                        mainNav.getMenu().removeItem(1234);
//                        break;
//                }
//            }
//        }
//    }


    @Override
    public void initContentView()
    {
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        sbc = getSharedPreferences("sbc", Activity.MODE_PRIVATE);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        mainDrawerlayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mainNav = (NavigationView) findViewById(R.id.main_nav);
        mainDrawerlayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);


    }


    @Override
    protected void configViewsOnResume()
    {
        super.configViewsOnResume();
        mainNav.getMenu().findItem(R.id.nav_home).setChecked(true);
        View headerView = mainNav.getHeaderView(0);
        SimpleDraweeView navHeaderBg = (SimpleDraweeView) headerView.findViewById(R.id.nav_header_bg);
        if (!account.getString("personalbg", "").equals(""))
        {
            //navHeaderBg.setImageURI(Uri.parse("file://" + account.getString("personalbg", "")));
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void setListener()
    {
        super.setListener();
        mainNav.setNavigationItemSelectedListener(this);
        View headerView = mainNav.getHeaderView(0);
        SimpleDraweeView avatar = (SimpleDraweeView) headerView.findViewById(R.id.nav_header_ava);
        avatar.setOnClickListener(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState)
    {
        super.initFragment(savedInstanceState);
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
            ft.replace(R.id.main_layout, mainFragment).commit();
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
            {
                // 先判断是否被add过
                ft.hide(from).add(R.id.main_layout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else
            {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
        mainDrawerlayout.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed()
    {
        if (mainDrawerlayout.isDrawerOpen(GravityCompat.START))
        {
            mainDrawerlayout.closeDrawer(GravityCompat.START);
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
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.nav_home:
                if (!item.isChecked())
                {
                    intent.setClass(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.nav_personal:
                if (Tool.isLogin(this))
                {
                    intent.setClass(MainActivity.this, PersonalActivity.class);
                } else
                {
                    Tool.showToast(this, "请先登录 : )");
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.nav_setting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_feedback:
                intent.setClass(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                Tool.showShare(this);
                break;
            case R.id.nav_theme:
                showMaterialDialog();
                break;
            case R.id.nav_about:
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_plugin:
                intent.setClass(MainActivity.this, PluginListActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }

    private void showMaterialDialog()
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_pick_color, null);
        final MaterialDialog materialDialog = new MaterialDialog(this)
                .setTitle("主题")
                .setCanceledOnTouchOutside(true)
                .setContentView(contentView);
        CircularImageView indigoColor = (CircularImageView) contentView.findViewById(R.id.indigo_color);
        CircularImageView lightGreenColor = (CircularImageView) contentView.findViewById(R.id.light_green_color);
        CircularImageView blueColor = (CircularImageView) contentView.findViewById(R.id.blue_color);
        CircularImageView pinkColor = (CircularImageView) contentView.findViewById(R.id.pink_color);
        CircularImageView deepOrangeColor = (CircularImageView) contentView.findViewById(R.id.deep_orange_color);
        CircularImageView greenColor = (CircularImageView) contentView.findViewById(R.id.green_color);
        CircularImageView purpleColor = (CircularImageView) contentView.findViewById(R.id.purple_color);
        CircularImageView orangeColor = (CircularImageView) contentView.findViewById(R.id.orange_color);
        CircularImageView deepPurpleColor = (CircularImageView) contentView.findViewById(R.id.deep_purple_color);

        indigoColor.setOnClickListener(this);
        lightGreenColor.setOnClickListener(this);
        blueColor.setOnClickListener(this);
        pinkColor.setOnClickListener(this);
        deepOrangeColor.setOnClickListener(this);
        greenColor.setOnClickListener(this);
        purpleColor.setOnClickListener(this);
        orangeColor.setOnClickListener(this);
        deepPurpleColor.setOnClickListener(this);

        materialDialog.setPositiveButton("恢复默认", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                materialDialog.dismiss();
                SharedPreferences.Editor editor = sbc.edit();
                editor.putInt("color", -1);
                editor.commit();
                finish();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });


        materialDialog.show();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            List<PackageInfo> pkglist = PluginManager.getInstance().getInstalledPackages(0);
            if(pkglist.size() == 0)
            {
                return super.onCreateOptionsMenu(menu);
            }
            for(int i=0;i<pkglist.size();i++)
            {
                menu.add(0,i,i,pkglist.get(i).applicationInfo.packageName);
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            List<PackageInfo> pkglist = PluginManager.getInstance().getInstalledPackages(0);
            for(int i=0;i<pkglist.size();i++)
            {
                if(item.getItemId() == i)
                {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(pkglist.get(i).packageName);
                    startActivity(intent);
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        SharedPreferences.Editor editor = sbc.edit();
        int id = v.getId();
        Intent restartIntent = new Intent(this, MainActivity.class);
        switch (id)
        {
            case R.id.nav_header_ava:
                if (!Tool.isLogin(this))
                {
                    Tool.showSnackbar(v, "请先登录");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.indigo_color:
                editor.putInt("color", 0);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.light_green_color:
                editor.putInt("color", 1);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.blue_color:
                editor.putInt("color", 2);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.pink_color:
                editor.putInt("color", 3);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.deep_orange_color:
                editor.putInt("color", 4);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.green_color:
                editor.putInt("color", 5);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.purple_color:
                editor.putInt("color", 6);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.orange_color:
                editor.putInt("color", 7);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.deep_purple_color:
                editor.putInt("color", 8);
                finish();
                startActivity(restartIntent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

        }
        editor.commit();

    }
}
