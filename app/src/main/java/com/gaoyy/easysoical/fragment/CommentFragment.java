package com.gaoyy.easysoical.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.adapter.CommentAdapter;
import com.gaoyy.easysoical.bean.Comment;
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
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment
{

    private View rootView;
    private RecyclerView commentRv;
    private LinkedList<Comment> commentList;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;

    private final OkHttpClient client = new OkHttpClient();

    private void assignViews(View rootView)
    {
        commentRv = (RecyclerView) rootView.findViewById(R.id.comment_rv);
    }

    public CommentFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        assignViews(rootView);
        initData();
        configViews();
        new CommentTask().execute();
        return rootView;
    }

    private void initData()
    {
        commentList = new LinkedList<Comment>();
    }

    private void configViews()
    {
        commentAdapter = new CommentAdapter(getActivity(), commentList);
        commentRv.setAdapter(commentAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentRv.setLayoutManager(linearLayoutManager);
    }

    class CommentTask extends AsyncTask<String, String, LinkedList<Comment>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected LinkedList<Comment> doInBackground(String... params)
        {
            String tid = ((Tweet) (getArguments().getSerializable("tweet"))).getTid();

            LinkedList<Comment> list = null;
            RequestBody formBody = new FormBody.Builder()
                    .add("tid", tid)
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/showCommentList")
                    .post(formBody)
                    .build();
            try
            {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject jsonObj = Tool.getMainJsonObj(body);
                    list = gson.fromJson(jsonObj.get("data").toString(),
                            new TypeToken<LinkedList<Comment>>()
                            {
                            }.getType());
                    Log.i(Global.TAG, "list-->" + list.toString());
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(LinkedList<Comment> s)
        {
            super.onPostExecute(s);
            commentAdapter.addItem(s);
        }
    }


}
