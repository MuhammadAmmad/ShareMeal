package com.example.kristijan.sharemeal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

/**
 * Created by kristijan on 11/05/16.
 */
public class FirebaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        // other setup code
    }

    Firebase myFirebaseRef = new Firebase("https://luminous-inferno-7895.firebaseio.com/");

}
