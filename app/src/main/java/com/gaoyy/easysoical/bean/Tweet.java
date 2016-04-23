package com.gaoyy.easysoical.bean;

import java.io.Serializable;

/**
 * Created by gaoyy on 2016/3/20/0020.
 */
public class Tweet implements Serializable
{
    //推文信息
    private String tid;
    private String content;
    private String picture;
    private String create_time;
    private String favorite_count;
    private String comment_count;
    //账户信息
    private String aid;
    private String username;
    private String realname;
    private String email;
    private String signature;
    private String gender;
    private String avatar;

    private String isfavor;

    @Override
    public String toString()
    {
        return "Tweet{" +
                "tid='" + tid + '\'' +
                ", content='" + content + '\'' +
                ", picture='" + picture + '\'' +
                ", create_time='" + create_time + '\'' +
                ", favorite_count='" + favorite_count + '\'' +
                ", comment_count='" + comment_count + '\'' +
                ", aid='" + aid + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isfavor='" + isfavor + '\'' +
                '}';
    }

    public String getIsfavor()
    {
        return isfavor;
    }

    public String getTid()
    {
        return tid;
    }

    public String getContent()
    {
        return content;
    }

    public String getPicture()
    {
        return picture;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public String getAid()
    {
        return aid;
    }

    public String getUsername()
    {
        return username;
    }

    public String getRealname()
    {
        return realname;
    }

    public String getEmail()
    {
        return email;
    }

    public String getSignature()
    {
        return signature;
    }

    public String getGender()
    {
        return gender;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public String getFavorite_count()
    {
        return favorite_count;
    }

    public String getComment_count()
    {
        return comment_count;
    }

    public void setIsfavor(String isfavor)
    {
        this.isfavor = isfavor;
    }

    public void setFavorite_count(String favorite_count)
    {
        this.favorite_count = favorite_count;
    }
}
