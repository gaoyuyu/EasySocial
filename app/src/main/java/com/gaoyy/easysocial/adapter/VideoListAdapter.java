package com.gaoyy.easysocial.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.bean.Video;
import com.gaoyy.easysocial.utils.Global;

import java.util.LinkedList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


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
        VideoItemViewHolder videoItemViewHolder = (VideoItemViewHolder) holder;
        Video video = data.get(position);
        Log.i(Global.TAG, "video==>" + video.toString());

//        new SetVideoCoverTask(videoItemViewHolder).execute(video.getVideo_url());
        videoItemViewHolder.itemVideoListTitle.setText(video.getVideo_title());
        videoItemViewHolder.itemVideoListTime.setText(video.getTime());
        videoItemViewHolder.itemJCVideoView.setUp(video.getVideo_url() , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");
    }

//    private class SetVideoCoverTask extends AsyncTask<String, String, Bitmap>
//    {
//        private VideoItemViewHolder videoItemViewHolder;
//
//        public SetVideoCoverTask(VideoItemViewHolder videoItemViewHolder)
//        {
//            this.videoItemViewHolder = videoItemViewHolder;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params)
//        {
//            Bitmap bg = Tool.createVideoThumbnail(params[0], 800, 600);
//            return bg;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap)
//        {
//            super.onPostExecute(bitmap);
//            videoItemViewHolder.itemVideoListBg.setBackground(new BitmapDrawable(bitmap));
//        }
//    }


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
        private JCVideoPlayerStandard itemJCVideoView;
        private TextView itemVideoListTitle;
        private TextView itemVideoListTime;


        public VideoItemViewHolder(View itemView)
        {
            super(itemView);
            itemVideoListCardview = (CardView) itemView.findViewById(R.id.item_video_list_cardview);
            itemVideoListBg = (RelativeLayout) itemView.findViewById(R.id.item_video_list_bg);
            itemJCVideoView = (JCVideoPlayerStandard) itemView.findViewById(R.id.item_jc_videoview);
            itemVideoListTitle = (TextView) itemView.findViewById(R.id.item_video_list_title);
            itemVideoListTime = (TextView) itemView.findViewById(R.id.item_video_list_time);
        }
    }
}
