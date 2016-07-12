package com.example.kristijan.sharemeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
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

    public static final int REQUEST_CODE = 11876;

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

                //payment here


                openShareActivity();
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

    public void onBraintreeSubmit(View v) {
        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken("eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJhODVkNTA3OGZlMTNlNGU2NWJmZWM2ZDYyZmNhMDFmNzNmM2U3M2EzMDU2MjY1ZmUxYzE3ODZiNTZmM2NiY2FhfGNyZWF0ZWRfYXQ9MjAxNi0wNy0xMVQxNToxNDowNC4zODU3Mzg0MTUrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(paymentRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                // Send the nonce to your server.
            }
        }
    }

    private void openShareActivity(){
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }


}
