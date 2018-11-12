package com.example.hideseekapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Handler handler = new Handler();
    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 2;
    static double Latitude;
    static double Longitude;
    LocationRequest locationRequest;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, 5000);
                setupMyLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
        handler.postDelayed(runnable, 1000);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            setupMyLocation();
        }
        LatLng start = new LatLng(Latitude, Longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.addMarker(new MarkerOptions().position(start).title("起點!!"));
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }


    @SuppressLint("MissingPermission")
    private void setupMyLocation() {
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager=
                (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = "gps";
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("LOCATION", location.getLatitude() + "/"
                    + location.getLongitude()); //經緯度
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(),
                            location.getLongitude())
                    , 15));
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
            MapItem MapItem = new MapItem(Longitude ,Latitude);
            MapItem.map_update();
//        mMap.setOnMyLocationButtonClickListener(
//                new GoogleMap.OnMyLocationButtonClickListener() {
//                    @Override
//                    public boolean onMyLocationButtonClick() {
//                        //透過位置服務，取得目前裝置所在
//                        gpsLocation();
//                        return false;
//                    }
//                });
        }
    }

    private void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);//更新間隔
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    private void gpsLocation() {
        LocationManager locationManager=
                (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = "gps";
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.i("LOCATION", location.getLatitude()+"/"
                    +location.getLongitude()); //經緯度
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(),
                            location.getLongitude())
                    , 15));
        }

//        Longitude =location.getLongitude();
//        Latitude = location.getLatitude();
//
//        MapItem MapItem = new MapItem(Longitude ,Latitude);
//        MapItem.map_update();
//        System.out.println("經緯度:"+Longitude+"，"+Latitude);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //允許權限
                    setupMyLocation();
                } else {
                    //拒絕授權, 停用MyLocation功能
                }
                break;
        }
    }

}

