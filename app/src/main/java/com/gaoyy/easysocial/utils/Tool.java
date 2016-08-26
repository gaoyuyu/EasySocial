package com.gaoyy.easysocial.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.morgoo.droidplugin.pm.PluginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
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
     * @param picUri
     * @param img
     * @return
     */
    public static DraweeController getCommonDraweeController(Uri picUri, SimpleDraweeView img)
    {
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                .setUseLastFrameForPreview(true)
                .build();
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(picUri)
                .setImageDecodeOptions(decodeOptions)
                .setAutoRotateEnabled(true)
                .setLocalThumbnailPreviewsEnabled(true)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .setProgressiveRenderingEnabled(false)
                .setResizeOptions(new ResizeOptions(800, 600))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(img.getController())
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
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context)
    {
        return getPackageInfo(context).versionName;
    }

    /**
     * getVersionCode 获取版本号
     *
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
     *
     * @param context
     * @return true 已登录 fasle 未登录
     */
    public static boolean isLogin(Context context)
    {
        SharedPreferences account = context.getSharedPreferences("account", Activity.MODE_PRIVATE);
        String loginKey = account.getString("loginKey", "");
        if (loginKey.equals("") || (!loginKey.equals("1")))
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 带文本和图片
     *
     * @param context
     * @param imgUrl
     * @param text
     */
    public static void showShare(Context context, String imgUrl, String text)
    {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(context.getString(R.string.app_name));
        oks.setTitleUrl(context.getString(R.string.github));
        oks.setText(text);
        oks.setUrl(context.getString(R.string.github));
        oks.setImageUrl(imgUrl);
        oks.setSite(context.getString(R.string.app_name));
        oks.setSiteUrl(context.getString(R.string.github));
        oks.show(context);

//        ShareSDK.initSDK(context);
//        OnekeyShare oks = new OnekeyShare();
//        // 关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.setTitle(context.getString(R.string.app_name));
//        oks.setTitleUrl(context.getString(R.string.github));
//        oks.setComment(context.getString(R.string.submit_question));
//        oks.setSite(context.getString(R.string.app_name));
//        oks.setText(context.getString(R.string.submit_question));
//        oks.setImageUrl(imgUrl);
//        oks.show(context);

    }

    /**
     * 无文本无图片
     *
     * @param context
     */
    public static void showShare(Context context)
    {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(context.getString(R.string.app_name));
        oks.setTitleUrl(context.getString(R.string.github));
        oks.setUrl(context.getString(R.string.github));
        oks.setSite(context.getString(R.string.app_name));
        oks.setSiteUrl(context.getString(R.string.github));
        oks.show(context);
    }

    public static int[] getThemeColors(Context context)
    {
        int[] colors = new int[2];
        SharedPreferences sbc = context.getSharedPreferences("sbc", Activity.MODE_PRIVATE);
        int key = sbc.getInt("color", -1);
        switch (key)
        {
            case 0:
                colors[0] = R.color.indigo_colorPrimary;
                colors[1] = R.color.indigo_colorPrimaryDark;
                break;
            case 1:
                colors[0] = R.color.light_green_colorPrimary;
                colors[1] = R.color.light_green_colorPrimaryDark;
                break;
            case 2:
                colors[0] = R.color.blue_colorPrimary;
                colors[1] = R.color.blue_colorPrimaryDark;
                break;
            case 3:
                colors[0] = R.color.pink_colorPrimary;
                colors[1] = R.color.pink_colorPrimaryDark;
                break;
            case 4:
                colors[0] = R.color.deep_orange_colorPrimary;
                colors[1] = R.color.deep_orange_colorPrimaryDark;
                break;
            case 5:
                colors[0] = R.color.green_colorPrimary;
                colors[1] = R.color.green_colorPrimaryDark;
                break;
            case 6:
                colors[0] = R.color.purple_colorPrimary;
                colors[1] = R.color.purple_colorPrimaryDark;
                break;
            case 7:
                colors[0] = R.color.orange_colorPrimary;
                colors[1] = R.color.orange_colorPrimaryDark;
                break;
            case 8:
                colors[0] = R.color.deep_purple_colorPrimary;
                colors[1] = R.color.deep_purple_colorPrimaryDark;
                break;
            default:
                colors[0] = R.color.colorPrimary;
                colors[1] = R.color.colorPrimaryDark;
                break;
        }
        return colors;
    }

    /**
     * 获取网络视频缩略图
     *
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createVideoThumbnail(String url, int width, int height)
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try
        {
            if (Build.VERSION.SDK_INT >= 14)
            {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else
            {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        }
        catch (IllegalArgumentException ex)
        {
            // Assume this is a corrupt video file
        }
        catch (RuntimeException ex)
        {
            // Assume this is a corrupt video file.
        }
        finally
        {
            try
            {
                retriever.release();
            }
            catch (RuntimeException ex)
            {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null)
        {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    /**
     * 根据插件的http地址获取文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 获取插件文件夹路径
     *
     * @return
     */
    public static String getPluginFileDir()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "pluginIn";
    }


    /**
     * 检查指定包名的插件是否已安装
     *
     * @param fileName
     */
    public static boolean checkTargetPackageisInstalled(String fileName)
    {
        PackageInfo p = null;
        String packageName = returnPackageName(fileName);
        try
        {
            p = PluginManager.getInstance().getPackageInfo(packageName, 0);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return (p != null);
    }

    /**
     * 返回指定包名
     *
     * @param fileName
     * @return
     */
    public static String returnPackageName(String fileName)
    {
        String res = null;
        if (fileName.equals("NewsReader.apk"))
        {
            res = Global.NEWSREADER_PACKAGENAME;
        }
        return res;
    }

    /**
     * 返回插件的Resourse
     *
     * @param context
     * @param apkPath
     * @return
     * @throws Exception
     */
    public static Resources getPluginResources(Context context, String apkPath)
    {
        Resources pluginRes = null;
        try
        {
            String PATH_AssetManager = "android.content.res.AssetManager";
            Class assetMagCls = null;

            assetMagCls = Class.forName(PATH_AssetManager);

            Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
            Object assetMag = assetMagCt.newInstance((Object[]) null);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath",
                    typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
            Resources res = context.getResources();
            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();
            Constructor resCt = Resources.class.getConstructor(typeArgs);
            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();
            res = (Resources) resCt.newInstance(valueArgs);
            pluginRes = res;
        }
        catch (Exception e)
        {
           Log.i(Global.TAG,"catch Exception when getPluginResources : "+e.toString());
        }
        return pluginRes;
    }

}
