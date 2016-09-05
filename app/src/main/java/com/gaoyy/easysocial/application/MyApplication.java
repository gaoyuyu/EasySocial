package com.gaoyy.easysocial.application;

import android.app.Application;
import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        //DroidPlugin
        PluginHelper.getInstance().applicationOnCreate(getBaseContext()); //must behind super.onCreate()

//        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }
}
