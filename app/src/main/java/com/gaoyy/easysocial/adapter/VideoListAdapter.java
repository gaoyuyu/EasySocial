package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.bean.Video;
import com.gaoyy.easysocial.fragment.VideoListFragment;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;

import java.util.LinkedList;

/**
 * Created by gaoyy on 2016/7/29 0029.
 */

public class VideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater inflater;
    private LinkedList<Video> data;
    private Context context;

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    public VideoListAdapter(Context context, LinkedList<Video> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = inflater.inflate(R.layout.item_video_list, parent, false);
        VideoItemViewHolder videoItemViewHolder = new VideoItemViewHolder(rootView);
        return videoItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        VideoItemViewHolder videoItemViewHolder = (VideoItemViewHolder)holder;
        Video video = data.get(position);
        Log.i(Global.TAG,"video==>"+video.toString());

        new SetVideoCoverTask(videoItemViewHolder).execute(video.getVideo_url());
        videoItemViewHolder.itemVideoListTitle.setText(video.getVideo_title());
        videoItemViewHolder.itemVideoListTime.setText(video.getTime());
        if(onItemClickListener != null)
        {
            videoItemViewHolder.itemVideoListCardview.setOnClickListener(new VideoOnClickListener(videoItemViewHolder));
        }
    }

    private class SetVideoCoverTask extends AsyncTask<String, String, Bitmap>
    {
        private VideoItemViewHolder videoItemViewHolder;

        public SetVideoCoverTask(VideoItemViewHolder videoItemViewHolder)
        {
            this.videoItemViewHolder = videoItemViewHolder;
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            Bitmap bg = Tool.createVideoThumbnail(params[0],800,600);
            return bg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);
            videoItemViewHolder.itemVideoListBg.setBackground(new BitmapDrawable(bitmap));
        }
    }


    public class VideoOnClickListener implements View.OnClickListener
    {
        VideoItemViewHolder videoItemViewHolder;

        public VideoOnClickListener(VideoItemViewHolder videoItemViewHolder)
        {
            this.videoItemViewHolder = videoItemViewHolder;
        }

        @Override
        public void onClick(View v)
        {
            int id = v.getId();
            switch (id)
            {
                case R.id.item_video_list_cardview:
                    onItemClickListener.onItemClick(videoItemViewHolder.itemVideoListCardview, videoItemViewHolder.getLayoutPosition());
                    break;
            }
        }
    }

    public void addItem(LinkedList<Video> newDatas)
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

    public void addMoreItem(LinkedList<Video> newDatas)
    {
        for (int i = 0; i < newDatas.size(); i++)
        {
            data.addLast(newDatas.get(i));
        }
        notifyItemRangeInserted(getItemCount(), newDatas.size());
        notifyItemRangeChanged(getItemCount(), getItemCount() - newDatas.size());
    }


    public static class VideoItemViewHolder extends RecyclerView.ViewHolder
    {
        private CardView itemVideoListCardview;
        private RelativeLayout itemVideoListBg;
        private ImageView imageView2;
        private TextView itemVideoListTitle;
        private TextView itemVideoListTime;


        public VideoItemViewHolder(View itemView)
        {
            super(itemView);
            itemVideoListCardview = (CardView) itemView.findViewById(R.id.item_video_list_cardview);
            itemVideoListBg = (RelativeLayout) itemView.findViewById(R.id.item_video_list_bg);
            imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
            itemVideoListTitle = (TextView) itemView.findViewById(R.id.item_video_list_title);
            itemVideoListTime = (TextView) itemView.findViewById(R.id.item_video_list_time);
        }
    }
}
