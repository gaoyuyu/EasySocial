package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.fragment.GuideFragment;

import java.util.ArrayList;

/**
 * Created by gaoyy on 2016/8/3 0003.
 */

public class GuidePagerAdapter extends FragmentPagerAdapter
{
    public String[] texts;
    public Context context;

    public GuidePagerAdapter(FragmentManager fm,Context context)
    {
        super(fm);
        this.context = context;
        this.texts  = context.getResources().getStringArray(R.array.guide);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = new GuideFragment();
        Bundle b = new Bundle();
        b.putString("text", texts[position]);
        b.putBoolean("isShowBtn",(position == (texts.length-1)));
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return texts.length;
    }

}
