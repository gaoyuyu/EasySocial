package com.gaoyy.easysocial.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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
import com.morgoo.droidplugin.pm.PluginManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class PluginListActivity extends BaseActivity implements PluginListAdapter.OnItemClickListener
{
    private RecyclerView pluginInRv;
    private Toolbar pluginInToolbar;
    private BasicProgressDialog basicProgressDialog;
    private List<Plugin> pluginList = new ArrayList<Plugin>();
    private ProgressBar dialogProgressBar;
    private View loadingdialogView;
    private View hintDialogView;
    private MaterialDialog loadingDialog;
    private MaterialDialog hintDialog;
    private TextView dialogRate;
    private CheckBox hintCheckbox;
    private PluginListAdapter pluginListAdapter;

    private static final int INSTALL_TAG = 630;
    private static final int DELETE_TAG = 530;

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
                    loadingDialog.dismiss();
                    if (intent.getBooleanExtra("isNeedInstall", false))
                    {
                        new PluginTask(INSTALL_TAG,intent.getStringExtra("url")).execute();
                    }
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

        loadingdialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progressbar, null);
        dialogProgressBar = (ProgressBar) loadingdialogView.findViewById(R.id.dialog_progressBar);
        dialogRate = (TextView) loadingdialogView.findViewById(R.id.dialog_rate);
        if (loadingDialog == null)
        {
            loadingDialog = getDownloadingDialog("下载中...", loadingdialogView);
        }

        hintDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_plugin_hint, null);
        hintCheckbox = (CheckBox) hintDialogView.findViewById(R.id.plugin_hint_checkBox);
        if (hintDialog == null)
        {
            hintDialog = getDownloadingDialog("提示", hintDialogView);
        }

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
        pluginListAdapter = new PluginListAdapter(PluginListActivity.this, pluginList);
        pluginInRv.setAdapter(pluginListAdapter);
        pluginInRv.addItemDecoration(new DividerItemDecoration(PluginListActivity.this, DividerItemDecoration.VERTICAL_LIST));
        pluginInRv.setLayoutManager(new LinearLayoutManager(PluginListActivity.this, LinearLayoutManager.VERTICAL, false));
        pluginInRv.setItemAnimator(new DefaultItemAnimator());
        pluginListAdapter.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(View view, final int position)
    {
        Plugin plugin = pluginList.get(position);
        final String remote = plugin.getRemote();
        switch (view.getId())
        {
            case R.id.item_plugin_download:
                loadingDialog.show();
                download(remote, false);
                break;
            case R.id.item_plugin_both:
                loadingDialog.show();
                download(pluginList.get(position).getRemote(), true);
                break;
            case R.id.item_plugin_install:
                new PluginTask(INSTALL_TAG,remote).execute();
                break;
            case R.id.item_plugin_delete:
                hintDialog.setPositiveButton("确定", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (hintCheckbox.isChecked())
                        {
                            new File(Tool.getPluginFileDir() + "/" + Tool.getFileName(pluginList.get(position).getRemote())).delete();
                        }
                        new PluginTask(DELETE_TAG,remote).execute();
                        hintDialog.dismiss();
                        pluginListAdapter.notifyDataSetChanged();

                    }
                });
                hintDialog.setNegativeButton("取消", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hintDialog.dismiss();
                    }
                });
                hintDialog.show();
                break;
        }
    }

    class PluginTask extends AsyncTask<String, String, String>
    {
        private int tag;
        private String remote;

        public PluginTask(int tag,String remote)
        {
            this.tag = tag;
            this.remote = remote;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog(getResources().getString(R.string.loading), basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            String label = "";
            try
            {
                switch (tag)
                {
                    case INSTALL_TAG:
                        int res = PluginManager.getInstance().installPackage(Tool.getPluginFileDir() + "/" + Tool.getFileName(remote), 0);
                        if(res == 1)
                        {
                            label = "安装成功";
                        }
                        break;
                    case DELETE_TAG:
                        PluginManager.getInstance().deletePackage(Tool.returnPackageName(Tool.getFileName(remote)), 0);
                        label = "已删除";
                        break;
                }
            }
            catch (RemoteException e)
            {
                Log.i(Global.TAG,"TAG : "+tag+"   catch Exception : "+e.toString());
            }
            return label;
        }

        @Override
        protected void onPostExecute(String i)
        {
            super.onPostExecute(i);
            Tool.stopProgressDialog(basicProgressDialog);
            Tool.showSnackbar(pluginInRv,i);
            pluginListAdapter.notifyDataSetChanged();
        }
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
        protected void onPostExecute(List<Plugin> s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if (s != null)
            {
                pluginList = s;
                pluginListAdapter.updateData(s);
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

    public void download(final String url, final boolean isNeedInstall)
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
                    intent.putExtra("isNeedInstall", isNeedInstall);
                    intent.putExtra("max", (int) response.body().contentLength());
                    File file = new File(destFileDir, Tool.getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                        total += len;
                        intent.putExtra("url", url);
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
