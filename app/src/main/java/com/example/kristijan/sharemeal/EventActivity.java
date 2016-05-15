package com.example.kristijan.sharemeal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.firebase.client.Firebase;

/**
 * Created by kristijan on 12/05/16.
 */
public class EventActivity extends AppCompatActivity {

    private Firebase mRef;

    private String mUserId;
    private String eventsUrl;


    protected EditText meal;
    protected EditText locationAddress;
    protected EditText latitude;
    protected EditText longitude;
    protected NumberPicker maxPerson;
    protected Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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




        // Get LocationManager object
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location myLocation = locationManager.getLastKnownLocation(provider);

        //latitude of location
        double myLatitude = myLocation.getLatitude();

        //longitude og location
        double myLongitude = myLocation.getLongitude();





        eventsUrl = Constants.FIREBASE_URL + "/users/" + mUserId + "/events";

        meal = (EditText)findViewById(R.id.meal);
        locationAddress = (EditText)findViewById(R.id.locationAddress);
        latitude = (EditText)findViewById(R.id.latitude);
        longitude = (EditText)findViewById(R.id.longitude);
        maxPerson = (NumberPicker) findViewById(R.id.maxPerson);

        createButton = (Button)findViewById(R.id.createButton);

        maxPerson.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        maxPerson.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        maxPerson.setWrapSelectorWheel(true);

        latitude.setText(String.valueOf(myLatitude));
        longitude.setText(String.valueOf(myLongitude));

        //Set a value change listener for NumberPicker
        /*maxPerson.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                tv.setText("Selected Number : " + newVal);
            }
        });*/

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Event event = new Event(meal.getText().toString(),
                        locationAddress.getText().toString(), latitude.getText().toString(),
                        longitude.getText().toString(), maxPerson.getValue()); //change that
                new Firebase(eventsUrl)
                        .push()
                        .setValue(event);

                //how to check if successful?
                //clear everything or go to previous activity --done
                //show snackbar saying event created
                Snackbar snackbar = Snackbar
                        .make(v, "Event created", Snackbar.LENGTH_LONG);

                snackbar.show();
                finish();
            }
        });

        /*// Set up LisVview
        final ListView listView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        // Add items via the Button and EditText at the bottom of the view.
        final EditText text = (EditText) findViewById(R.id.todoText);
        final Button button = (Button) findViewById(R.id.addButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Event item = new Event(text.getText().toString());
                new Firebase(itemsUrl)
                        .push()
                        .setValue(item);
            }
        });

        // Use Firebase to populate the list.
        new Firebase(itemsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        adapter.add((String) dataSnapshot.child("title").getValue());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        adapter.remove((String) dataSnapshot.child("title").getValue());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        // Delete items when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new Firebase(itemsUrl)
                        .orderByChild("title")
                        .equalTo((String) listView.getItemAtPosition(position))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }

                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mRef.unauth();
            loadLoginView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*
    Location
     */
}
