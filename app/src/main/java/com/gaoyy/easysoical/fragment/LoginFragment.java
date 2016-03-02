package com.gaoyy.easysoical.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoyy.easysoical.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by gaoyy on 2016/3/1/0001.
 */
public class LoginFragment extends Fragment
{

    private View rootView;
    private Toolbar loginToolbar;
    private MaterialEditText email;
    private MaterialEditText password;
    private AppCompatActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        assignViews(rootView);
        activity = (AppCompatActivity) getActivity();
        initToolbar();
        return rootView;
    }

    private void assignViews(View rootView)
    {
        loginToolbar = (Toolbar) rootView.findViewById(R.id.login_toolbar);
        email = (MaterialEditText) rootView.findViewById(R.id.email);
        password = (MaterialEditText) rootView.findViewById(R.id.password);
    }
    public void initToolbar()
    {
        loginToolbar.setTitle(R.string.login);
        activity.setSupportActionBar(loginToolbar);
        //设置返回键可用
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
