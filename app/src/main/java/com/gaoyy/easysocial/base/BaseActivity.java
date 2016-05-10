package com.gaoyy.easysocial.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseActivity extends AppCompatActivity
{
    /**
     * setContentView here
     */
    public abstract void initContentView();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initContentView();
        assignViews();
        initToolbar();
        initData();
        configViews();
        setListener();
        initFragment(savedInstanceState);
        executeTask();
    }

    /**
     * assign Views
     */
    protected void assignViews()
    {

    }

    /**
     * initialization Toolbar
     */
    protected void initToolbar()
    {

    }

    /**
     * should override in ChildClass if in need
     */
    public void initToolbar(Toolbar toolbar, int titleId, boolean enabled, int[] colors)
    {
        toolbar.setTitle(titleId);
        toolbar.setBackgroundColor(getResources().getColor(colors[0]));
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(enabled);
        getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(colors[1]);
        tintManager.setStatusBarTintEnabled(true);
    }

    /**
     * initialization Data
     */
    protected void initData()
    {

    }

    /**
     * config Views
     */
    protected void configViews()
    {

    }

    /**
     * setListener For all Views
     */
    protected void setListener()
    {

    }


    protected void initFragment(Bundle savedInstanceState)
    {

    }

    /**
     * do AsyncTsak here
     */
    protected void executeTask()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        configViewsOnResume();
    }

    /**
     * may override this method in onResume
     */
    protected void configViewsOnResume()
    {

    }
}
