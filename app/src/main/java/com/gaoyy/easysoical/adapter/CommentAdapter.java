package com.gaoyy.easysoical.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    public interface OnTouchListener
    {
        void onTouch(View v, MotionEvent event);
    }

    private OnItemClickListener onItemClickListener;
    private OnTouchListener onTouchListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public void setOnTouchListener(OnTouchListener listener)
    {
        this.onTouchListener = listener;
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
            View rootView = inflater.inflate(R.layout.comment_main, parent, false);
            ComHeaderViewHolder comHeaderViewHolder = new ComHeaderViewHolder(rootView);
            return comHeaderViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof ComItemViewHolder)
        {
            ComItemViewHolder comItemViewHolder = (ComItemViewHolder) holder;
            Comment comment = data.get(position - 1);
            Uri avaUri = Uri.parse(comment.getAvatar());
            comItemViewHolder.itemHomeAccount.setText(comment.getUsername());
            comItemViewHolder.itemHomeAccount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            comItemViewHolder.itemHomeDetail.setVisibility(View.GONE);
            comItemViewHolder.itemCommentTime.setText(comment.getTime());
            comItemViewHolder.itemCommentText.setText(comment.getComment());
            comItemViewHolder.itemHomeAvatar.setImageURI(avaUri);

            comItemViewHolder.itemCommentLabel.setText("#"+position+"楼");

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) (comItemViewHolder.itemHomeAvatar.getLayoutParams());
            layoutParams.height = 70;
            layoutParams.width = 64;
            comItemViewHolder.itemHomeAvatar.setLayoutParams(layoutParams);

            if(null == comment.getPusername())
            {
                comItemViewHolder.itemCommentPrefix.setVisibility(View.GONE);
            }
            else
            {
                comItemViewHolder.itemCommentPrefix.setVisibility(View.VISIBLE);
                String pusername = "@"+comment.getPusername();
                String prefix = "回复"+pusername+"：";
                SpannableStringBuilder sp = new SpannableStringBuilder(prefix);
                sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.holo_blue)),2,prefix.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                comItemViewHolder.itemCommentPrefix.setText(sp);
            }

            if(onItemClickListener != null)
            {
                comItemViewHolder.itemCommentLayout.setOnClickListener(new BasicOnClickListener(comItemViewHolder));
            }

            if(onTouchListener != null)
            {
                comItemViewHolder.itemCommentLayout.setOnTouchListener(new View.OnTouchListener()
                {
                    @Override
                    public boolean onTouch(View v, MotionEvent event)
                    {
                        onTouchListener.onTouch(v,event);
                        return false;
                    }
                });
            }


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
            comHeaderViewHolder.itemHomeAvatar.setImageURI(avaUri);

            if(tweet.getPicture() == null || tweet.getPicture().equals(""))
            {
                comHeaderViewHolder.itemHomeTweimg.setVisibility(View.GONE);
            }
            else
            {
                Uri picUri = Uri.parse(tweet.getPicture());
                comHeaderViewHolder.itemHomeTweimg.setHierarchy(hierarchy);
                comHeaderViewHolder.itemHomeTweimg.setController(controller);
                comHeaderViewHolder.itemHomeTweimg.setImageURI(picUri);
            }



            comHeaderViewHolder.botline.setVisibility(View.VISIBLE);

            TextPaint textPaint = comHeaderViewHolder.itemHomeAccount.getPaint();
            textPaint.setFakeBoldText(true);
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
                case R.id.item_comment_layout:
                    onItemClickListener.onItemClick(comItemViewHolder.itemCommentLayout, comItemViewHolder.getLayoutPosition());
                    break;
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
            data.addLast(newDatas.get(i));
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
        private TextView itemCommentTime;
        private TextView itemCommentLabel;
        private TextView itemCommentPrefix;

        private LinearLayout itemCommentLayout;

        public ComItemViewHolder(View itemView)
        {
            super(itemView);
            itemCommentText = (TextView) itemView.findViewById(R.id.item_comment_text);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemCommentTime = (TextView) itemView.findViewById(R.id.item_comment_time);
            itemCommentLabel = (TextView) itemView.findViewById(R.id.item_comment_label);
            itemCommentLayout = (LinearLayout) itemView.findViewById(R.id.item_comment_layout);
            itemCommentPrefix = (TextView) itemView.findViewById(R.id.item_comment_prefix);
        }
    }

    public static class ComHeaderViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemHomeTweet;
        private SimpleDraweeView itemHomeTweimg;
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;

        private View botline;

        public ComHeaderViewHolder(View itemView)
        {
            super(itemView);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemHomeTweet = (TextView) itemView.findViewById(R.id.item_home_tweet);
            itemHomeTweimg = (SimpleDraweeView) itemView.findViewById(R.id.item_home_tweimg);
            botline = itemView.findViewById(R.id.comment_main_botline);
        }
    }
}
