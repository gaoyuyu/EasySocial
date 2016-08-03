package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gaoyy.easysocial.bean.Rank;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Rank> data;
    private Context context;


    public RankAdapter(Context context, LinkedList<Rank> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_rank, parent, false);
        RankItemViewHolder rankItemViewHolder = new RankItemViewHolder(rootView);
        return rankItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        RankItemViewHolder rankItemViewHolder = (RankItemViewHolder) holder;
        Rank rank = data.get(position);
        rankItemViewHolder.itemRankFav.setText(rank.getCount());
        rankItemViewHolder.itemHomeAvatar.setImageURI(Uri.parse(rank.getAvatar()));
        rankItemViewHolder.itemHomeAccount.setText(rank.getUsername());
        rankItemViewHolder.itemHomeDetail.setText(rank.getCreate_time());
        rankItemViewHolder.itemHomeTweet.setText(rank.getContent());
        rankItemViewHolder.itemRankFav.setText(rank.getCount());

        if(rank.getPicture() == null || rank.getPicture().equals(""))
        {
            rankItemViewHolder.itemHomeTweimg.setVisibility(View.GONE);
        }
        else
        {
            rankItemViewHolder.itemHomeTweimg.setVisibility(View.VISIBLE);
            Float width = Float.valueOf(rank.getPic_width());
            Float height = Float.valueOf(rank.getPic_height());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Tool.px2dip(context,width),Tool.px2dip(context,height));
            rankItemViewHolder.itemHomeTweimg.setLayoutParams(lp);
            rankItemViewHolder.itemHomeTweimg.setHierarchy(Tool.getCommonGenericDraweeHierarchy(context));
            rankItemViewHolder.itemHomeTweimg.setController(Tool.getCommonDraweeController(Uri.parse(rank.getPicture()),rankItemViewHolder.itemHomeTweimg));
        }

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
    public void addItem(LinkedList<Rank> newDatas)
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

    public static class RankItemViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemHomeTweet;
        private SimpleDraweeView itemHomeTweimg;
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;
        private CardView itemHomeCardview;
        private TextView itemRankFav;


        public RankItemViewHolder(View itemView)
        {
            super(itemView);
            itemHomeCardview = (CardView) itemView.findViewById(R.id.item_home_cardview);
            itemRankFav = (TextView)  itemView.findViewById(R.id.item_rank_fav);
            itemHomeAvatar = (SimpleDraweeView)  itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView)  itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView)  itemView.findViewById(R.id.item_home_detail);
            itemHomeTweet = (TextView)  itemView.findViewById(R.id.item_home_tweet);
            itemHomeTweimg = (SimpleDraweeView)  itemView.findViewById(R.id.item_home_tweimg);

        }
    }
}
