package com.gaoyy.easysoical.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by gaoyy on 2016/3/2/0002.
 */
public class Tool
{
    public static void showSnackbar(View view,String text)
    {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
