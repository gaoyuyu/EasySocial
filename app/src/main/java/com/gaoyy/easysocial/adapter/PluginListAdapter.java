package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.bean.Plugin;
import com.gaoyy.easysocial.utils.Tool;

import java.util.List;

/**
 * Created by gaoyy on 2016/8/22 0022.
 */
public class PluginListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<Plugin> data;
    private LayoutInflater inflater;

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
        PluginViewHolder pluginViewHolder = (PluginViewHolder)holder;
        pluginViewHolder.itemPluginName.setText(Tool.getFileName(data.get(position).getRemote())+"/"+data.get(position).getSize());

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
        public PluginViewHolder(View itemView)
        {
            super(itemView);
            itemPluginImg = (ImageView) itemView.findViewById(R.id.item_plugin_img);
            itemPluginName = (TextView) itemView.findViewById(R.id.item_plugin_name);
        }
    }





}
