package com.gaoyy.easysoical.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.ListAdapter;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class PageFragment extends Fragment
{
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView swRecyclerView;
    private LinkedList<String> data;
    private ListAdapter listAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItemPosition;

    private void assignViews(View rootView)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swRecyclerView = (RecyclerView) rootView.findViewById(R.id.sw_recyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.page_fragment, container, false);
        assignViews(rootView);
        initData();
        configViews();
        return rootView;
    }

    public void initData()
    {
        String title = getArguments().getString("title");
        data = new LinkedList<String>();
        for (int i = 0; i < 15; i++)
        {
            data.add(i, title + "@" + i);
        }
    }

    public void configViews()
    {
        listAdapter = new ListAdapter(getActivity(), data);
        swRecyclerView.setAdapter(listAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        swRecyclerView.setLayoutManager(linearLayoutManager);
        swRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        LinkedList<String> newDatas = new LinkedList<String>();
                        for (int i = 0; i < 5; i++)
                        {
                            int index = i + 1;
                            newDatas.add("new item" + index+""+(int)(Math.random()*100));
                        }
                        listAdapter.addItem(newDatas);
                        swRecyclerView.scrollToPosition(0);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        swRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == listAdapter.getItemCount())
                {
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LinkedList<String> newDatas = new LinkedList<String>();
                            for (int i = 0; i < 5; i++)
                            {
                                int index = i + 1;
                                newDatas.add("more item" + index+""+(int)(Math.random()*100));
                            }
                            listAdapter.addMoreItem(newDatas);
                        }
                    }, 1500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
