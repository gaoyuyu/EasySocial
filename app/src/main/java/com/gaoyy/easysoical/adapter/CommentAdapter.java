package com.gaoyy.easysoical.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.bean.Comment;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Comment> data;
    private Context context;


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public CommentAdapter(Context context, LinkedList<Comment> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_comment, parent, false);
        ComItemViewHolder comItemViewHolder = new ComItemViewHolder(rootView);

        return comItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ComItemViewHolder comItemViewHolder = (ComItemViewHolder) holder;
        Comment comment = data.get(position);
        Uri avaUri = Uri.parse(comment.getAvatar());
        comItemViewHolder.itemHomeAccount.setText(comment.getUsername());
        comItemViewHolder.itemHomeDetail.setText(comment.getTime());
        comItemViewHolder.itemCommentText.setText(comment.getComment());
        comItemViewHolder.itemHomeAvatar.setImageURI(avaUri);
        if (onItemClickListener != null)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class BasicOnClickListener implements View.OnClickListener
    {
        ComItemViewHolder comItemViewHolder;

        public BasicOnClickListener(ComItemViewHolder comItemViewHolder)
        {
            this.comItemViewHolder = comItemViewHolder;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {

            }
        }
    }


    public void addItem(LinkedList<Comment> newDatas)
    {
        if (data.size() != 0)
        {
            data.clear();
        }

        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addFirst(newDatas.get(i));
        }
        notifyDataSetChanged();
    }

    public void addMoreItem(LinkedList<Comment> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }

    public static class ComItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemCommentText;
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;

        public ComItemViewHolder(View itemView)
        {
            super(itemView);
            itemCommentText = (TextView) itemView.findViewById(R.id.item_comment_text);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
        }
    }
}
