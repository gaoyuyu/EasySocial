package com.gaoyy.easysoical.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.gaoyy.easysoical.bean.Tweet;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Tweet> data;
    private Context context;
//    private static final int TYPE_ITEM = 0;  //普通Item View
//    private static final int TYPE_FOOTER = 1;  //顶部FootView

    public ListAdapter(Context context, LinkedList<Tweet> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

//    @Override
//    public int getItemViewType(int position)
//    {
//        if (position + 1 == getItemCount())
//        {
//            return TYPE_FOOTER;
//        } else
//        {
//            return TYPE_ITEM;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
//        if (viewType == TYPE_FOOTER)
//        {
//            View rootView = inflater.inflate(R.layout.footer, parent, false);
//            FooterViewHolder footerViewHolder = new FooterViewHolder(rootView);
//            return footerViewHolder;
//        } else if (viewType == TYPE_ITEM)
//        {
//            View rootView = inflater.inflate(R.layout.item_home, parent, false);
//            ItemViewHolder itemViewHolder = new ItemViewHolder(rootView);
//            return itemViewHolder;
//        }
//        return null;
        View rootView = inflater.inflate(R.layout.item_home, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(rootView);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
//        if (holder instanceof ItemViewHolder)
//        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Tweet tweet = data.get(position);
            itemViewHolder.itemHomeAccount.setText(tweet.getUsername());
            itemViewHolder.itemHomeDetail.setText(tweet.getCreate_time());
            itemViewHolder.itemHomeTweet.setText(tweet.getContent());

        GenericDraweeHierarchyBuilder builder =new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(new ProgressBarDrawable())
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .build();


        Uri avaUri = Uri.parse(tweet.getAvatar());
        Uri picUri = Uri.parse(tweet.getPicture());
        itemViewHolder.itemHomeAvatar.setImageURI(avaUri);

        itemViewHolder.itemHomeTweimg.setHierarchy(hierarchy);
        itemViewHolder.itemHomeTweimg.setController(controller);
        itemViewHolder.itemHomeTweimg.setImageURI(picUri);
//        } else if (holder instanceof FooterViewHolder)
//        {
//            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
//            footerViewHolder.loadMoreTv.setText("数据加载中...");
//        }
    }

    @Override
    public int getItemCount()
    {
        return data.size();
//        return data.size() + 1;
    }

    public void addItem(LinkedList<Tweet> newDatas)
    {
        if(data.size() != 0)
        {
            data.clear();
        }

        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addFirst(newDatas.get(i));
        }
//        notifyItemRangeInserted(0, newDatas.size());
//        notifyItemRangeChanged(0 + newDatas.size(), getItemCount() - newDatas.size());
        notifyDataSetChanged();
    }

    public void addMoreItem(LinkedList<Tweet> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
//        notifyItemRangeInserted(getItemCount() - 1, newDatas.size());
//        notifyItemRangeChanged(getItemCount() - 1, getItemCount() - newDatas.size());
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;
        private TextView itemHomeTweet;
        private SimpleDraweeView itemHomeTweimg;
        private TextView itemHomeFav;
        private TextView itemHomeCom;
        private TextView itemHomeShare;


        public ItemViewHolder(View itemView)
        {
            super(itemView);
            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemHomeTweet = (TextView) itemView.findViewById(R.id.item_home_tweet);
            itemHomeTweimg = (SimpleDraweeView) itemView.findViewById(R.id.item_home_tweimg);
            itemHomeFav = (TextView) itemView.findViewById(R.id.item_home_fav);
            itemHomeCom = (TextView) itemView.findViewById(R.id.item_home_com);
            itemHomeShare = (TextView) itemView.findViewById(R.id.item_home_share);
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
