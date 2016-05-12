package com.example.kristijan.sharemeal;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by kristijan on 12/05/16.
 */
public class ShareMealApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
