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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import butterknife.BindView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by kristijan on 12/05/16.
 */
public class CreateEventActivity extends BaseActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;

    private Firebase mRef;

    private String mUserId;
    private String eventsUrl;

    private Place selectedPlace;
    private boolean placeSelected = false;

    private static final int PLACE_PICKER_REQUEST = 1020;

    Location lastLocation;

    @BindView(R.id.meal) EditText meal;
    @BindView(R.id.locationAddress) EditText locationAddress;
    @BindView(R.id.timeText) EditText timeText;
    @BindView(R.id.dateText) EditText dateText;
    @BindView(R.id.maxPerson) NumberPicker maxPerson;
    @BindView(R.id.locationButton) Button locationButton;
    @BindView(R.id.createButton) Button createButton;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastLocation = null;


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        maxPerson.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        maxPerson.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        maxPerson.setWrapSelectorWheel(true);


        //Set a value change listener for NumberPicker
        /*maxPerson.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                tv.setText("Selected Number : " + newVal);
            }
        });*/


        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        CreateEventActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateEventActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });



        eventsUrl = Constants.FIREBASE_URL + "/events";
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double finalLatitude = 0;
                double finalLongitude = 0;
                if (placeSelected){
                    finalLatitude = selectedPlace.getLatLng().latitude;
                    finalLongitude = selectedPlace.getLatLng().longitude;
                }
                else if (lastLocation != null){
                    finalLatitude = lastLocation.getLatitude();
                    finalLongitude = lastLocation.getLongitude();
                }
                else{
                    //change to alert to select location
                    finalLatitude = 0;
                    finalLongitude = 0;
                }

                Event event = new Event(mUserId, meal.getText().toString(),
                        locationAddress.getText().toString(), Double.toString(finalLatitude),
                        Double.toString(finalLongitude), maxPerson.getValue()); //change that
                Firebase fire =new Firebase(eventsUrl).push();
                        fire.setValue(event);
                String eventID = fire.getKey();

                new Firebase(Constants.FIREBASE_URL + "/users/" + mUserId + "/ownsEvents")
                        .push().setValue(eventID);

                //how to check if successful?
                //clear everything or go to previous activity --done
                //show snackbar saying event created
                Snackbar snackbar = Snackbar
                        .make(v, "Event created", Snackbar.LENGTH_LONG);//not working

                snackbar.show();
                finish();
            }
        });

        /* // Set up LisVview
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

        locationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createPlacePicker();
            }
        });
    }

    private void createPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedPlace = PlacePicker.getPlace(data, this);
                placeSelected = true;

            }
        }
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

        //MOVE TO SIDEBAR
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            unauthenticate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocation();

        if(lastLocation!=null) {
            //latitude of location
            double myLatitude = lastLocation.getLatitude();

            //longitude of location
            double myLongitude = lastLocation.getLongitude();

            // Add a marker in Sydney and move the camera
            LatLng eventLocation = new LatLng(myLatitude, myLongitude);
            mMap.addMarker(new MarkerOptions().position(eventLocation).title("Location of the event"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLocation));
        }


    }



    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        timeText.setText(""+hourOfDay+":"+minute);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        dateText.setText(""+dayOfMonth+"/"+monthOfYear+"/"+year);
    }

    private void getLocation(){
        SmartLocation.with(getApplicationContext()).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        lastLocation = location;
                    }});
    }

    private void updateLocation(){
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
        lastLocation = locationManager.getLastKnownLocation(provider);

        if (lastLocation == null)
            getLocation();
    }
}
