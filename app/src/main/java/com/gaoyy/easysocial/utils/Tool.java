package com.gaoyy.easysocial.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.gaoyy.easysocial.view.BasicProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

/**
 * Created by gaoyy on 2016/3/2/0002.
 */
public class Tool
{
    /**
     * showSnackbar
     *
     * @param view
     * @param text
     */
    public static void showSnackbar(View view, String text)
    {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * startProgressDialog
     *
     * @param message
     * @param basicProgressDialog
     */
    public static void startProgressDialog(String message, BasicProgressDialog basicProgressDialog)
    {
        basicProgressDialog.setMessage(message);
        basicProgressDialog.show();
    }

    /**
     * stopProgressDialog
     *
     * @param basicProgressDialog
     */
    public static void stopProgressDialog(BasicProgressDialog basicProgressDialog)
    {
        basicProgressDialog.dismiss();
    }


    /**
     * getMainJsonObj
     *
     * @param body
     * @return
     */
    public static JSONObject getMainJsonObj(String body)
    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(body);
        }
        catch (JSONException e)
        {
            Log.i(Global.TAG, "catch Exception when getMainJsonObj：" + e.toString());
        }

        return jsonObject;
    }

    /**
     * getRepCode 获取返回的结果码（0-请求成功，-1-内部错误）
     *
     * @param body
     * @return
     */
    public static int getRepCode(String body)
    {
        JSONObject jsonObject = getMainJsonObj(body);
        int repCode = -2;
        try
        {
            repCode = jsonObject.getInt("code");
        }
        catch (JSONException e)
        {
            Log.i(Global.TAG, "catch Exception when getRepCode：" + e.toString());
        }
        return repCode;
    }

    /**
     * getRepInfo（若返回json中存在info）
     *
     * @param body
     * @return
     */
    public static String getRepInfo(String body)
    {
        JSONObject jsonObject = getMainJsonObj(body);
        String info = "";
        try
        {
            info = jsonObject.getString("info");
        }
        catch (JSONException e)
        {
            Log.i(Global.TAG, "catch Exception when getRepInfo：" + e.toString());
        }
        return info;
    }

    /**
     * getDataJsonObj
     *
     * @param body
     * @return
     */
    public static JSONObject getDataJsonObj(String body)
    {
        JSONObject jsonObject = getMainJsonObj(body);
        JSONObject dataJsonObj = null;
        try
        {
            dataJsonObj = (JSONObject) jsonObject.get("data");
        }
        catch (JSONException e)
        {
            Log.i(Global.TAG, "catch Exception when getDataJsonObj：" + e.toString());
        }
        return dataJsonObj;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * getCommonGenericDraweeHierarchy
     *
     * @param context
     * @return
     */
    public static GenericDraweeHierarchy getCommonGenericDraweeHierarchy(Context context)
    {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        return hierarchy;
    }

    /**
     * getCommonDraweeController
     *
     * @param context
     * @return
     */
    public static DraweeController getCommonDraweeController(Context context)
    {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .build();
        return controller;
    }

    /**
     * getOkHttpClient
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient()
    {
        OkHttpClient client = new OkHttpClient();
        return client;
    }

    /**
     * getVersionName 获取版本名
     * @param context
     * @return
     */
    public static String getVersionName(Context context)
    {
        return getPackageInfo(context).versionName;
    }

    /**
     * getVersionCode 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context)
    {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context)
    {
        PackageInfo pi = null;
        try
        {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 是否登录
     * @param context
     * @return true 已登录 fasle 未登录
     */
    public static boolean isLogin(Context context)
    {
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String loginKey = account.getString("loginKey","");
        if(loginKey.equals("")||(!loginKey.equals("1")))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}