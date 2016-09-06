package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.bean.Comment;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.utils.TransitionHelper;
import com.gaoyy.easysocial.view.BasicProgressDialog;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 回复
 */
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
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().setExitTransition(new ChangeBounds());
        }
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
                TransitionHelper.finishAtyAfterTransition(this);
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
                Log.i(Global.TAG, "===========================================");
                Log.i(Global.TAG, "SendTask code-->" + Tool.getRepCode(body));
                Log.i(Global.TAG, "SendTask body-->" + body);
                Log.i(Global.TAG, "===========================================");
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "SendTask doInBackground Exception-->" + e.toString());
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
                Intent intent = new Intent();
                /**
                 * 刷新HomeFragment的数据
                 */
                if( -1 != getIntent().getIntExtra("position",-1))
                {
                    intent.putExtra("position",getIntent().getIntExtra("position",-1));
                }
                else
                {
                    try
                    {
                        String favCount =  (Tool.getDataJsonObj(s)).getString("favCount");
                        String comCount =  (Tool.getDataJsonObj(s)).getString("comCount");
                        String isfavor =  (Tool.getDataJsonObj(s)).getString("isfavor");
                        intent.putExtra("favCount",favCount);
                        intent.putExtra("comCount",comCount);
                        intent.putExtra("isfavor",isfavor);
                    }
                    catch (Exception e)
                    {
                        Log.i(Global.TAG, "catch Exception when get FAV&COM count：" + e.toString());
                    }
                }

                setResult(RESULT_OK,intent);
                TransitionHelper.finishAtyAfterTransition(ReplyActivity.this);

            }
            else
            {
                Tool.showToast(ReplyActivity.this, "发送失败，请重试");
            }
        }
    }
}
