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
        maxPerson.setMaxValue(10);

        //Gets whether the selector wheel wraps when reaching the min/max value.
        maxPerson.setWrapSelectorWheel(true);


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

                Event event = new Event(getmUserId(), meal.getText().toString(),
                        locationAddress.getText().toString(), Double.toString(finalLatitude),
                        Double.toString(finalLongitude), maxPerson.getValue()); //change that
                Firebase fire =new Firebase(eventsUrl).push();
                        fire.setValue(event);
                String eventID = fire.getKey();

                new Firebase(Constants.FIREBASE_URL + "/users/" + getmUserId() + "/ownsEvents")
                        .push().setValue(eventID);

                //how to check if successful?
                //show snackbar saying event created --not working yet
                Snackbar snackbar = Snackbar
                        .make(v, "Event created", Snackbar.LENGTH_LONG);

                snackbar.show();
                finish();
            }
        });


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
