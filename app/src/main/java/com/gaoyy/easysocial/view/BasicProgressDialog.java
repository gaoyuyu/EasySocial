package com.gaoyy.easysocial.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.gaoyy.easysocial.R;

import java.lang.ref.WeakReference;

public class BasicProgressDialog extends Dialog
{

    private Context mContent;
    private static WeakReference<BasicProgressDialog> basicProgressDialogWeakReference = null;

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
        basicProgressDialogWeakReference = new WeakReference<>(new BasicProgressDialog(context,R.style.BasicProgressDialog));
        BasicProgressDialog basicProgressDialog = basicProgressDialogWeakReference.get();
        basicProgressDialog.setContentView(R.layout.dialog_loading);
        basicProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return basicProgressDialog;

    }


    public BasicProgressDialog setMessage(String message)
    {
        BasicProgressDialog basicProgressDialog = basicProgressDialogWeakReference.get();
        TextView loading = (TextView) basicProgressDialog.findViewById(R.id.loading_hint);
        if (loading != null)
        {
            loading.setText(message);
        }
        return basicProgressDialog;
    }

}

