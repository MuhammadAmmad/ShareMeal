package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kristijan on 15/05/16.
 */
public class ViewEventActivity extends AppCompatActivity {

    public static final String PARAM_EVENTID = "param_event_id";

    private Firebase mRef;

    private String mUserId;
    private String eventsUrl;

    private String eventID;

    @BindView(R.id.meal) TextView meal;
    @BindView(R.id.locationAddress) TextView locationAddress;
    @BindView(R.id.latitude) TextView latitude;
    @BindView(R.id.longitude) TextView longitude;
    @BindView(R.id.maxPerson) TextView maxPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        // Check Authentication
        mRef = new Firebase(Constants.FIREBASE_URL);
        if (mRef.getAuth() == null) {
            loadLoginView();
        }

        try {
            mUserId = mRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }

        eventsUrl = Constants.FIREBASE_URL + "/users/" + mUserId + "/events";

        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            eventID = extras.getString(PARAM_EVENTID);
        }

        new Firebase(eventsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(eventID)){
                            meal.setText(dataSnapshot.child("meal").getValue(String.class));
                            locationAddress.setText(dataSnapshot.child("locationAddress").getValue(String.class));
                            latitude.setText(dataSnapshot.child("latitude").getValue(String.class));
                            longitude.setText(dataSnapshot.child("longitude").getValue(String.class));
                            maxPerson.setText(dataSnapshot.child("personLimit").getValue(String.class));
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
