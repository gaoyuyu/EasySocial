package com.gaoyy.easysocial.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.adapter.PluginListAdapter;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.bean.Plugin;
import com.gaoyy.easysocial.utils.DividerItemDecoration;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class PluginListActivity extends BaseActivity
{
    private RecyclerView pluginInRv;
    private Toolbar pluginInToolbar;
    private BasicProgressDialog basicProgressDialog;
    private List<Plugin> pluginList = null;
    private ProgressBar dialogProgressBar;
    private View dialogView;
    private MaterialDialog materialDialog;
    private TextView dialogRate;
    private PluginListAdapter pluginListAdapter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("android.intent.action.UPDATE_PROGRESS_BROADCAST"))
            {
                int max = intent.getIntExtra("max", -1);
                int total = intent.getIntExtra("total", -1);
                dialogProgressBar.setMax(max);
                dialogProgressBar.setProgress(total);
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                String rate = numberFormat.format((float) total / (float) max * 100);
                dialogRate.setText(rate + "%");
                if (total == max)
                {
                    materialDialog.dismiss();
                    if (pluginListAdapter != null)
                    {
                        pluginListAdapter.notifyDataSetChanged();
                    }
                }
            } else if (intent.getAction().equals("android.intent.action.PLUGIN_SCAN_BROADCAST"))
            {
                Log.i(Global.TAG,"update Adapter");
                if (pluginListAdapter != null)
                {
                    pluginListAdapter.notifyDataSetChanged();
                }
            }
        }
    };


    @Override
    protected void configViewsOnResume()
    {
        super.configViewsOnResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.UPDATE_PROGRESS_BROADCAST");
        intentFilter.addAction("android.intent.action.PLUGIN_SCAN_BROADCAST");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

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
        dialogView = LayoutInflater.from(PluginListActivity.this).inflate(R.layout.dialog_progressbar, null);
        dialogProgressBar = (ProgressBar) dialogView.findViewById(R.id.dialog_progressBar);
        dialogRate = (TextView) dialogView.findViewById(R.id.dialog_rate);

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
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                Log.i(Global.TAG,"========timetask=============");
                Intent intent = new Intent("android.intent.action.PLUGIN_SCAN_BROADCAST");
                sendBroadcast(intent);
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 3 * 1000;
        timer.scheduleAtFixedRate(timerTask, delay,intevalPeriod);
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

    class ScanTask extends AsyncTask<String, String, List<Plugin>>
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

            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }
            return pluginList;
        }

        @Override
        protected void onPostExecute(final List<Plugin> s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if (s != null)
            {
                pluginListAdapter = new PluginListAdapter(PluginListActivity.this, s);
                pluginInRv.setAdapter(pluginListAdapter);
                pluginInRv.addItemDecoration(new DividerItemDecoration(PluginListActivity.this, DividerItemDecoration.VERTICAL_LIST));
                pluginInRv.setLayoutManager(new LinearLayoutManager(PluginListActivity.this, LinearLayoutManager.VERTICAL, false));
                pluginInRv.setItemAnimator(new DefaultItemAnimator());

                pluginListAdapter.setOnItemClickListener(new PluginListAdapter.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        if (materialDialog == null)
                        {
                            materialDialog = getDownloadingDialog("下载中...", dialogView);
                        }
                        materialDialog.show();
                        download(s.get(position).getRemote());


                    }
                });
            }
        }
    }

    public MaterialDialog getDownloadingDialog(String title, View view)
    {
        MaterialDialog materialDialog = new MaterialDialog(PluginListActivity.this)
                .setTitle(title)
                .setCanceledOnTouchOutside(false)
                .setContentView(view);
        return materialDialog;
    }

    public void download(final String url)
    {
        final String destFileDir = Tool.getPluginFileDir();
        File pluginFile = new File(destFileDir);
        if (!pluginFile.exists())
        {
            pluginFile.mkdir();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = Tool.getOkHttpClient().newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                int total = 0;
                FileOutputStream fos = null;
                Intent intent = new Intent("android.intent.action.UPDATE_PROGRESS_BROADCAST");
                try
                {
                    is = response.body().byteStream();
                    intent.putExtra("max", (int) response.body().contentLength());
                    File file = new File(destFileDir, Tool.getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                        total += len;
                        intent.putExtra("total", total);
                        //发送广播更新数据和进度条
                        sendBroadcast(intent);
                    }
                    fos.flush();
                }
                catch (IOException e)
                {
                    Log.i(Global.TAG, "catch Exception when downloading plugin：" + e.toString());
                }
                finally
                {
                    try
                    {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                    }
                    catch (IOException e)
                    {
                        Log.i(Global.TAG, "catch Exception when close IO：" + e.toString());
                    }
                }
            }
        });
    }
}
