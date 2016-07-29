package com.gaoyy.easysocial.bean;

import java.io.Serializable;

/**
 * Created by gaoyy on 2016/7/29 0029.
 */

public class Video implements Serializable
{
    private String video_title;
    private String video_url;
    private String time;

    public String getVideo_title()
    {
        return video_title;
    }

    public String getTime()
    {
        return time;
    }

    public String getVideo_url()
    {
        return video_url;
    }

    @Override
    public String toString()
    {
        return "Video{" +
                "video_title='" + video_title + '\'' +
                ", video_url='" + video_url + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
