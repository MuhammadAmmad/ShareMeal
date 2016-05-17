package com.example.kristijan.sharemeal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    @BindView(R.id.rsvpBtn) Button rsvpBtn;

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

        eventsUrl = Constants.FIREBASE_URL + "/events";

        ButterKnife.bind(this);

        System.out.println("event intent");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            eventID = extras.getString(PARAM_EVENTID);
            System.out.println("event"+eventID);
        }

        rsvpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Firebase(eventsUrl+"/"+eventID+"/usersJoined")
                        .push().setValue(mUserId);
                new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/eventsJoined")
                        .push().setValue(eventID);

            }
        });


        /*Firebase ref = new Firebase(Constants.FIREBASE_URL);
// fetch a list of Mary's groups
        ref.child("users/mchen/groups").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                // for each group, fetch the name and print it
                String groupKey = snapshot.getKey();
                ref.child("groups/" + groupKey + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println("Mary is a member of this group: " + snapshot.getValue());
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // ignore
                    }
                });
            }
        });*/


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
