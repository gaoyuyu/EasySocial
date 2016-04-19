package com.gaoyy.easysoical.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.FavoriteAdapter;
import com.gaoyy.easysoical.bean.Favorite;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
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
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    private View rootView;
    private SwipeRefreshLayout favoriteSrlayout;
    private RecyclerView favoriteRv;
    private FavoriteAdapter favoriteAdapter;
    private LinkedList<Favorite> favLists;
    private int lastVisibleItemPosition;


    private int pageCount = -1;
    private int currentPage = 1;
    private LinearLayoutManager linearLayoutManager;


    private void assignViews(View rootView)
    {
        favoriteSrlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_favorite_srlayout);
        favoriteRv = (RecyclerView) rootView.findViewById(R.id.fragment_favorite_rv);
    }


    public FavoriteFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        assignViews(rootView);
        initData();
        configViews();
        new FavoriteTask(true).execute(String.valueOf(currentPage));
        return rootView;
    }

    private void initData()
    {
        favLists = new LinkedList<Favorite>();
    }

    private void configViews()
    {
        favoriteSrlayout.setOnRefreshListener(this);
        favoriteAdapter = new FavoriteAdapter(getActivity(), favLists);
        favoriteRv.setAdapter(favoriteAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favoriteRv.setLayoutManager(linearLayoutManager);
        favoriteRv.setItemAnimator(new DefaultItemAnimator());

        favoriteRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == favoriteAdapter.getItemCount())
                {
                    if (currentPage + 1 > pageCount)
                    {
                        Tool.showSnackbar(recyclerView, ":)到底啦");
                    }
                    else
                    {
                        currentPage = currentPage + 1;
                        new FavoriteTask(false).execute(String.valueOf(currentPage));
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

    @Override
    public void onRefresh()
    {
        currentPage = 1;
        new FavoriteTask(true).execute(String.valueOf(currentPage));
    }


    class FavoriteTask extends AsyncTask<String, String, LinkedList<Favorite>>
    {
        private boolean status;

        public FavoriteTask(boolean status)
        {
            this.status = status;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            favoriteSrlayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Favorite> doInBackground(String... params)
        {
            LinkedList<Favorite> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("pageNum", params[0])
                    .add("aid", getArguments().getString("aid"))
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showFavList")
                    .post(formBody)
                    .build();
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "FavoriteFragment body-->" + body);
                Log.i(Global.TAG, "FavoriteFragment code-->" + Tool.getRepCode(body));
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(body);
                    pageCount = dataJsonObj.getInt("pageCount");
                    list = gson.fromJson(dataJsonObj.get("favoriteData").toString(),
                            new TypeToken<LinkedList<Favorite>>()
                            {
                            }.getType());
                    Log.i(Global.TAG, "FavoriteFragment list-->" + list.toString());
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "FavoriteFragment doInBackground Exception-->" + e.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(LinkedList<Favorite> s)
        {
            super.onPostExecute(s);
            favoriteSrlayout.setRefreshing(false);
            if (s != null)
            {
                if (status)
                {
                    favoriteAdapter.addItem(s);
                }
                else
                {
                    favoriteAdapter.addMoreItem(s);
                }
            }
            else
            {
                Log.i(Global.TAG, "FavoriteFragment 内部错误");
            }
        }
    }

}
