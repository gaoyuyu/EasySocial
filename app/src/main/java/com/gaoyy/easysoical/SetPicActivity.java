package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.gaoyy.easysoical.view.BasicProgressDialog;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetPicActivity extends Activity implements View.OnClickListener
{
    private TextView setPicAblum;
    private TextView setPicCamera;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static String picFileFullName;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    private SharedPreferences account;

    private BasicProgressDialog basicProgressDialog;

    private static final int PUBLISH_SET_IMG_REQUEST_CODE = 500;

    private void assignViews()
    {
        setPicAblum = (TextView) findViewById(R.id.set_pic_ablum);
        setPicCamera = (TextView) findViewById(R.id.set_pic_camera);

        basicProgressDialog = BasicProgressDialog.create(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pic);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
        assignViews();
        setListener();
    }

    private void setListener()
    {
        setPicAblum.setOnClickListener(this);
        setPicCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.set_pic_ablum:
                openAlbum();
                break;
            case R.id.set_pic_camera:
                takePicture();
                break;
        }
    }

    public void takePicture()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists())
            {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else
        {
            Log.e(Global.TAG, "请确认已经插入SD卡");
        }
    }

    public void openAlbum()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Log.e(Global.TAG, "获取拍照图片成功，path=" + picFileFullName);
                Log.e(Global.TAG, "获取拍照图片成功，path=" + new File(picFileFullName).getName());
                handleImg(picFileFullName);
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // 用户取消了图像捕获
            }
            else
            {
                // 图像捕获失败，提示用户
                Log.e(Global.TAG, "拍照失败");
            }
        }
        else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Uri uri = data.getData();
                if (uri != null)
                {
                    String realPath = getRealPathFromURI(uri);
                    Log.e(Global.TAG, "获取相册图片成功，path=" + realPath);
                    Log.e(Global.TAG, "获取相册图片成功，path=" + new File(realPath).getName());
                    handleImg(realPath);

                }
                else
                {
                    Log.e(Global.TAG, "从相册获取图片失败");
                }
            }
        }
    }

    /**
     * 处理图片
     * @param path 图片路径
     */
    private void handleImg(String path)
    {
        if (getIntent().getBooleanExtra("isPublish", false))
        {
            Intent intent = new Intent();
            intent.putExtra("tweimg", path);
            setResult(RESULT_OK, intent);
            finish();
        }
        else
        {
            new UpoadTask().execute(path);
        }
    }

    /**
     * This method is used to get real path of file from from uri<br/>
     * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-captured-from-camera
     *
     * @param contentUri
     * @return String
     */
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }

    class UpoadTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog("loading...", basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            File imgFile = new File(params[0]);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imgFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imgFile))
                    .build();

            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/upload")
                    .post(requestBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                body = response.body().string();
                Log.i(Global.TAG,"Setpic-->"+body);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return body;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            try
            {
                if (0 == Tool.getRepCode(s))
                {
                    Tool.showToast(SetPicActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                    Log.i(Global.TAG, (Tool.getMainJsonObj(s)).getString("data"));
                    Intent intent = new Intent();
                    intent.putExtra("avatar",(Tool.getMainJsonObj(s)).getString("data"));
                    setResult(RESULT_OK,intent);
//                    SharedPreferences.Editor editor = account.edit();
//                    editor.putString("avatar", (Tool.getMainJsonObj(s)).getString("data"));
//                    editor.commit();
                    finish();
                }
                else
                {
                    Tool.showToast(SetPicActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "onPostExecute e-->" + e.toString());
            }
        }
    }


    class UpdateAvatarTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }


}
