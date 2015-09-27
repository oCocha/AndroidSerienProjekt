package com.serien.android.androidserienprojekt.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.serien.android.androidserienprojekt.R;

/**
 * Created by Igor on 25.09.2015.
 */
public class LoadingActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        int SCREEN_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LoadingActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        }, SCREEN_TIME_OUT);
    }
}
