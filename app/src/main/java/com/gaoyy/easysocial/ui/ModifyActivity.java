package com.gaoyy.easysocial.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;
import com.gaoyy.easysocial.view.BasicProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 修改个人信息
 */
public class ModifyActivity extends BaseActivity implements View.OnClickListener
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

    private BasicProgressDialog basicProgressDialog;

    private static final int MODIFY_AVATAR_REQUEST_CODE = 400;


    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_modify);
        account = getSharedPreferences("account", Activity.MODE_PRIVATE);
    }

    @Override
    protected void assignViews()
    {
        super.assignViews();
        modifyToolbar = (Toolbar) findViewById(R.id.modify_toolbar);
        modifyAvatarLayout = (RelativeLayout) findViewById(R.id.modify_avatar_layout);
        modifyAvatar = (SimpleDraweeView) findViewById(R.id.modify_avatar);
        modifyUsernameLayout = (RelativeLayout) findViewById(R.id.modify_username_layout);
        modifyUsername = (TextView) findViewById(R.id.modify_username);
        modifyGenderLayout = (RelativeLayout) findViewById(R.id.modify_gender_layout);
        modifyGender = (TextView) findViewById(R.id.modify_gender);
        modifySignatureLayout = (RelativeLayout) findViewById(R.id.modify_signature_layout);
        modifySignature = (TextView) findViewById(R.id.modify_signature);

        basicProgressDialog = BasicProgressDialog.create(this);
    }


    @Override
    protected void initToolbar()
    {
        int[] colors = Tool.getThemeColors(this);
        super.initToolbar(modifyToolbar, R.string.modify, true, colors);
    }

    @Override
    protected void setListener()
    {
        super.setListener();
        modifyAvatarLayout.setOnClickListener(this);
        modifyUsernameLayout.setOnClickListener(this);
        modifyGenderLayout.setOnClickListener(this);
        modifySignatureLayout.setOnClickListener(this);
    }


    @Override
    protected void configViewsOnResume()
    {
        super.configViewsOnResume();
        updateData();

    }

    private void updateData()
    {
        modifyAvatar.setImageURI(Uri.parse(account.getString("avatar", "")));
        modifyUsername.setText(account.getString("username", ""));
        if (account.getString("gender", "").equals("1"))
        {
            modifyGender.setText("男");
        }
        else
        {
            modifyGender.setText("女");
        }
        modifySignature.setText(account.getString("signature", ""));
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
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null);
        int id = v.getId();
        Intent intent = new Intent();
        switch (id)
        {
            case R.id.modify_avatar_layout:
                intent.setClass(ModifyActivity.this, SetPicActivity.class);
                startActivityForResult(intent, MODIFY_AVATAR_REQUEST_CODE);
                break;
            case R.id.modify_username_layout:
                showMaterialDialog(contentView, getResources().getString(R.string.nickname), account.getString("username", ""), "username");
                break;
            case R.id.modify_gender_layout:
                contentView = LayoutInflater.from(this).inflate(R.layout.dialog_gender, null);
                showMaterialDialog(contentView, getResources().getString(R.string.gender), account.getString("gender", ""), "gender");
                break;
            case R.id.modify_signature_layout:
                showMaterialDialog(contentView, getResources().getString(R.string.signature), account.getString("signature", ""), "signature");
                break;
        }
    }


    private void showMaterialDialog(View contentView, String title, String old, String key)
    {
        final MaterialDialog materialDialog = new MaterialDialog(this)
                .setTitle(title)
                .setCanceledOnTouchOutside(true)
                .setContentView(contentView);

        if (title.equals(getResources().getString(R.string.gender)))
        {
            final String[] value = new String[1];
            RadioGroup radioGroup = (RadioGroup) contentView.findViewById(R.id.dialog_radipgroup);
            RadioButton male = (RadioButton) contentView.findViewById(R.id.dialog_male);
            RadioButton female = (RadioButton) contentView.findViewById(R.id.dialog_female);
            female.setTag("2");
            if (old.equals("1"))
            {
                male.setChecked(true);
            }
            else if (old.equals("2"))
            {
                female.setChecked(true);
            }
            materialDialog.setPositiveButton("确定", new PositiveOnClickListener(materialDialog, key, radioGroup, null));
        }
        else
        {
            MaterialEditText materialEditText = (MaterialEditText) contentView.findViewById(R.id.dialog_edittext);
            materialEditText.setText(old);
            materialDialog.setPositiveButton("确定", new PositiveOnClickListener(materialDialog, key, null, materialEditText));
        }


        materialDialog.setNegativeButton("取消", new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                materialDialog.dismiss();
            }
        });

        materialDialog.show();
    }

    class PositiveOnClickListener implements View.OnClickListener
    {
        private MaterialDialog materialDialog;
        private String key;
        private RadioGroup radioGroup;
        private MaterialEditText materialEditText;

        public PositiveOnClickListener(MaterialDialog materialDialog, String key, RadioGroup radioGroup, MaterialEditText materialEditText)
        {
            this.materialDialog = materialDialog;
            this.key = key;
            this.radioGroup = radioGroup;
            this.materialEditText = materialEditText;
        }

        @Override
        public void onClick(View v)
        {

            if (materialEditText == null)
            {
                String value = null;
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.dialog_male)
                {
                    value = "1";
                }
                else
                {
                    value = "2";
                }
                String[] params = {key, value};
                new UpdatePersonInfoTask(materialDialog).execute(params);
            }
            else if (radioGroup == null)
            {
                String[] params = {key, materialEditText.getText().toString()};
                new UpdatePersonInfoTask(materialDialog).execute(params);
            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_AVATAR_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                String avatar = data.getStringExtra("avatar");
                String[] params = {"avatar", avatar};
                new UpdatePersonInfoTask(null).execute(params);
            }
        }

    }

    class UpdatePersonInfoTask extends AsyncTask<String, String, String>
    {
        MaterialDialog materialDialog;

        public UpdatePersonInfoTask(MaterialDialog materialDialog)
        {
            this.materialDialog = materialDialog;
        }

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
                    .add("aid", account.getString("aid", ""))
                    .add("key", params[0])
                    .add("value", params[1])
                    .build();
            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/updatePerson")
                    .post(formBody)
                    .build();
            String body = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                body = response.body().string();
                Log.i(Global.TAG, "===========================================");
                Log.i(Global.TAG, "UpdatePersonInfoTask code-->" + Tool.getRepCode(body));
                Log.i(Global.TAG, "UpdatePersonInfoTask body-->" + body);
                Log.i(Global.TAG, "===========================================");
            }
            catch (Exception e)
            {
                Log.e(Global.TAG, "UpdatePersonInfoTask doInBackground Exception-->" + e.toString());
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
                    JSONObject dataJsonObj = Tool.getDataJsonObj(s);
                    Tool.showToast(ModifyActivity.this, dataJsonObj.getString("status"));
                    SharedPreferences.Editor editor = account.edit();
                    editor.putString(dataJsonObj.getString("key"), dataJsonObj.getString("value"));
                    editor.commit();
                    if (materialDialog != null)
                    {
                        materialDialog.dismiss();
                    }

                    //更新UI数据
                    updateData();

                }
                else
                {
                    Tool.showToast(ModifyActivity.this, (Tool.getMainJsonObj(s)).getString("data"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
