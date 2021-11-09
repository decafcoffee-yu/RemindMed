package com.example.cecs491project.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.cecs491project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.model.PlacesSearchResult;


import java.util.Objects;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button back;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    double currentLat, currentLng;
    hospitalSearch hospitalSearch = new hospitalSearch();
    pharmacySearch pharmacySearch = new pharmacySearch();
    Button hospital, pharmacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        back = findViewById(R.id.go_back);
        hospital = findViewById(R.id.hospital);
        pharmacy = findViewById(R.id.pharmacy);
        back.setOnClickListener(v -> MapActivity.super.onBackPressed());
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocation();

    }

    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(location -> {
            if(location !=null){
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                hospitalSearch.setLat(currentLat);
                hospitalSearch.setLng(currentLng);
                pharmacySearch.setLat(currentLat);
                pharmacySearch.setLng(currentLng);
                mapFragment.getMapAsync(googleMap -> {
                    LatLng latLng = new LatLng(currentLat, currentLng);
                    MarkerOptions options = new MarkerOptions().position(latLng).title("You are here");
                    googleMap.setMyLocationEnabled(true);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    googleMap.addMarker(options);

                    PlacesSearchResult[] placesSearchResults = hospitalSearch.run().results;

                    for (PlacesSearchResult placesSearchResult : placesSearchResults) {
                        Log.e("responseTag", placesSearchResult.toString());
                        double lat = placesSearchResult.geometry.location.lat;
                        double lng = placesSearchResult.geometry.location.lng;
                        MarkerOptions optionsNearby = new MarkerOptions().position(new LatLng(lat, lng));
                        String name = placesSearchResult.name;
                        optionsNearby.title(name);
                        mMap.addMarker(optionsNearby);
                    }
                    hospital.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMap.clear();
                            PlacesSearchResult[] placesSearchResults = hospitalSearch.run().results;

                            for (PlacesSearchResult placesSearchResult : placesSearchResults) {
                                Log.e("responseTag", placesSearchResult.toString());
                                double lat = placesSearchResult.geometry.location.lat;
                                double lng = placesSearchResult.geometry.location.lng;
                                MarkerOptions optionsNearby = new MarkerOptions().position(new LatLng(lat, lng));
                                String name = placesSearchResult.name;
                                optionsNearby.title(name);
                                mMap.addMarker(optionsNearby);
                            }
                        }
                    });

                    pharmacy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMap.clear();
                            PlacesSearchResult[] placesSearchResults = pharmacySearch.run().results;

                            for (PlacesSearchResult placesSearchResult : placesSearchResults) {
                                Log.e("responseTag", placesSearchResult.toString());
                                double lat = placesSearchResult.geometry.location.lat;
                                double lng = placesSearchResult.geometry.location.lng;
                                MarkerOptions optionsNearby = new MarkerOptions().position(new LatLng(lat, lng));
                                String name = placesSearchResult.name;
                                optionsNearby.title(name);
                                mMap.addMarker(optionsNearby);
                            }
                        }
                    });

                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 44){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }



}