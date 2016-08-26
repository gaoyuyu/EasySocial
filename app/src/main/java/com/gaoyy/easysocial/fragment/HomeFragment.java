package com.gaoyy.easysocial.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.ListAdapter;
import com.gaoyy.easysocial.bean.Comment;
import com.gaoyy.easysocial.bean.Tweet;
import com.gaoyy.easysocial.ui.LoginActivity;
import com.gaoyy.easysocial.ui.PhotoActivity;
import com.gaoyy.easysocial.ui.PublishActivity;
import com.gaoyy.easysocial.ui.ReplyActivity;
import com.gaoyy.easysocial.ui.TweetDetailActivity;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
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
 * Created by gaoyy on 2016/2/16/0016.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ListAdapter.OnItemClickListener
{
    private View rootView;
    private LinkedList<Tweet> tweetList;
    private ListAdapter listAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItemPosition;

    private int pageCount = -1;
    private int currentPage = 1;


    private BasicProgressDialog basicProgressDialog;
    private SwipeRefreshLayout fragmentHomeSrlayout;
    private RecyclerView fragmentHomeRv;
    private FloatingActionButton fragmentHomeFab;

    private SharedPreferences account;
    private String isPersonal;

    private static final int REPLY_REQUEST_CODE = 600;

    private void assignViews(View rootView)
    {
        fragmentHomeSrlayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_home_srlayout);
        fragmentHomeRv = (RecyclerView) rootView.findViewById(R.id.fragment_home_rv);
        fragmentHomeFab = (FloatingActionButton) rootView.findViewById(R.id.fragment_home_fab);

        basicProgressDialog = BasicProgressDialog.create(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        account = getActivity().getSharedPreferences("account", Activity.MODE_PRIVATE);
        isPersonal = (getArguments().getString("isPersonal") == null) ? "" : getArguments().getString("isPersonal");
        assignViews(rootView);
        initData();
        configViews();
        setListener();
        new HomeTask(true).execute(String.valueOf(currentPage));
        return rootView;
    }


    public void initData()
    {
        tweetList = new LinkedList<Tweet>();
    }

    public void configViews()
    {
        listAdapter = new ListAdapter(getActivity(), tweetList);
        fragmentHomeRv.setAdapter(listAdapter);
        //设置布局
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentHomeRv.setLayoutManager(linearLayoutManager);
        fragmentHomeRv.setItemAnimator(new DefaultItemAnimator());

        //设置刷新时动画的颜色，可以设置4个
        fragmentHomeSrlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        fragmentHomeSrlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        fragmentHomeSrlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        fragmentHomeRv.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == listAdapter.getItemCount())
                {
                    if (currentPage + 1 > pageCount)
                    {
                        Tool.showSnackbar(recyclerView, ":)到底啦");
                    }
                    else
                    {
                        currentPage = currentPage + 1;
                        new HomeTask(false).execute(String.valueOf(currentPage));
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
        fragmentHomeFab.setOnClickListener(this);
        fragmentHomeSrlayout.setOnRefreshListener(this);
        listAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.fragment_home_fab:
                if (Tool.isLogin(getActivity()))
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PublishActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Tool.showToast(getActivity(), "请先登录 : )");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void onRefresh()
    {
        currentPage = 1;
        new HomeTask(true).execute(String.valueOf(currentPage));
    }

    @Override
    public void onItemClick(View view, int position)
    {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.item_home_tweimg:
                intent.putExtra("url",tweetList.get(position).getPicture());
                intent.setClass(getActivity(),PhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.item_home_cardview:
                if (Tool.isLogin(getActivity()))
                {
                    intent.setClass(getActivity(), TweetDetailActivity.class);
                    Tweet tweet = (Tweet) ((CardView) view).getTag();
                    intent.putExtra("tweet", tweet);
                    startActivity(intent);
                }
                else
                {
                    Tool.showToast(getActivity(), "请先登录 : )");
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.item_home_com_layout:
                if(Tool.isLogin(getActivity()))
                {
                    Comment comment = new Comment();
                    comment.setTid(tweetList.get(position).getTid());
                    intent.putExtra("comment", comment);
                    intent.putExtra("position", position);
                    intent.setClass(getActivity(), ReplyActivity.class);
                    startActivityForResult(intent,REPLY_REQUEST_CODE);
                }
                else
                {
                    Tool.showToast(getActivity(), "请先登录 : )");
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.item_home_fav_layout:
                if (Tool.isLogin(getActivity()))
                {
                    Tweet currentTweet = tweetList.get(position);
                    if (currentTweet.getIsfavor().equals("1"))
                    {
                        Tool.showSnackbar(view, "已点过赞~");
                    }
                    else
                    {
                        SharedPreferences account = getActivity().getSharedPreferences("account", Activity.MODE_PRIVATE);
                        String[] params = {currentTweet.getTid(), account.getString("aid", "")};
                        new doFavorTask(currentTweet, position).execute(params);
                    }
                }
                else
                {
                    Tool.showToast(getActivity(), "请先登录 : )");
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.item_home_share_layout:
                Tool.showShare(getActivity(),tweetList.get(position).getPicture(),tweetList.get(position).getContent());
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("TAG", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REPLY_REQUEST_CODE)
        {
            if(resultCode == getActivity().RESULT_OK)
            {
                int position = data.getIntExtra("position",-1);
                Log.i(Global.TAG,"=onActivityResult=frag==>"+position);
                Tweet tweet = tweetList.get(position);
                tweet.setComment_count(""+(Integer.valueOf(tweet.getComment_count())+1));
                listAdapter.updateFromPosition(position,tweet);
            }
        }

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
            fragmentHomeSrlayout.setRefreshing(true);
        }

        @Override
        protected LinkedList<Tweet> doInBackground(String... params)
        {

            LinkedList<Tweet> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("pageNum", params[0])
                    .add("aid", Tool.isLogin(getActivity())?account.getString("aid", ""):"")
                    .add("isPersonal", isPersonal)
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showTweet")
                    .post(formBody)
                    .build();
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(body);
                    pageCount = dataJsonObj.getInt("pageCount");
                    list = gson.fromJson(dataJsonObj.get("pageData").toString(),
                            new TypeToken<LinkedList<Tweet>>()
                            {
                            }.getType());
                }
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
            fragmentHomeSrlayout.setRefreshing(false);
            if (s != null)
            {
                if (status)
                {
                    listAdapter.addItem(s);
                }
                else
                {
                    listAdapter.addMoreItem(s);
                }
            }
            else
            {
                Log.i(Global.TAG, "内部错误");
            }
        }
    }

    class doFavorTask extends AsyncTask<String, String, String>
    {
        private Tweet currentTweet;

        private int position;

        public doFavorTask(Tweet currentTweet, int position)
        {
            this.currentTweet = currentTweet;
            this.position = position;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog(getResources().getString(R.string.request), basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("tid", params[0])
                    .add("aid", params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/doFavor")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, "body--->" + body);
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

            return body;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if (0 == Tool.getRepCode(s))
            {
                Tool.showSnackbar(rootView, "点赞成功 :)");
                currentTweet.setIsfavor("1");
                currentTweet.setFavorite_count((Integer.valueOf(currentTweet.getFavorite_count()) + 1) + "");
                listAdapter.updateFromPosition(position, currentTweet);
            }
            else
            {
                Tool.showSnackbar(rootView, "点赞失败 :(");
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(Global.TAG,"HomeFragment onResume");
    }

}
