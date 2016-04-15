package com.gaoyy.easysoical.bean;

/**
 * Created by gaoyy on 2016/4/14/0014.
 */
public class Favorite
{
    private String fid;
    private String from_aid;
    private String to_aid;
    private String time;
    private String from_name;
    private String to_name;
    private String to_avatar;
    private String to_content;
    private String to_picture;
    private String to_tid;

    public String getTo_content()
    {
        return to_content;
    }

    public String getTo_picture()
    {
        return to_picture;
    }

    public String getTo_tid()
    {
        return to_tid;
    }

    public String getFid()
    {
        return fid;
    }

    public String getFrom_aid()
    {
        return from_aid;
    }

    public String getTo_aid()
    {
        return to_aid;
    }

    public String getTime()
    {
        return time;
    }

    public String getFrom_name()
    {
        return from_name;
    }

    public String getTo_name()
    {
        return to_name;
    }

    public String getTo_avatar()
    {
        return to_avatar;
    }

    @Override
    public String toString()
    {
        return "Favorite{" +
                "fid='" + fid + '\'' +
                ", from_aid='" + from_aid + '\'' +
                ", to_aid='" + to_aid + '\'' +
                ", time='" + time + '\'' +
                ", from_name='" + from_name + '\'' +
                ", to_name='" + to_name + '\'' +
                ", to_avatar='" + to_avatar + '\'' +
                ", to_content='" + to_content + '\'' +
                ", to_picture='" + to_picture + '\'' +
                ", to_tid='" + to_tid + '\'' +
                '}';
    }
}
