package com.gaoyy.easysoical;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gaoyy.easysoical.utils.Global;
import com.gaoyy.easysoical.utils.Tool;
import com.gaoyy.easysoical.view.BasicProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener
{

    private Toolbar regToolbar;
    private MaterialEditText regEmail;
    private MaterialEditText regUsername;
    private MaterialEditText regRealname;
    private MaterialEditText regPassword;
    private MaterialEditText regRepassword;
    private AppCompatButton regBtn;

    private String email;
    private String username;
    private String realname;
    private String password;
    private String repassword;

    private BasicProgressDialog basicProgressDialog;

    private void assignViews()
    {
        regToolbar = (Toolbar) findViewById(R.id.reg_toolbar);
        regEmail = (MaterialEditText) findViewById(R.id.reg_email);
        regUsername = (MaterialEditText) findViewById(R.id.reg_username);
        regRealname = (MaterialEditText) findViewById(R.id.reg_realname);
        regPassword = (MaterialEditText) findViewById(R.id.reg_password);
        regRepassword = (MaterialEditText) findViewById(R.id.reg_repassword);
        regBtn = (AppCompatButton) findViewById(R.id.reg_btn);
        basicProgressDialog = BasicProgressDialog.create(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        assignViews();
        initToolbar();
        setListener();
        getInputData();


    }

    private void initToolbar()
    {
        regToolbar.setTitle(R.string.reg);
        setSupportActionBar(regToolbar);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void setListener()
    {
        regRepassword.addTextChangedListener(this);
        regBtn.setOnClickListener(this);
    }

    private void getInputData()
    {
        email = regEmail.getText().toString();
        username = regUsername.getText().toString();
        password = regPassword.getText().toString();
        repassword = regRepassword.getText().toString();
        realname = regRealname.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String psd = regPassword.getText().toString();
        String repsd = regRepassword.getText().toString();
        if (!psd.equals(repsd))
        {
            regBtn.setPressed(true);
            regBtn.setEnabled(false);
            regBtn.setClickable(false);
            regBtn.setTextColor(getResources().getColor(R.color.gray));
        }
        else
        {
            regBtn.setPressed(false);
            regBtn.setEnabled(true);
            regBtn.setClickable(true);
            regBtn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.reg_btn:
                getInputData();
                String errorMsg = getErrorMsg();
                if (errorMsg.equals(""))
                {
                    String[] params = {email, username, realname, password};
                    new RegTask().execute(params);
                }
                else
                {
                    Tool.showToast(RegActivity.this, errorMsg);
                }
                break;
        }
    }

    private String getErrorMsg()
    {
        String errorMsg = "";
        if (email.equals(""))
        {
            errorMsg = errorMsg + "Email为空";
        }
        if (username.equals(""))
        {
            errorMsg = errorMsg + "，Username为空";
        }
        if (realname.equals(""))
        {
            errorMsg = errorMsg + "，Realname为空";
        }
        if (password.equals(""))
        {
            errorMsg = errorMsg + "，Passowrd为空";
        }
        return errorMsg;
    }


    class RegTask extends AsyncTask<String, String, String>
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
            RequestBody formBody = new FormBody.Builder()
                    .add("email", params[0])
                    .add("username", params[1])
                    .add("realname", params[2])
                    .add("password", params[3])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "User/register")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, "RegActivity body-->" + body);
                Log.i(Global.TAG, "RegActivity code-->" + Tool.getRepCode(body));
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "RegActivity e-->" + e.toString());
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
                Tool.showToast(RegActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                if (0 == Tool.getRepCode(s))
                {
                    finish();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
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
}
