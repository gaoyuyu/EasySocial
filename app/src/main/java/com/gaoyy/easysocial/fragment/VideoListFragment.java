package com.gaoyy.easysocial.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
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

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.VideoListAdapter;
import com.gaoyy.easysocial.bean.Video;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 视频
 */
public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    private View rootView;
    private LinkedList<Video> videoList;
    private VideoListAdapter videoListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItemPosition;

    private int pageCount = -1;
    private int currentPage = 1;

    private SwipeRefreshLayout fragmentVideoListSrlayout;
    private RecyclerView fragmentVideoListRv;


    public VideoListFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_video_list, container, false);
        assignViews(rootView);
        initData();
        configViews();
        setListener();
        new VideoTask(false).execute(String.valueOf(currentPage));
        return rootView;
    }

    private void assignViews(View rootView)
    {
        fragmentVideoListSrlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_video_list_srlayout);
        fragmentVideoListRv = (RecyclerView) rootView.findViewById(R.id.fragment_video_list_rv);
    }

    private void initData()
    {
        videoList = new LinkedList<Video>();
    }
    private void configViews()
    {
        videoListAdapter = new VideoListAdapter(getActivity(),videoList);
        fragmentVideoListRv.setAdapter(videoListAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentVideoListRv.setLayoutManager(linearLayoutManager);
        fragmentVideoListRv.setItemAnimator(new DefaultItemAnimator());

        //设置刷新时动画的颜色，可以设置4个
        fragmentVideoListSrlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        fragmentVideoListSrlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        fragmentVideoListSrlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        fragmentVideoListRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == videoListAdapter.getItemCount())
                {
                    if (currentPage + 1 > pageCount)
                    {
                        Tool.showSnackbar(recyclerView, ":)到底啦");
                    }
                    else
                    {
                        currentPage = currentPage + 1;
                        new VideoTask(false).execute(String.valueOf(currentPage));
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

    private void setListener()
    {
        fragmentVideoListSrlayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh()
    {
        currentPage = 1;
        new VideoTask(true).execute(String.valueOf(currentPage));
    }

    class VideoTask extends AsyncTask<String, String, LinkedList<Video>>
    {
        private boolean status;

        public VideoTask(boolean status)
        {
            this.status = status;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            fragmentVideoListSrlayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Video> doInBackground(String... params)
        {

            LinkedList<Video> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("pageNum", params[0])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showVideo")
                    .post(formBody)
                    .build();
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "===========================================");
                Log.i(Global.TAG, "RankTask code-->" + Tool.getRepCode(body));
                Log.i(Global.TAG, "RankTask body-->" + body);
                Log.i(Global.TAG, "===========================================");
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(body);
                    pageCount = dataJsonObj.getInt("pageCount");
                    list = gson.fromJson(dataJsonObj.get("videoData").toString(),
                            new TypeToken<LinkedList<Video>>()
                            {
                            }.getType());
                }
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "RankTask doInBackground Exception-->" + e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(LinkedList<Video> s)
        {
            super.onPostExecute(s);
            fragmentVideoListSrlayout.setRefreshing(false);
            if (s != null)
            {
                if (status)
                {
                    videoListAdapter.addItem(s);
                }
                else
                {
                    videoListAdapter.addMoreItem(s);
                }
            }
            else
            {
                Log.i(Global.TAG, "RankTask内部错误");
            }
        }
    }
}
