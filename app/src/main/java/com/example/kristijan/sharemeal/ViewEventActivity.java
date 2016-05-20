package com.example.kristijan.sharemeal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.BindView;

/**
 * Created by kristijan on 15/05/16.
 */
public class ViewEventActivity extends BaseActivity {

    public static final String PARAM_EVENTID = "param_event_id";

    private Firebase mRef;

    private String eventsUrl;

    private String eventID;

    @BindView(R.id.meal) TextView meal;
    @BindView(R.id.locationAddress) TextView locationAddress;
    @BindView(R.id.maxPerson) TextView maxPerson;
    @BindView(R.id.rsvpBtn) Button rsvpBtn;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view_event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsUrl = Constants.FIREBASE_URL + "/events";

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            eventID = extras.getString(PARAM_EVENTID);
            System.out.println("event"+eventID);
        }

        rsvpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new Firebase(eventsUrl+"/"+eventID+"/usersJoined")
                        .push().setValue(getmUserId());
                new Firebase(Constants.FIREBASE_URL + "/users/" + getmUserId() + "/eventsJoined")
                        .push().setValue(eventID);

            }
        });

        //firebases example that can be useful
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
        //only single call needed!!!
    }

}
