package com.gaoyy.easysoical.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.ListAdapter;
import com.gaoyy.easysoical.bean.Tweet;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class HomeFragment extends Fragment
{
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView swRecyclerView;
    private LinkedList<Tweet> data;
    private ListAdapter listAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItemPosition;

    private final OkHttpClient client = new OkHttpClient();

    private int pageCount = -1;
    private int currentPage = 1;


    private void assignViews(View rootView)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swRecyclerView = (RecyclerView) rootView.findViewById(R.id.sw_recyclerView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        assignViews(rootView);
        initData();
        configViews();
        new HomeTask(true).execute(String.valueOf(currentPage));
        return rootView;
    }

    public void initData()
    {
        String title = getArguments().getString("title");
        data = new LinkedList<Tweet>();
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
                currentPage=1;
                new HomeTask(true).execute(String.valueOf(currentPage));
            }
        });

        swRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition+1 == listAdapter.getItemCount())
                {
                    if (currentPage <= pageCount)
                    {
                        currentPage = currentPage + 1;
                        new HomeTask(false).execute(String.valueOf(currentPage));
                    }
                    else
                    {
                        Tool.showSnackbar(recyclerView, ":)到底啦");
                    }
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

    class HomeTask extends AsyncTask<String, String, LinkedList<Tweet>>
    {
        private boolean status;
        public HomeTask(boolean status)
        {
            this.status = status;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Tweet> doInBackground(String... params)
        {
            LinkedList<Tweet> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("pageNum", params[0])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showTweet")
                    .post(formBody)
                    .build();
            try
            {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Gson gson = new Gson();
                JSONObject jsonObject = null;
                JSONObject dataObject = null;
                jsonObject = new JSONObject(body);


                dataObject = (JSONObject) jsonObject.get("data");
                pageCount = dataObject.getInt("pageCount");
                list = gson.fromJson(dataObject.get("pageData").toString(),
                        new TypeToken<LinkedList<Tweet>>()
                        {
                        }.getType());
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(LinkedList<Tweet> s)
        {
            super.onPostExecute(s);
            Log.i(Global.TAG, "s.size------>" + s.size());
            swipeRefreshLayout.setRefreshing(false);
//            if(isDownLoadMore)
//            {
//                listAdapter.addMoreItem(s);
//                isDownLoadMore = false;
//            }
//            else
//            {
//                listAdapter.addItem(s);
//            }
            if(status)
            {
                listAdapter.addItem(s);
            }
            else
            {
                listAdapter.addMoreItem(s);
            }

        }
    }
}
