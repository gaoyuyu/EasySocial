package com.gaoyy.easysocial.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.ui.MainActivity;


public class GuideFragment extends Fragment
{

    private View rootView;

    public GuideFragment()
    {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        TextView tv =(TextView)rootView.findViewById(R.id.fragment_guide_center);
        Button btn =(Button)rootView.findViewById(R.id.fragment_guide_btn);
        MaterialRippleLayout fragmentGuideMrlayout = (MaterialRippleLayout) rootView.findViewById(R.id.fragment_guide_mrlayout);
        Bundle b = getArguments();
        tv.setText(b.getString("text"));
        if(b.getBoolean("isShowBtn"))
        {
            fragmentGuideMrlayout.setVisibility(View.VISIBLE);
        }
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                SharedPreferences preferences = getActivity().getSharedPreferences("AppIsFirstIn",
                        getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isFirstIn", false);
                editor.commit();
            }
        });
        return rootView;
    }

}
