package com.gaoyy.easysoical.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gaoyy.easysoical.view.BasicProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

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
}
