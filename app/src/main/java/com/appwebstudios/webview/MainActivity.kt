package com.appwebstudios.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

/*
 *@aurthur Rahul Gaur
 *@date 14.11.19
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private boolean flag = false;
    private int checker = 0;
    private int result = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView mWebView = findViewById(R.id.webView);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "onConsoleMessage: console " + consoleMessage.message());
                return true;
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (flag && checker == 0) {
                    checker = 1;
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

                        if (object.getString("completed").contains("false")) {
                            result = 1;
                        } else {
                            result = 2;
                        }

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
                if (url.contains("/></head><body>")) {
                    flag = true;
                }

                if (result == 1) {
                    finishSuccessActivity();
                } else if (result == 2) {
                    finishFailedActivity();
                }
            }
        });

        String content =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                        "<html><head>" +
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                        "</head><body><form method=\"get\" action=\"https://jsonplaceholder.typicode.com/todos/1\"><button type=\"submit\">Continue</button></form></body></html>";

        //<html><head></head><body><a href=\"URL\">get emp data</a></body></html>

        //<a href="https://jsonplaceholder.typicode.com/todos/1">get emp data</a>
        mWebView.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }

    void finishSuccessActivity() {
        Log.e(TAG, "finishSuccessActivity: called finish success ");
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "False");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    void finishFailedActivity() {
        Log.e(TAG, "finishFailedActivity: called failed ");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
