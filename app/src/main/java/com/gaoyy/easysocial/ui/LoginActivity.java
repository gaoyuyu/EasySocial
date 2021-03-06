package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener
{
    private Toolbar loginToolbar;
    private MaterialEditText email;
    private MaterialEditText password;
    private AppCompatButton loginBtn;
    private AppCompatButton loginRegbtn;
    private MaterialRippleLayout loginMrlayout;

    private BasicProgressDialog basicProgressDialog;

    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        email = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login_btn);
        loginMrlayout = (MaterialRippleLayout)findViewById(R.id.login_mrlayout);
        loginRegbtn = (AppCompatButton) findViewById(R.id.login_regbtn);
        basicProgressDialog = BasicProgressDialog.create(this);

    }


    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        loginMrlayout.setBackgroundColor(getResources().getColor(colors[0]));
        super.initToolbar(loginToolbar, R.string.login, true, colors);
    }

    @Override
    protected void configViews()
    {
        super.configViews();
        email.setText("740514999@qq.com");
        password.setText("1qa2ws");
    }

    @Override
    protected void setListener()
    {
        super.setListener();
        loginBtn.setOnClickListener(this);
        loginRegbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.login_btn:
                String[] params = {email.getText().toString(), password.getText().toString()};
                new LoginTask().execute(params);
                break;
            case R.id.login_regbtn:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegActivity.class);
                startActivity(intent);
                break;
        }
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

    class LoginTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog(getResources().getString(R.string.logining), basicProgressDialog);
        }

        @Override
        protected String doInBackground(String... params)
        {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", params[0])
                    .add("password", params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "User/login")
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
                Log.e(Global.TAG, "LoginTask doInBackground Exception-->" + e.toString());
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
                    SharedPreferences account = (LoginActivity.this).getSharedPreferences("account",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = account.edit();
                    JSONObject dataJsonObj = Tool.getDataJsonObj(s);
                    editor.putString("loginKey", "1");
                    editor.putString("aid", dataJsonObj.getString("aid"));
                    editor.putString("username", dataJsonObj.getString("username"));
                    editor.putString("realname", dataJsonObj.getString("realname"));
                    editor.putString("email", dataJsonObj.getString("email"));
                    editor.putString("password", dataJsonObj.getString("password"));
                    editor.putString("signature", dataJsonObj.getString("signature"));
                    editor.putString("gender", dataJsonObj.getString("gender"));
                    editor.putString("avatar", dataJsonObj.getString("avatar"));
                    editor.putString("personal", "");
                    editor.commit();
                    Tool.showToast(LoginActivity.this, "登录成功");
                    finish();
                }
                else
                {
                    Tool.showToast(LoginActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                }
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "LoginTask onPostExecute Exception-->" + e.toString());
            }

        }
    }
}
