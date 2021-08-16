package com.appwebstudios.webview

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import com.appwebstudios.webview.R
import com.appwebstudios.webview.MainActivity
import org.json.JSONObject
import androidx.annotation.RequiresApi
import android.os.Build
import android.content.Intent
import android.app.Activity
import android.util.Log
import android.webkit.*
import org.apache.commons.io.IOUtils
import java.io.StringWriter
import java.lang.Exception
import java.net.URL

/*
 *@aurthur Rahul Gaur
 *@date 14.11.19
 */
class MainActivity : AppCompatActivity() {

    private var flag = false
    private var checker = 0

    private var result = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mWebView = findViewById<WebView>(R.id.webView)
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.e(TAG, "onConsoleMessage: console " + consoleMessage.message())
                return true
            }
        }
        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val url = request.url.toString()
                if (flag && checker == 0) {
                    checker = 1
                    try {
                        val aURL = URL(url)
                        val conn = aURL.openConnection()
                        conn.connect()
                        val `is` = conn.getInputStream()
                        val writer = StringWriter()
                        IOUtils.copy(`is`, writer, "UTF-8")
                        Log.e(TAG, "shouldInterceptRequest: string $writer")
                        val `object` = JSONObject(writer.toString())
                        Log.e(TAG, "shouldInterceptRequest: json object $`object`")
                        result = if (`object`.getString("completed").contains("false")) {
                            1
                        } else {
                            2
                        }
                        return super.shouldInterceptRequest(view, request)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "shouldInterceptRequest: exception " + e.message)
                    }
                }
                return null
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                Log.e(TAG, "onReceivedError: error " + error.description)
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.e(TAG, "onPageFinished: finish $url")
                if (url.contains("/></head><body>")) {
                    flag = true
                }
                if (result == 1) {
                    finishSuccessActivity()
                } else if (result == 2) {
                    finishFailedActivity()
                }
            }
        }
        val content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<html><head>" +
                "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
                "</head><body><form method=\"get\" action=\"https://jsonplaceholder.typicode.com/todos/1\"><button type=\"submit\">Continue</button></form></body></html>"

        //<html><head></head><body><a href=\"URL\">get emp data</a></body></html>

        //<a href="https://jsonplaceholder.typicode.com/todos/1">get emp data</a>
        mWebView.loadData(content, "text/html; charset=utf-8", "UTF-8")
    }

    fun finishSuccessActivity() {
        Log.e(TAG, "finishSuccessActivity: called finish success ")
        val returnIntent = Intent()
        returnIntent.putExtra("result", "False")
        setResult(RESULT_OK, returnIntent)
        finish()
    }

    fun finishFailedActivity() {
        Log.e(TAG, "finishFailedActivity: called failed ")
        val returnIntent = Intent()
        setResult(RESULT_CANCELED, returnIntent)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}