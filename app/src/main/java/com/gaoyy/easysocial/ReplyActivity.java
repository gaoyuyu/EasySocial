package com.gaoyy.easysocial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.bean.Comment;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReplyActivity extends BaseActivity
{
    private Toolbar replyToolbar;
    private EditText replyEdit;
    private Comment comment;
    private BasicProgressDialog basicProgressDialog;

    private boolean isChild;


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_reply);
        comment = (Comment) getIntent().getSerializableExtra("comment");
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        replyToolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        replyEdit = (EditText) findViewById(R.id.reply_edit);
        basicProgressDialog = BasicProgressDialog.create(this);
    }

    private void setHintText()
    {
        isChild = getIntent().getBooleanExtra("isChild", false);
        if (isChild)
        {
            replyEdit.setHint("回复@" + comment.getUsername() + "：");
        }
    }

    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(replyToolbar, R.string.reply, true, colors);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        setHintText();
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
            Tool.startProgressDialog(getResources().getString(R.string.sending), basicProgressDialog);
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

                if( -1 != getIntent().getIntExtra("position",-1))
                {
                    Intent intent = new Intent();
                    intent.putExtra("position",getIntent().getIntExtra("position",-1));
                    setResult(RESULT_OK,intent);
                }
                finish();

            }
            else
            {
                Tool.showToast(ReplyActivity.this, "发送失败，请重试");
            }
        }
    }
}
