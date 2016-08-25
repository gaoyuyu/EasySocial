package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.bean.Plugin;
import com.gaoyy.easysocial.utils.Tool;

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


        File pluginFile = new File(Tool.getPluginFileDir() + "/" + Tool.getFileName(plugin.getRemote()));
        if (pluginFile.exists())
        {
            pluginViewHolder.itemPluginLayout.setTag(true);
            pluginViewHolder.itemPluginImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_theme));
            pluginViewHolder.itemPluginTag.setText("已下载");
            pluginViewHolder.itemPluginTag.setTextColor(context.getResources().getColor(R.color.green_colorPrimary));

        } else
        {
            pluginViewHolder.itemPluginLayout.setTag(false);
            pluginViewHolder.itemPluginImg.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_default_plugin));
            pluginViewHolder.itemPluginTag.setText("未下载");
            pluginViewHolder.itemPluginTag.setTextColor(context.getResources().getColor(R.color.gray600));
        }


        if (onItemClickListener != null)
        {
            pluginViewHolder.itemPluginLayout.setOnClickListener(new BasicOnClickListener(pluginViewHolder));
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
        private ImageView itemPluginImg;
        private TextView itemPluginName;
        private LinearLayout itemPluginLayout;
        private TextView itemPluginTag;

        public PluginViewHolder(View itemView)
        {
            super(itemView);
            itemPluginImg = (ImageView) itemView.findViewById(R.id.item_plugin_img);
            itemPluginName = (TextView) itemView.findViewById(R.id.item_plugin_name);
            itemPluginLayout = (LinearLayout) itemView.findViewById(R.id.item_plugin_layout);
            itemPluginTag = (TextView) itemView.findViewById(R.id.item_plugin_tag);
        }
    }


}
