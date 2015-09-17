package com.serien.android.androidserienprojekt.activities;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Igor on 16.09.2015.
 */
public class InitializeParse extends Application {

    @Override
    public void onCreate(){
        super.onCreate();


        Parse.initialize(this, "3g5Fu5xOa2I4hZQGOWnveDJmvJZfd66mMXzmdSe6", "A5YKm8qcMfG6nSFQ9BKgMy0wjx5T8kMENADfurgd");
    }
}
