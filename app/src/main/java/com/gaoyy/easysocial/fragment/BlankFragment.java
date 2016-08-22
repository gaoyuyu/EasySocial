package com.gaoyy.easysocial.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.utils.Global;
import com.gaoyy.easysocial.utils.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment
{



    public BlankFragment()
    {
        // Required empty public constructor
    }

    private ProgressBar progressBar;
    private  Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);


        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        Button btn = (Button) rootView.findViewById(R.id.button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.ttprogressBar);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ScanTask().execute();
            }
        });


        return rootView;
    }

    class ScanTask extends AsyncTask<String, String, String>
    {
        private boolean status;


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {

            Request request = new Request.Builder()
                    .url(Global.HOST_URL + "Public/scanPlugin")
                    .build();
            String address = null;
            try
            {
                Response response = Tool.getOkHttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String body = response.body().string();

                address = Tool.getMainJsonObj(body).getString("data");
                Log.i(Global.TAG, "body-->" + body);
                Log.i(Global.TAG, "address-->" + address);
                Log.i(Global.TAG, "code-->" + Tool.getRepCode(body));
            }
            catch (Exception e)
            {
                Log.i(Global.TAG, "e-->" + e.toString());
            }
            return address;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            download(s);
        }
    }

    public void download(final String url)
    {
        final String destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(Global.TAG,"file dir==>"+destFileDir);
        Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = Tool.getOkHttpClient().newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                int total= 0;
                FileOutputStream fos = null;
                try
                {
                    is = response.body().byteStream();
                    Log.i(Global.TAG,"final ---->"+response.body().contentLength());
                    progressBar.setMax((int)response.body().contentLength());
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                        total+=len;
                        Log.i(Global.TAG,"total  ---->"+total);

                        progressBar.setProgress(total);
                    }
                    fos.flush();

                }
                catch (IOException e)
                {
                }
                finally
                {
                    try
                    {
                        if (is != null) is.close();
                    }
                    catch (IOException e)
                    {
                    }
                    try
                    {
                        if (fos != null) fos.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        });


    }

    private String getFileName(String path)
    {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

}
