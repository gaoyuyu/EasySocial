package com.gaoyy.easysocial.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class PageAdapter extends FragmentStatePagerAdapter
{
    private String[] pagerTitle;
    private List<Fragment> fragmentList;

    public PageAdapter(FragmentManager fm, String[] pagerTitle, List<Fragment> fragmentList)
    {
        super(fm);
        this.pagerTitle = pagerTitle;
        this.fragmentList = fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return pagerTitle[position];
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return pagerTitle.length;
    }
}
