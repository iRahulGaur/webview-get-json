package com.appwebstudios.webview;

import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

/*
@aurthur Rahul Gaur
@date 14.11.19
*/
public class MyWebViewClient extends WebViewClient {

    private static final String TAG = "MyWebViewClient";
    private boolean flag = false;
    private MainActivity context;

    MyWebViewClient(MainActivity context) {
        this.context = context;
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();

        if (flag) {
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();

                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                Log.e(TAG, "shouldInterceptRequest: string " + writer.toString());

                JSONObject object = new JSONObject(writer.toString());
                Log.e(TAG, "shouldInterceptRequest: json object " + object);

                MainActivity.msg = String.valueOf(object.getBoolean("completed"));

                context.setValue(String.valueOf(object.getBoolean("completed")));

                return super.shouldInterceptRequest(view, request);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "shouldInterceptRequest: exception " + e.getMessage());
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "onReceivedError: error " + error.getDescription());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.e(TAG, "onPageFinished: finish " + url);
        if (url.contains("/></head><body><a href=")) {
            flag = true;
        }
    }
}
