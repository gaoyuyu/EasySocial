package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.bean.Favorite;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.utils.Tool;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/2/16/0016.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Favorite> data;
    private Context context;


    public FavoriteAdapter(Context context, LinkedList<Favorite> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_favorite, parent, false);
        FavorItemViewHolder favorItemViewHolder = new FavorItemViewHolder(rootView);
        return favorItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        FavorItemViewHolder favorItemViewHolder = (FavorItemViewHolder) holder;
        Favorite favorite = data.get(position);
        favorItemViewHolder.itemHomeDetail.setVisibility(View.GONE);
        favorItemViewHolder.itemHomeAccount.setText(favorite.getFrom_name());
        favorItemViewHolder.itemHomeAvatar.setImageURI(Uri.parse(favorite.getTo_avatar()));
        favorItemViewHolder.itemFavoriteTime.setText(favorite.getTime());
        favorItemViewHolder.itemFavoriteText.setText("赞了我");

        String toPrefix = "@"+favorite.getTo_name()+"：";
        String toContent = toPrefix+favorite.getTo_content();
        SpannableStringBuilder sp = new SpannableStringBuilder(toContent);
        sp.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorAccent)),0,toContent.length()-favorite.getTo_content().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        favorItemViewHolder.itemFavoriteToContent.setText(sp);

        if(favorite.getTo_picture() == null||favorite.getTo_picture().equals(""))
        {
            favorItemViewHolder.itemFavoriteToPic.setVisibility(View.GONE);
        }
        else
        {
            favorItemViewHolder.itemFavoriteToPic.setHierarchy(Tool.getCommonGenericDraweeHierarchy(context));
            favorItemViewHolder.itemFavoriteToPic.setController(Tool.getCommonDraweeController(Uri.parse(favorite.getTo_picture()),favorItemViewHolder.itemFavoriteToPic));
        }


    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public void addItem(LinkedList<Favorite> newDatas)
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

    public void addMoreItem(LinkedList<Favorite> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }

    public static class FavorItemViewHolder extends RecyclerView.ViewHolder
    {
        private LinearLayout itemFavoriteLayout;
        private TextView itemFavoriteText;
        private TextView itemFavoriteTime;

        private SimpleDraweeView itemHomeAvatar;
        private TextView itemHomeAccount;
        private TextView itemHomeDetail;

        private TextView itemFavoriteTo;

        private TextView itemFavoriteToContent;
        private SimpleDraweeView itemFavoriteToPic;


        public FavorItemViewHolder(View itemView)
        {
            super(itemView);

            itemFavoriteLayout = (LinearLayout) itemView.findViewById(R.id.item_favorite_layout);
            itemFavoriteText = (TextView) itemView.findViewById(R.id.item_favorite_text);
            itemFavoriteTime = (TextView) itemView.findViewById(R.id.item_favorite_time);

            itemHomeAvatar = (SimpleDraweeView) itemView.findViewById(R.id.item_home_avatar);
            itemHomeAccount = (TextView) itemView.findViewById(R.id.item_home_account);
            itemHomeDetail = (TextView) itemView.findViewById(R.id.item_home_detail);
            itemFavoriteToContent = (TextView) itemView.findViewById(R.id.item_favorite_to_content);
            itemFavoriteToPic = (SimpleDraweeView) itemView.findViewById(R.id.item_favorite_to_pic);
        }
    }
}
