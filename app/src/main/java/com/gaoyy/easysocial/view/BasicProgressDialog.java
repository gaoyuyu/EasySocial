package com.gaoyy.easysocial.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.gaoyy.easysocial.R;

public class BasicProgressDialog extends Dialog
{

    private Context mContent;
    private static BasicProgressDialog basicProgressDialog = null;

    public BasicProgressDialog(Context context)
    {
        super(context);
        mContent = context;
    }

    public BasicProgressDialog(Context context, int theme)
    {
        super(context, theme);
    }

    public static BasicProgressDialog create(Context context)
    {
        basicProgressDialog = new BasicProgressDialog(context,
                R.style.BasicProgressDialog);
        basicProgressDialog.setContentView(R.layout.dialog_loading);
        basicProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return basicProgressDialog;

    }

    public BasicProgressDialog setTitle(String title)
    {
        return basicProgressDialog;
    }

    public BasicProgressDialog setMessage(String message)
    {
        TextView loading = (TextView) basicProgressDialog.findViewById(R.id.loading_hint);
        if (loading != null)
        {
            loading.setText(message);
        }
        return basicProgressDialog;
    }

}

