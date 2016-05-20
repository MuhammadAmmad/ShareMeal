package com.example.kristijan.sharemeal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import butterknife.BindView;


public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HashMap<Marker, String> mHashMap = new HashMap<>();

    public static final String PARAM_EVENTID = "param_event_id";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_maps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Intent intent = new Intent(MapsActivity.this, ViewEventActivity.class);
                intent.putExtra(PARAM_EVENTID, mHashMap.get(marker));
                startActivity(intent);

                Log.d("Marker clicked", marker.getTitle());
            }
        });



        String eventsUrl = Constants.FIREBASE_URL + "/events";
        new Firebase(eventsUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                        String title = dataSnapshot.child("meal").getValue(String.class);
                        String eventID = dataSnapshot.getKey();

                        LatLng childLocation = new LatLng(latitude,longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(childLocation).title(title));
                        mHashMap.put(marker, eventID); //to display event details
                        Log.v("E_CHILD_ADDED",childLocation.toString());


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

        enableMyLocation();


    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) { //that might be only for newer android versions
            // Permission to access the location is missing.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
}
