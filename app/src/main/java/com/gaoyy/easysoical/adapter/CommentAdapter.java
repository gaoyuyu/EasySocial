package com.gaoyy.easysoical.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysoical.R;
import com.gaoyy.easysoical.bean.Comment;
import com.gaoyy.easysoical.bean.Tweet;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Comment> data;
    private Context context;
    private Tweet tweet;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;


    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public CommentAdapter(Context context, LinkedList<Comment> data, Tweet tweet)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.tweet = tweet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View rootView = inflater.inflate(R.layout.item_comment, parent, false);
            ComItemViewHolder comItemViewHolder = new ComItemViewHolder(rootView);
            return comItemViewHolder;
        }
        else if (viewType == TYPE_HEADER)
        {
            View rootView = inflater.inflate(R.layout.comment_basic, parent, false);
            ComHeaderViewHolder comHeaderViewHolder = new ComHeaderViewHolder(rootView);
            return comHeaderViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ComItemViewHolder)
        {
            ComItemViewHolder comItemViewHolder = (ComItemViewHolder) holder;
            Comment comment = data.get(position - 1);
            Uri avaUri = Uri.parse(comment.getAvatar());
            comItemViewHolder.itemHomeAccount.setText(comment.getUsername());
            comItemViewHolder.itemHomeAccount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            comItemViewHolder.itemHomeDetail.setText(comment.getTime());
            comItemViewHolder.itemCommentText.setText(comment.getComment());
            comItemViewHolder.itemHomeAvatar.setImageURI(avaUri);
        }
        else if (holder instanceof ComHeaderViewHolder)
        {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)
                    .build();
            ComHeaderViewHolder comHeaderViewHolder = (ComHeaderViewHolder) holder;

            comHeaderViewHolder.itemHomeAccount.setText(tweet.getUsername());
            comHeaderViewHolder.itemHomeDetail.setText(tweet.getCreate_time());
            comHeaderViewHolder.itemHomeTweet.setText(tweet.getContent());

            Uri avaUri = Uri.parse(tweet.getAvatar());
            Uri picUri = Uri.parse(tweet.getPicture());
            comHeaderViewHolder.itemHomeAvatar.setImageURI(avaUri);
            comHeaderViewHolder.itemHomeTweimg.setHierarchy(hierarchy);
            comHeaderViewHolder.itemHomeTweimg.setController(controller);
            comHeaderViewHolder.itemHomeTweimg.setImageURI(picUri);

        }

        if (onItemClickListener != null)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return TYPE_HEADER;
        }
        else
        {
            return TYPE_ITEM;
        }
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

    public static class ComHeaderViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemHomeTweet;
        private SimpleDraweeView itemHomeTweimg;
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;

        public ComHeaderViewHolder(View itemView)
        {
            super(itemView);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemHomeTweet = (TextView) itemView.findViewById(R.id.item_home_tweet);
            itemHomeTweimg = (SimpleDraweeView) itemView.findViewById(R.id.item_home_tweimg);
        }
    }
}
