package com.appwebstudios.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    private TextView statusTV;
    private static final String TAG = "Welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        statusTV = findViewById(R.id.statusView);
        Button webBtn = findViewById(R.id.webBtn);

        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //result is ok
                Log.e(TAG, "onActivityResult: everything worked fine ");
                statusTV.setText("Status: Success");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //result is not ok
                Log.e(TAG, "onActivityResult: something wrong ");
                statusTV.setText("Status: Failed");
            }
        }
    }
}
