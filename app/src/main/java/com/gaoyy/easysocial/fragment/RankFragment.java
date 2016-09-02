package com.gaoyy.easysocial.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.RankAdapter;
import com.gaoyy.easysocial.base.LazyFragment;
import com.gaoyy.easysocial.bean.Rank;
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
 * 排行榜
 */
public class RankFragment extends LazyFragment implements SwipeRefreshLayout.OnRefreshListener
{
    private View rootView;
    private RelativeLayout fragmentRankLayout;
    private SwipeRefreshLayout fragmentRankSrlayout;
    private RecyclerView fragmentRankRv;
    private LinkedList<Rank> rankList;
    private RankAdapter rankAdapter;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    private void assignViews(View rootView)
    {
        fragmentRankLayout = (RelativeLayout) rootView.findViewById(R.id.fragment_rank_layout);
        fragmentRankSrlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_rank_srlayout);
        fragmentRankRv = (RecyclerView) rootView.findViewById(R.id.fragment_rank_rv);
    }


    public RankFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_rank, container, false);
        }
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    protected void lazyLoad()
    {
        if (!isPrepared || !isVisible)
        {
            return;
        }
        assignViews(rootView);
        initData();
        configViews();
        new RankTask().execute();
    }

    private void initData()
    {
        rankList = new LinkedList<Rank>();
    }

    private void configViews()
    {
        fragmentRankSrlayout.setOnRefreshListener(this);
        rankAdapter = new RankAdapter(getActivity(), rankList);
        fragmentRankRv.setAdapter(rankAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentRankRv.setLayoutManager(linearLayoutManager);
        fragmentRankRv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh()
    {
        new RankTask().execute();
    }

    class RankTask extends AsyncTask<String, String, LinkedList<Rank>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            fragmentRankSrlayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Rank> doInBackground(String... params)
        {
            LinkedList<Rank> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showTweetRank")
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
                    JSONObject mainJsonObj = Tool.getMainJsonObj(body);
                    list = gson.fromJson(mainJsonObj.get("data").toString(),
                            new TypeToken<LinkedList<Rank>>()
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
        protected void onPostExecute(LinkedList<Rank> ranks)
        {
            super.onPostExecute(ranks);
            fragmentRankSrlayout.setRefreshing(false);
            if(ranks != null)
            {
                rankAdapter.addItem(ranks);
            }
        }
    }

}
