package com.gaoyy.easysoical.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.gaoyy.easysoical.view.BasicProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gaoyy on 2016/3/1/0001.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{

    private View rootView;
    private Toolbar loginToolbar;
    private MaterialEditText email;
    private MaterialEditText password;
    private AppCompatActivity activity;
    private AppCompatButton loginBtn;

    private final OkHttpClient client = new OkHttpClient();
    BasicProgressDialog basicProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        activity = (AppCompatActivity) getActivity();
        assignViews(rootView);
        initToolbar();
        setListener();
        return rootView;
    }

    private void assignViews(View rootView)
    {
        loginToolbar = (Toolbar) rootView.findViewById(R.id.login_toolbar);
        email = (MaterialEditText) rootView.findViewById(R.id.email);
        password = (MaterialEditText) rootView.findViewById(R.id.password);
        loginBtn = (AppCompatButton) rootView.findViewById(R.id.login_btn);

        basicProgressDialog = BasicProgressDialog.create(activity);
    }

    public void initToolbar()
    {
        loginToolbar.setTitle(R.string.login);
        activity.setSupportActionBar(loginToolbar);
        //设置返回键可用
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListener()
    {
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.login_btn:

                String[] params = {email.getText().toString(),password.getText().toString()};
                new LoginTask().execute(params);
//                Request request = new Request.Builder()
//                        .url(Global.HOST_URL + "User/login")
//                        .build();
//                client.newCall(request).enqueue(new Callback()
//                {
//                    @Override
//                    public void onFailure(Call call, IOException e)
//                    {
//                        Log.i(Global.TAG,"onFailure");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException
//                    {
//                        Log.i(Global.TAG,"onResponse");
//                        if (!response.isSuccessful())
//                        {
//                            Log.i(Global.TAG,"Unexpected");
//                            throw new IOException("Unexpected code " + response);
//                        }
//                        Headers responseHeaders = response.headers();
//                        for (int i = 0; i < responseHeaders.size(); i++)
//                        {
//                            Log.i(Global.TAG,responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                        }
//
//                        Log.i(Global.TAG,response.body().string());
//                    }
//                });
                break;
        }
    }

    class LoginTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog("Login...", basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", params[0])
                    .add("password", params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "User/login")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
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
            try
            {
                if (0 == Tool.getRepCode(s))
                {
                    SharedPreferences account = activity.getSharedPreferences("account",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = account.edit();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(s);
                    editor.putString("aid", dataJsonObj.getString("aid"));
                    editor.putString("username", dataJsonObj.getString("username"));
                    editor.putString("realname", dataJsonObj.getString("realname"));
                    editor.putString("email", dataJsonObj.getString("email"));
                    editor.putString("password", dataJsonObj.getString("password"));
                    editor.putString("signature", dataJsonObj.getString("signature"));
                    editor.putString("gender", dataJsonObj.getString("gender"));
                    editor.putString("avatar", dataJsonObj.getString("avatar"));
                    editor.commit();
                    Tool.showToast(activity,"登录成功");
                }
                else
                {
                    Tool.showToast(activity,(Tool.getMainJsonObj(s)).getString("data"));
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }

        }
    }
}
