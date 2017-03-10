package com.example.informatik.geoloacation;


import android.*;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import static android.support.v4.app.ActivityCompat.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnRequestPermissionsResultCallback {
    private static final boolean DEBUG = true;
    private GoogleMap mMap;
    private boolean isResume = false;

    LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create a custom Marker with an arrow symbol
        final MarkerOptions myPositoon = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappointer))
                .position(new LatLng(41.889, -45.444))
                .flat(true);


        locationListener = new LocationListener() {
            // If the Locationmanager found a Location
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Posotion", Objects.toString(location.getLongitude(), null) + " " + Objects.toString(location.getAccuracy(), null) + " " + location.getProvider());

                // Gets the Latitude and Longitude, and set the pointer to this location
                LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                // Create a circle with the current accuracy of the location
                mMap.addCircle(new CircleOptions()
                        .center(myPosition)
                        .radius(location.getAccuracy())
                        .strokeWidth(20)
                        .strokeColor(Color.argb(50, 255, 0, 0))
                        .fillColor(Color.argb(50, 0, 255, 0)));

                mMap.addMarker(myPositoon.position(myPosition));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Posotion", "Status Changed");
            }

            public void onProviderEnabled(String provider) {
                Log.d("Posotion", "Provider Enabled: " + provider);
            }

            public void onProviderDisabled(String provider) {
                Log.d("Posotion", "Provider Disabled: " + provider);
                mMap.clear();
            }
        };

        locateDevice();


    }

    private void locateDevice() {
        // Check if permissions were given for locating
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            DialogFragment newFragment = new FireMissilesDialogFragment();
            newFragment.show(getFragmentManager(), "Destructor");
            return;
        } else {
            // Initialize the LocationManager which is used to obtain the devices Location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


            // On start gets the last known location of the GPS provider, to give a fast first location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null && !isResume) {
                LatLng myPosition = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myPosition).title("My Position"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            }


            // Requests location uodates every .5 sec of both providers.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = true;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager.removeUpdates(locationListener);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isResume) {
            locateDevice();
        }

    }
}
