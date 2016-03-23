package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.gaoyy.easysoical.view.BasicProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private Toolbar loginToolbar;
    private MaterialEditText email;
    private MaterialEditText password;
    private AppCompatButton loginBtn;

    private final OkHttpClient client = new OkHttpClient();
    BasicProgressDialog basicProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        assignViews();
        initToolbar();
        setListener();

    }

    private void assignViews()
    {
        loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        email = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);
        loginBtn = (AppCompatButton) findViewById(R.id.login_btn);

        email.setText("740514999@qq.com");
        password.setText("1qa2ws");

        basicProgressDialog = BasicProgressDialog.create(this);
    }

    public void initToolbar()
    {
        loginToolbar.setTitle(R.string.login);
        setSupportActionBar(loginToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void setListener()
    {
        loginBtn.setOnClickListener(this);
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class LoginTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Tool.startProgressDialog("Login...", basicProgressDialog);
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
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG,body);
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
                Log.i(Global.TAG, "onPostExecute e-->" + e.toString());
            }

        }
    }
}
