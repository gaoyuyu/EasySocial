package com.gaoyy.easysocial.bean;

/**
 * Created by gaoyy on 2016/4/23/0023.
 */
public class Rank
{
    private String tid;
    private String aid;
    private String picture;
    private String content;
    private String create_time;
    private String pic_width;
    private String pic_height;
    private String username;
    private String realname;
    private String email;
    private String password;
    private String signature;
    private String gender;
    private String avatar;
    private String count;

    public String getTid()
    {
        return tid;
    }

    public String getAid()
    {
        return aid;
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

    public String getPassword()
    {
        return password;
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

    public String getCount()
    {
        return count;
    }

    public String getPic_width()
    {
        return pic_width;
    }

    public String getPic_height()
    {
        return pic_height;
    }

    @Override
    public String toString()
    {
        return "Rank{" +
                "tid='" + tid + '\'' +
                ", aid='" + aid + '\'' +
                ", picture='" + picture + '\'' +
                ", content='" + content + '\'' +
                ", create_time='" + create_time + '\'' +
                ", pic_width='" + pic_width + '\'' +
                ", pic_height='" + pic_height + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
