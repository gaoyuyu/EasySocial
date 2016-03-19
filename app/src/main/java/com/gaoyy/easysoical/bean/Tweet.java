package com.gaoyy.easysoical.bean;

/**
 * Created by gaoyy on 2016/3/20/0020.
 */
public class Tweet
{
    //推文信息
    private String tid;
    private String account_id;
    private String content;
    private String picture;
    private String create_time;
    //账户信息
    private String aid;
    private String username;
    private String realname;
    private String email;
    private String signature;
    private String gender;
    private String avatar;

    public String getTid()
    {
        return tid;
    }

    public String getAccount_id()
    {
        return account_id;
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

    @Override
    public String toString()
    {
        return "Tweet{" +
                "tid='" + tid + '\'' +
                ", account_id='" + account_id + '\'' +
                ", content='" + content + '\'' +
                ", picture='" + picture + '\'' +
                ", create_time='" + create_time + '\'' +
                ", aid='" + aid + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
