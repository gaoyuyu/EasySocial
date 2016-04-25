package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.bean.Tweet;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.R;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Tweet> data;
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

    public ListAdapter(Context context, LinkedList<Tweet> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_home, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(rootView);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Tweet tweet = data.get(position);
        Log.i(Global.TAG, "-getComment_count-->" + tweet.getComment_count() + "--getFavorite_count->" + tweet.getFavorite_count());
        itemViewHolder.itemHomeCardView.setTag(tweet);
        itemViewHolder.itemHomeAccount.setText(tweet.getUsername());
        TextPaint textPaint = itemViewHolder.itemHomeAccount.getPaint();
        textPaint.setFakeBoldText(true);
        itemViewHolder.itemHomeDetail.setText(tweet.getCreate_time());
        itemViewHolder.itemHomeTweet.setText(tweet.getContent());


        Log.i(Global.TAG,tweet.toString());

        Uri avaUri = Uri.parse(tweet.getAvatar());
        itemViewHolder.itemHomeAvatar.setImageURI(avaUri);

        if (tweet.getPicture() == null || (tweet.getPicture()).equals(""))
        {
            itemViewHolder.itemHomeTweimg.setVisibility(View.GONE);
        }
        else
        {
            itemViewHolder.itemHomeTweimg.setVisibility(View.VISIBLE);
            Uri picUri = Uri.parse(tweet.getPicture());
            itemViewHolder.itemHomeTweimg.setHierarchy(Tool.getCommonGenericDraweeHierarchy(context));
            itemViewHolder.itemHomeTweimg.setController(Tool.getCommonDraweeController(context));
            itemViewHolder.itemHomeTweimg.setImageURI(picUri);
        }

        if(tweet.getIsfavor().equals("1"))
        {
            itemViewHolder.itemHomeFavLabel.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_favorite_press));
        }
        else
        {
            itemViewHolder.itemHomeFavLabel.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_favorite));
        }

        itemViewHolder.itemHomeComCount.setText(tweet.getComment_count());
        itemViewHolder.itemHomeFavCount.setText(tweet.getFavorite_count());


        if (onItemClickListener != null)
        {
            itemViewHolder.itemHomeCardView.setOnClickListener(new BasicOnClickListener(itemViewHolder));
            itemViewHolder.itemHomeTweimg.setOnClickListener(new BasicOnClickListener(itemViewHolder));
            itemViewHolder.itemHomeFavLayout.setOnClickListener(new BasicOnClickListener(itemViewHolder));
            itemViewHolder.itemHomeComLayout.setOnClickListener(new BasicOnClickListener(itemViewHolder));
        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class BasicOnClickListener implements View.OnClickListener
    {
        ItemViewHolder itemViewHolder;

        public BasicOnClickListener(ItemViewHolder itemViewHolder)
        {
            this.itemViewHolder = itemViewHolder;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.item_home_cardview:
                    onItemClickListener.onItemClick(itemViewHolder.itemHomeCardView, itemViewHolder.getLayoutPosition());
                    break;
                case R.id.item_home_tweimg:
                    onItemClickListener.onItemClick(itemViewHolder.itemHomeTweimg, itemViewHolder.getLayoutPosition());
                    break;
                case R.id.item_home_fav_layout:
                    onItemClickListener.onItemClick(itemViewHolder.itemHomeFavLayout, itemViewHolder.getLayoutPosition());
                    break;
                case R.id.item_home_com_layout:
                    onItemClickListener.onItemClick(itemViewHolder.itemHomeComLayout, itemViewHolder.getLayoutPosition());
                    break;
            }
        }
    }


    public void addItem(LinkedList<Tweet> newDatas)
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

    public void addMoreItem(LinkedList<Tweet> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }

    public void updateFromPosition(int position,Tweet tweet)
    {
        data.remove(position);
        data.add(position,tweet);
        notifyItemChanged(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;
        private TextView itemHomeTweet;
        private SimpleDraweeView itemHomeTweimg;
        private CardView itemHomeCardView;
        private LinearLayout itemHomeFavLayout;
        private ImageView itemHomeFavLabel;
        private TextView itemHomeFavCount;
        private LinearLayout itemHomeComLayout;
        private ImageView itemHomeComLabel;
        private TextView itemHomeComCount;
        private LinearLayout itemHomeShareLayout;
        private ImageView itemHomeShareLabel;
        private TextView itemHomeShareCount;



        public ItemViewHolder(View itemView)
        {
            super(itemView);
            itemHomeCardView = (CardView) itemView.findViewById(R.id.item_home_cardview);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemHomeTweet = (TextView) itemView.findViewById(R.id.item_home_tweet);
            itemHomeTweimg = (SimpleDraweeView) itemView.findViewById(R.id.item_home_tweimg);
            itemHomeFavLayout = (LinearLayout) itemView.findViewById(R.id.item_home_fav_layout);
            itemHomeFavLabel = (ImageView) itemView.findViewById(R.id.item_home_fav_label);
            itemHomeFavCount = (TextView) itemView.findViewById(R.id.item_home_fav_count);
            itemHomeComLayout = (LinearLayout) itemView.findViewById(R.id.item_home_com_layout);
            itemHomeComLabel = (ImageView) itemView.findViewById(R.id.item_home_com_label);
            itemHomeComCount = (TextView) itemView.findViewById(R.id.item_home_com_count);
            itemHomeShareLayout = (LinearLayout) itemView.findViewById(R.id.item_home_share_layout);
            itemHomeShareLabel = (ImageView) itemView.findViewById(R.id.item_home_share_label);
            itemHomeShareCount = (TextView) itemView.findViewById(R.id.item_home_share_count);
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
