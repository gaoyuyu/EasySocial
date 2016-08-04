package com.gaoyy.easysocial.ui;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.GuidePagerAdapter;
import com.gaoyy.easysocial.base.BaseActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class GuideActivity extends BaseActivity
{

    private ViewPager guideViewpager;
    private GuidePagerAdapter guidePagerAdapter;
    private SystemBarTintManager tintManager;
    private int[] colors = {R.color.indigo_colorPrimary, R.color.purple_colorPrimaryDark, R.color.blue_colorPrimaryDark};
    public ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        guideViewpager = (ViewPager) findViewById(R.id.guide_viewpager);

    }

    @Override
    protected void configViews()
    {
        super.configViews();
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(colors[0]);
        tintManager.setStatusBarTintEnabled(true);
        guidePagerAdapter = new GuidePagerAdapter(getSupportFragmentManager(),this);
        guideViewpager.setAdapter(guidePagerAdapter);
        guideViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int nextPosition = position + 1;
                int color = (Integer) argbEvaluator.evaluate(positionOffset, getPageColor(position, colors),
                        getPageColor(nextPosition, colors));
                guideViewpager.setBackgroundColor(color);
                tintManager.setStatusBarTintColor(color);
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    private int getPageColor(int position, int[] color)
    {
        if (position > (color.length - 1))
        {
            return Color.TRANSPARENT;
        }
        return getResources().getColor(color[position]);

    }
}
