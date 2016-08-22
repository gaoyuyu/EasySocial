package com.gaoyy.easysocial.ui;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.PluginListAdapter;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.bean.Plugin;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class PluginListActivity extends BaseActivity
{
    private RecyclerView pluginInRv;
    private Toolbar pluginInToolbar;
    private BasicProgressDialog basicProgressDialog;
    private List<Plugin> pluginList = null;

    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_plugin_list);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        pluginInToolbar = (Toolbar) findViewById(R.id.plugin_in_toolbar);
        pluginInRv = (RecyclerView) findViewById(R.id.plugin_in_rv);
        basicProgressDialog = BasicProgressDialog.create(this);

    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(pluginInToolbar, R.string.plugin, true, colors);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        new ScanTask().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ScanTask extends AsyncTask<String, String,  List<Plugin>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog(getResources().getString(R.string.request), basicProgressDialog);
        }

        @Override
        protected List<Plugin> doInBackground(String... params)
        {
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/scanPlugin")
                    .build();
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();
                if (0 == Tool.getRepCode(body))
                {
                    Gson gson = new Gson();
                    JSONObject mainJsonObj = Tool.getMainJsonObj(body);
                    pluginList = gson.fromJson(mainJsonObj.get("data").toString(),
                            new TypeToken<List<Plugin>>()
                            {
                            }.getType());
                }

                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }
            return pluginList;
        }

        @Override
        protected void onPostExecute(List<Plugin> s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if(s!= null)
            {
                PluginListAdapter pluginListAdapter = new PluginListAdapter(PluginListActivity.this,s);
                pluginInRv.setAdapter(pluginListAdapter);
                pluginInRv.setLayoutManager(new GridLayoutManager(PluginListActivity.this,4));

            }
        }
    }
}
