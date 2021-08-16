package com.appwebstudios.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {

    companion object {
        private const val TAG = "Welcome"
        private const val SUCCESS_RESPONSE = 111
    }

    private var statusTV: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        statusTV = findViewById(R.id.statusView)
        val webBtn = findViewById<Button>(R.id.webBtn)
        webBtn.setOnClickListener { v: View? ->
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(i, SUCCESS_RESPONSE)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //result is ok
                Log.e(TAG, "onActivityResult: everything worked fine ")
                statusTV!!.text = "Status: Success"
            } else if (resultCode == RESULT_CANCELED) {
                //result is not ok
                Log.e(TAG, "onActivityResult: something wrong ")
                statusTV!!.text = "Status: Failed"
            }
        }
    }

}