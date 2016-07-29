package com.example.android.ireadalot;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by gjezzi on 29/07/16.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate () {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
