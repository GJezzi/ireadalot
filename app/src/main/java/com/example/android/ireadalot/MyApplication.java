package com.example.android.ireadalot;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by gjezzi on 29/07/16.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate () {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Firebase.setAndroidContext(this);
    }
}
