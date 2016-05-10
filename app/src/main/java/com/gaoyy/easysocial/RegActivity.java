package com.gaoyy.easysocial;

import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegActivity extends BaseActivity implements TextWatcher, View.OnClickListener
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


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_reg);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
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
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(regToolbar, R.string.reg, true, colors);
    }

    @Override
    protected void setListener()
    {
        super.setListener();
        regRepassword.addTextChangedListener(this);
        regBtn.setOnClickListener(this);
        getInputData();
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
            Tool.startProgressDialog(getResources().getString(R.string.loading), basicProgressDialog);
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
