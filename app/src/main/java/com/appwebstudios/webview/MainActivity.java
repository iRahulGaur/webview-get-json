package com.appwebstudios.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/*
@aurthur Rahul Gaur
@date 14.11.19
*/
public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private static final String TAG = "MainActivity";
    private TextView textView;
    public static String msg = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webView);

        //mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "onConsoleMessage: console " + consoleMessage.message());
                return true;
            }
        });

        textView = findViewById(R.id.dataTV);

        textView.setText("Completed:");

        MyWebViewClient myWebViewClient = new MyWebViewClient(this);
        mWebView.setWebViewClient(myWebViewClient);

        String content =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                        "<html><head>"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                        "</head><body><a href=\"https://jsonplaceholder.typicode.com/todos/1\">get emp data</a></body></html>";

        //<a href="https://jsonplaceholder.typicode.com/todos/1">get emp data</a>
        mWebView.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }

    @SuppressLint("SetTextI18n")
    public void setValue(String value) {
        textView.setText("Completed: " + value);
    }

    @SuppressLint("SetTextI18n")
    public void start(View view) {
        Log.e(TAG, "start: clicked ");
        textView.setText("Completed: " + msg);
    }
}
