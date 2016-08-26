package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.bean.Plugin;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;
import java.util.List;

/**
 * Created by gaoyy on 2016/8/22 0022.
 */
public class PluginListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<Plugin> data;
    private LayoutInflater inflater;

    private int pluginStatus;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public PluginListAdapter(Context context, List<Plugin> data)
    {
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_plugin_in, parent, false);
        PluginViewHolder pluginViewHolder = new PluginViewHolder(rootView);
        return pluginViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Plugin plugin = data.get(position);
        PluginViewHolder pluginViewHolder = (PluginViewHolder) holder;
        pluginViewHolder.itemPluginName.setText(Tool.getFileName(data.get(position).getRemote()) + "/" + data.get(position).getSize());

        String pluginName = Tool.getFileName(plugin.getRemote());
        File pluginFile = new File(Tool.getPluginFileDir() + "/" + pluginName);

        setButtonVisiableStatus(pluginViewHolder, pluginName, pluginFile);

        if (onItemClickListener != null)
        {
            pluginViewHolder.itemPluginLayout.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
            pluginViewHolder.itemPluginDownload.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
            pluginViewHolder.itemPluginBoth.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
            pluginViewHolder.itemPluginInstall.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
            pluginViewHolder.itemPluginDelete.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
        }

    }

    public void updateData(List<Plugin> data)
    {
        this.data = data;
        notifyDataSetChanged();
    }

    private void setButtonVisiableStatus(PluginViewHolder pluginViewHolder, String pluginName, File pluginFile)
    {
        if(pluginFile.exists())
        {
            if(Tool.checkTargetPackageisInstalled(pluginName))
            {
                updateStatusBtn(pluginViewHolder, Global.AFTER_DOWNLOAD_INSTALL);
                try
                {
                    PackageInfo  packageInfo = PluginManager.getInstance().getPackageInfo(Tool.returnPackageName(pluginName),0);
                    Log.i(Global.TAG,""+(packageInfo == null));
//                    packageInfo.applicationInfo.icon;
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                updateStatusBtn(pluginViewHolder, Global.AFTER_DOWNLOAD_ONLY);
            }
        }
        else
        {
            if(Tool.checkTargetPackageisInstalled(pluginName))
            {
                updateStatusBtn(pluginViewHolder, Global.AFTER_DOWNLOAD_INSTALL);
            }
            else
            {
                updateStatusBtn(pluginViewHolder, -1);
            }
        }
    }


    public void updateStatusBtn(PluginViewHolder pluginViewHolder, int pluginStatus)
    {
        switch (pluginStatus)
        {
            //显示安装，删除
            case Global.AFTER_DOWNLOAD_ONLY:
                pluginViewHolder.itemPluginInstall.setVisibility(View.VISIBLE);
                pluginViewHolder.itemPluginDelete.setVisibility(View.VISIBLE);
                pluginViewHolder.itemPluginDownload.setVisibility(View.GONE);
                pluginViewHolder.itemPluginBoth.setVisibility(View.GONE);
                break;
            //显示删除
            case Global.AFTER_DOWNLOAD_INSTALL:
                pluginViewHolder.itemPluginDelete.setVisibility(View.VISIBLE);
                pluginViewHolder.itemPluginInstall.setVisibility(View.GONE);
                pluginViewHolder.itemPluginDownload.setVisibility(View.GONE);
                pluginViewHolder.itemPluginBoth.setVisibility(View.GONE);
                break;
            //显示删除
            case Global.AFTER_INSTALL:
                pluginViewHolder.itemPluginDelete.setVisibility(View.VISIBLE);
                pluginViewHolder.itemPluginInstall.setVisibility(View.GONE);
                pluginViewHolder.itemPluginDownload.setVisibility(View.GONE);
                pluginViewHolder.itemPluginBoth.setVisibility(View.GONE);
                break;
//         不需做判断了
//            case Global.AFTER_DELETE:
//                /**
//                 *1 只删除插件
//                 *2 删除插件和本地文件
//                 */
//                break;
            //默认 仅下载，下载并安装
            default:
                pluginViewHolder.itemPluginDelete.setVisibility(View.GONE);
                pluginViewHolder.itemPluginInstall.setVisibility(View.GONE);
                pluginViewHolder.itemPluginDownload.setVisibility(View.VISIBLE);
                pluginViewHolder.itemPluginBoth.setVisibility(View.VISIBLE);
                break;
        }
    }

    public class BasicOnClickListener implements View.OnClickListener
    {
        PluginViewHolder pluginViewHolder;

        public BasicOnClickListener(PluginViewHolder pluginViewHolder)
        {
            this.pluginViewHolder = pluginViewHolder;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.item_plugin_layout:
                    boolean tag = (boolean) pluginViewHolder.itemPluginLayout.getTag();
                    if(tag)
                    {
                        Tool.showSnackbar(pluginViewHolder.itemPluginLayout,"该插件已下载");
                    }
                    else
                    {
                        onItemClickListener.onItemClick(pluginViewHolder.itemPluginLayout, pluginViewHolder.getLayoutPosition());
                    }
                    break;
                case R.id.item_plugin_download:
                    onItemClickListener.onItemClick(pluginViewHolder.itemPluginDownload, pluginViewHolder.getLayoutPosition());
                    break;
                case R.id.item_plugin_both:
                    onItemClickListener.onItemClick(pluginViewHolder.itemPluginBoth, pluginViewHolder.getLayoutPosition());
                    break;
                case R.id.item_plugin_install:
                    onItemClickListener.onItemClick(pluginViewHolder.itemPluginInstall, pluginViewHolder.getLayoutPosition());
                    break;
                case R.id.item_plugin_delete:
                    onItemClickListener.onItemClick(pluginViewHolder.itemPluginDelete, pluginViewHolder.getLayoutPosition());
                    break;
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    public static class PluginViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout itemPluginLayout;
        private ImageView itemPluginImg;
        private TextView itemPluginName;
        private AppCompatButton itemPluginDownload;
        private AppCompatButton itemPluginBoth;
        private AppCompatButton itemPluginInstall;
        private AppCompatButton itemPluginDelete;

        public PluginViewHolder(View itemView)
        {
            super(itemView);
            itemPluginLayout = (LinearLayout) itemView.findViewById(R.id.item_plugin_layout);
            itemPluginImg = (ImageView) itemView.findViewById(R.id.item_plugin_img);
            itemPluginName = (TextView) itemView.findViewById(R.id.item_plugin_name);
            itemPluginDownload = (AppCompatButton) itemView.findViewById(R.id.item_plugin_download);
            itemPluginBoth = (AppCompatButton) itemView.findViewById(R.id.item_plugin_both);
            itemPluginInstall = (AppCompatButton) itemView.findViewById(R.id.item_plugin_install);
            itemPluginDelete = (AppCompatButton) itemView.findViewById(R.id.item_plugin_delete);
        }
    }


}
