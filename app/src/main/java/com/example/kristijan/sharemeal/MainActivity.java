package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

/**
 * Created by kristijan on 12/05/16.
 */
public class MainActivity extends AppCompatActivity{

    private Firebase mRef;



    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public MainActivity() {
        super();
    }

    @Override
        protected void onCreate (@Nullable Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            // Check Authentication
            mRef = new Firebase(Constants.FIREBASE_URL);
            if (mRef.getAuth() == null) {
                loadLoginView();
            }
        }
    }