package com.gaoyy.easysocial.bean;

import java.io.Serializable;

/**
 * Created by gaoyy on 2016/3/28/0028.
 */
public class Comment implements Serializable
{
    private String tid;
    private String aid;
    private String pid;
    private String comment;
    private String username;
    private String avatar;
    private String time;
    private String pusername;

    public String getTid()
    {
        return tid;
    }

    public String getAid()
    {
        return aid;
    }

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

    public String getPusername()
    {
        return pusername;
    }

    public void setTid(String tid)
    {
        this.tid = tid;
    }

    public void setAid(String aid)
    {
        this.aid = aid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    @Override
    public String toString()
    {
        return "Comment{" +
                "tid='" + tid + '\'' +
                ", aid='" + aid + '\'' +
                ", pid='" + pid + '\'' +
                ", comment='" + comment + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", time='" + time + '\'' +
                ", pusername='" + pusername + '\'' +
                '}';
    }
}
