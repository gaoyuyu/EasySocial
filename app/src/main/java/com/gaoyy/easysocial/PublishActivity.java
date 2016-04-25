package com.gaoyy.easysocial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.gaoyy.easysocial.utils.Tool;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener
{


    private static final int PUBLISH_SET_IMG = 500;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    private Toolbar publishToolbar;
    private EditText publishEdittext;
    private RelativeLayout publishBottombar;
    private ImageView publishSetimg;
    private ImageView publishSend;
    private RelativeLayout publishImglayout;
    private SimpleDraweeView publishImg;
    private ImageView publishClose;

    private BasicProgressDialog basicProgressDialog;
    private SharedPreferences account;

    private void assignViews()
    {
        publishToolbar = (Toolbar) findViewById(R.id.publish_toolbar);
        publishEdittext = (EditText) findViewById(R.id.publish_edittext);
        publishBottombar = (RelativeLayout) findViewById(R.id.publish_bottombar);
        publishSetimg = (ImageView) findViewById(R.id.publish_setimg);
        publishSend = (ImageView) findViewById(R.id.publish_send);
        publishImglayout = (RelativeLayout) findViewById(R.id.publish_imglayout);
        publishImg = (SimpleDraweeView) findViewById(R.id.publish_img);
        publishClose = (ImageView) findViewById(R.id.publish_close);

        basicProgressDialog = BasicProgressDialog.create(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
        assignViews();
        initToolbar();
        setListener();

    }

    private void initToolbar()
    {
        publishToolbar.setTitle(R.string.publish);
        setSupportActionBar(publishToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void setListener()
    {
        publishSetimg.setOnClickListener(this);
        publishSend.setOnClickListener(this);
        publishClose.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.publish_setimg:
                Intent intent = new Intent();
                intent.setClass(this, SetPicActivity.class);
                intent.putExtra("isPublish", true);
                startActivityForResult(intent, PUBLISH_SET_IMG);
                break;
            case R.id.publish_send:
                String content = publishEdittext.getText().toString();
                if (content == null || content.equals(""))
                {
                    Tool.showToast(PublishActivity.this, "内容为空");
                }
                else
                {
                    String imgName = (String) (publishImg.getTag());
                    if (imgName == null) imgName = "";
                    String[] params = {imgName, content};
                    new PublishTask().execute(params);
                }

                break;
            case R.id.publish_close:
                publishImglayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PUBLISH_SET_IMG)
        {
            if (resultCode == RESULT_OK)
            {
                publishImg.setTag(data.getStringExtra("tweimg"));
                publishImg.setHierarchy(Tool.getCommonGenericDraweeHierarchy(this));
                publishImg.setImageURI(Uri.parse("file://" + data.getStringExtra("tweimg")));
                publishImglayout.setVisibility(View.VISIBLE);
            }
        }
    }

    class PublishTask extends AsyncTask<String, String, String>
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

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("aid", account.getString("aid", ""));
            builder.addFormDataPart("content", params[1]);
            if(!(params[0].equals("")))
            {
                builder.addFormDataPart("image", imgFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imgFile));
            }
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/publishTweet")
                    .post(requestBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                body = response.body().string();
                Log.i(Global.TAG, "publish body--->" + body);
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
                    Tool.showToast(PublishActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                }
                else
                {
                    Tool.showToast(PublishActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                }
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "onPostExecute e-->" + e.toString());
            }
        }
    }
}
