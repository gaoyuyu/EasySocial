package com.gaoyy.easysoical.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoyy.easysoical.R;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<String> data;
    private Context context;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    public ListAdapter(Context context, LinkedList<String> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position + 1 == getItemCount())
        {
            return TYPE_FOOTER;
        } else
        {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_FOOTER)
        {
            View rootView = inflater.inflate(R.layout.footer, parent, false);
            FooterViewHolder footerViewHolder = new FooterViewHolder(rootView);
            return footerViewHolder;
        } else if (viewType == TYPE_ITEM)
        {
            View rootView = inflater.inflate(R.layout.item_home, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(rootView);
            return itemViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ItemViewHolder)
        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.tv.setText(data.get(position));
        } else if (holder instanceof FooterViewHolder)
        {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.loadMoreTv.setText("数据加载中...");
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size() + 1;
    }

    public void addItem(LinkedList<String> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addFirst(newDatas.get(i));
        }
        notifyItemRangeInserted(0, newDatas.size());
        notifyItemRangeChanged(0 + newDatas.size(), getItemCount() - newDatas.size());
    }

    public void addMoreItem(LinkedList<String> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        notifyItemRangeInserted(getItemCount() - 1, newDatas.size());
        notifyItemRangeChanged(getItemCount() - 1, getItemCount() - newDatas.size());
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv;

        public ItemViewHolder(View itemView)
        {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.page_tv);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder
    {
        private TextView loadMoreTv;

        public FooterViewHolder(View itemView)
        {
            super(itemView);
            loadMoreTv = (TextView) itemView.findViewById(R.id.load_next_page_text);
        }
    }
}
