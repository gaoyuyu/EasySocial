package com.gaoyy.easysoical.bean;

import java.io.Serializable;

/**
 * Created by gaoyy on 2016/3/28/0028.
 */
public class Comment implements Serializable
{
    private String pid;
    private String comment;
    private String username;
    private String avatar;
    private String time;

    public String getComment()
    {
        return comment;
    }

    public String getPid()
    {
        return pid;
    }

    public String getUsername()
    {
        return username;
    }

    public String getTime()
    {
        return time;
    }

    public String getAvatar()
    {
        return avatar;
    }

    @Override
    public String toString()
    {
        return "Comment{" +
                "pid='" + pid + '\'' +
                ", comment='" + comment + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
