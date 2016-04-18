package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gaoyy.easysoical.bean.Comment;
import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.gaoyy.easysoical.view.BasicProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReplyActivity extends AppCompatActivity
{
    private Toolbar replyToolbar;
    private EditText replyEdit;
    private Comment comment;
    private BasicProgressDialog basicProgressDialog;


    private boolean isChild;

    private void assignViews()
    {
        replyToolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        replyEdit = (EditText) findViewById(R.id.reply_edit);
        basicProgressDialog = BasicProgressDialog.create(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        comment = (Comment) getIntent().getSerializableExtra("comment");
        assignViews();
        initToolbar();
        setHintText();
    }

    private void setHintText()
    {
        isChild = getIntent().getBooleanExtra("isChild", false);
        if (isChild)
        {
            replyEdit.setHint("回复@" + comment.getUsername() + "：");
        }
    }

    private void initToolbar()
    {
        replyToolbar.setTitle(getResources().getString(R.string.reply));
        setSupportActionBar(replyToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.reply_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.menu_reply_send:
                String content = replyEdit.getText().toString();
                if (content.equals("") || null == content)
                {
                    Tool.showToast(ReplyActivity.this, "内容为空");
                }
                else
                {
                    SharedPreferences account = (ReplyActivity.this).getSharedPreferences("account", Activity.MODE_PRIVATE);
                    String replyTo = isChild ? comment.getAid() : "0";
                    String[] params = {content, comment.getTid(), replyTo, account.getString("aid", "")};
                    new SendTask().execute(params);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class SendTask extends AsyncTask<String, String, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog("Sending...", basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("replyContent", params[0])
                    .add("replyTweet", params[1])
                    .add("replyTo", params[2])
                    .add("replyFrom", params[3])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/reply")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, body);
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "doInBackground e-->" + e.toString());
            }
            return body;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Tool.stopProgressDialog(basicProgressDialog);
            if (0 == Tool.getRepCode(s))
            {
                Tool.showToast(ReplyActivity.this, "发送成功");
                finish();
            }
            else
            {
                Tool.showToast(ReplyActivity.this, "发送失败，请重试");
            }
        }
    }
}
