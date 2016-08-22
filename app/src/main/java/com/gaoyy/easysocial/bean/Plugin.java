package com.gaoyy.easysocial.bean;

/**
 * Created by gaoyy on 2016/8/22 0022.
 */
public class Plugin
{
    private String location;
    private String remote;
    private String size;

    public String getLocation()
    {
        return location;
    }

    public String getRemote()
    {
        return remote;
    }

    public String getSize()
    {
        return size;
    }

    @Override
    public String toString()
    {
        return "Plugin{" +
                "location='" + location + '\'' +
                ", remote='" + remote + '\'' +
                ", size=" + size +
                '}';
    }
}
