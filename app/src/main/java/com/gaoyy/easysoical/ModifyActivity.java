package com.gaoyy.easysoical;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener
{
    private Toolbar modifyToolbar;
    private RelativeLayout modifyAvatarLayout;
    private SimpleDraweeView modifyAvatar;
    private RelativeLayout modifyUsernameLayout;
    private TextView modifyUsername;
    private RelativeLayout modifyGenderLayout;
    private TextView modifyGender;
    private RelativeLayout modifySignatureLayout;
    private TextView modifySignature;

    private SharedPreferences account;

    private void assignViews()
    {
        modifyToolbar = (Toolbar) findViewById(R.id.modify_toolbar);
        modifyAvatarLayout = (RelativeLayout) findViewById(R.id.modify_avatar_layout);
        modifyAvatar = (SimpleDraweeView) findViewById(R.id.modify_avatar);
        modifyUsernameLayout = (RelativeLayout) findViewById(R.id.modify_username_layout);
        modifyUsername = (TextView) findViewById(R.id.modify_username);
        modifyGenderLayout = (RelativeLayout) findViewById(R.id.modify_gender_layout);
        modifyGender = (TextView) findViewById(R.id.modify_gender);
        modifySignatureLayout = (RelativeLayout) findViewById(R.id.modify_signature_layout);
        modifySignature = (TextView) findViewById(R.id.modify_signature);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        account = getSharedPreferences("account",Activity.MODE_PRIVATE);
        assignViews();
        initToolbar();
        configViews();
        setListener();
    }

    private void initToolbar()
    {
        modifyToolbar.setTitle("编辑信息");
        setSupportActionBar(modifyToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
    }

    private void setListener()
    {
        modifyAvatarLayout.setOnClickListener(this);
        modifyUsernameLayout.setOnClickListener(this);
        modifyGenderLayout.setOnClickListener(this);
        modifySignatureLayout.setOnClickListener(this);
    }

    private void configViews()
    {
        modifyAvatar.setImageURI(Uri.parse(account.getString("avatar","")));
        modifyUsername.setText(account.getString("username",""));
        if(account.getString("gender","").equals("1"))
        {
            modifyGender.setText("男");
        }
        else
        {
            modifyGender.setText("女");
        }
        modifySignature.setText(account.getString("signature",""));

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
            case R.id.modify_avatar_layout:

                break;
            case R.id.modify_username_layout:

                break;
            case R.id.modify_gender_layout:

                break;
            case R.id.modify_signature_layout:

                break;
        }
    }
}
