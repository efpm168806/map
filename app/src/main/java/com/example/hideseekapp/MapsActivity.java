package com.example.hideseekapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Handler handler = new Handler();
    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 2;
    static double Latitude;
    static double Longitude;
    LocationRequest locationRequest;
    String fileName="Login";
    String ID;
    String[][] list;
    int a;

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

    public MapsActivity(String[][] list, int a) {
        this.list = list;
        this.a = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        game_state();

        try{    //讀取ID
            FileInputStream inputStream = openFileInput(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = inputStream.read(bytes))!=-1){
                baos.write(bytes,0,len);
            }
            baos.flush();
            byte[] result = baos.toByteArray();
            ID = new String(result);
            System.out.println("id:"+ID);
            baos.close();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    //check game statement start or not
    public void game_state(){
        try{
            String game_state = DBconnect.executeQuery("SELECT game_state FROM map WHERE User_id = '"+ID+"' ");
            JSONObject game = new JSONArray(game_state).getJSONObject(0);
            String Game_state=(String)game.getString("game_state").toString();
            if(Game_state==""||Game_state==null||Game_state=="0"){
                String result = DBconnect.executeQuery("UPDATE map SET game_state= '1' WHERE User_id = '"+ID+"'");
                game_start();
            }else{
                //game already start so do nothing
            }

        }
        catch (JSONException e){

        }
    }
    //finish the game
    public void game_finish(){
        try{
            String game_state = DBconnect.executeQuery("SELECT game_state FROM map WHERE User_id = '"+ID+"' ");
            JSONObject game = new JSONArray(game_state).getJSONObject(0);
            String Game_state=(String)game.getString("game_state").toString();
            String result = DBconnect.executeQuery("UPDATE map SET game_state= '0' WHERE User_id = '"+ID+"'");
        }
        catch (JSONException e){

        }
    }
    //start game total time count down
    public void game_start(){
        new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {
                //倒數10分鐘
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                game_finish();
            }
        }.start();
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
            MapItem MapItem = new MapItem(Longitude ,Latitude,ID);
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
    //標誌全部玩家的位置
    public void MakePoint(String Ghost){
        mMap.clear();
        //不是鬼的人的畫面
        if(Ghost=="0"){
            for(int i=0 ; i<a ; i++){
                double lat = Double.parseDouble(list[i][2]);
                double longt = Double.parseDouble(list[i][1]);
                if(list[i][3]=="1"){
                    int n = (int) (Math.random()*4 +1);
                    double n1 = (double)(Math.random()-0.001);
                    if(n=='1'){
                        lat = lat + n1;
                        LatLng fakegps = new LatLng(lat, longt);
                        mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                    }else if(n=='2'){
                        lat = lat - n1;
                        LatLng fakegps = new LatLng(lat, longt);
                        mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                    }else if(n=='3'){
                        longt = longt + n1;
                        LatLng fakegps = new LatLng(lat, longt);
                        mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                    }else{
                        longt = longt - n1;
                        LatLng fakegps = new LatLng(lat, longt);
                        mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                    }
                    //manyme
                }else if(list[i][3]=="2"){
                    double n1 = (double)(Math.random()-0.0005);
                    double n2 = (double)(Math.random()-0.0005);
                    LatLng manyme1 = new LatLng(Double.parseDouble(list[i][2])+n1, Double.parseDouble(list[i][1])+n2);
                    LatLng manyme2 = new LatLng(Double.parseDouble(list[i][2])+n1, Double.parseDouble(list[i][1])-n2);
                    LatLng manyme3 = new LatLng(Double.parseDouble(list[i][2])-n1, Double.parseDouble(list[i][1])+n2);
                    LatLng manyme4 = new LatLng(Double.parseDouble(list[i][2])-n1, Double.parseDouble(list[i][1])-n2);
                    mMap.addMarker(new MarkerOptions().position(manyme1).title("other player"));
                    mMap.addMarker(new MarkerOptions().position(manyme2).title("other player"));
                    mMap.addMarker(new MarkerOptions().position(manyme3).title("other player"));
                    mMap.addMarker(new MarkerOptions().position(manyme4).title("other player"));
                    //normal
                }else if(list[i][3]=="3"){
                    //隱形帳篷所以不標點
                }else{
                    LatLng point = new LatLng(Double.parseDouble(list[i][2]), Double.parseDouble(list[i][1]));
                    mMap.addMarker(new MarkerOptions().position(point).title("other player"));
                }
            }

            LatLng myself = new LatLng(Latitude, Longitude);
            mMap.addMarker(new MarkerOptions().position(myself).title("me"));

        //鬼玩家的畫面
        }else{
            for(int i=0 ; i<a ; i++){
                //如果鬼看到鬼
                if(list[i][4]=="1"){
                    LatLng point = new LatLng(Double.parseDouble(list[i][2]), Double.parseDouble(list[i][1]));
                    mMap.addMarker(new MarkerOptions().position(point).title("ghost player"));
                //如果鬼看到人
                }else{
                    double lat = Double.parseDouble(list[i][2]);
                    double longt = Double.parseDouble(list[i][1]);
                    if(list[i][3]=="1"){
                        int n = (int) (Math.random()*4 +1);
                        double n1 = (double)(Math.random()-0.001);
                        if(n=='1'){
                            lat = lat + n1;
                            LatLng fakegps = new LatLng(lat, longt);
                            mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                        }else if(n=='2'){
                            lat = lat - n1;
                            LatLng fakegps = new LatLng(lat, longt);
                            mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                        }else if(n=='3'){
                            longt = longt + n1;
                            LatLng fakegps = new LatLng(lat, longt);
                            mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                        }else{
                            longt = longt - n1;
                            LatLng fakegps = new LatLng(lat, longt);
                            mMap.addMarker(new MarkerOptions().position(fakegps).title("other player"));
                        }
                        //manyme
                    }else if(list[i][3]=="2"){
                        double n1 = (double)(Math.random()-0.0005);
                        double n2 = (double)(Math.random()-0.0005);
                        LatLng manyme1 = new LatLng(Double.parseDouble(list[i][2])+n1, Double.parseDouble(list[i][1])+n2);
                        LatLng manyme2 = new LatLng(Double.parseDouble(list[i][2])+n1, Double.parseDouble(list[i][1])-n2);
                        LatLng manyme3 = new LatLng(Double.parseDouble(list[i][2])-n1, Double.parseDouble(list[i][1])+n2);
                        LatLng manyme4 = new LatLng(Double.parseDouble(list[i][2])-n1, Double.parseDouble(list[i][1])-n2);
                        mMap.addMarker(new MarkerOptions().position(manyme1).title("other player"));
                        mMap.addMarker(new MarkerOptions().position(manyme2).title("other player"));
                        mMap.addMarker(new MarkerOptions().position(manyme3).title("other player"));
                        mMap.addMarker(new MarkerOptions().position(manyme4).title("other player"));
                        //normal
                    }else if(list[i][3]=="3"){
                        //隱形帳篷所以不標點
                    }else{
                        LatLng point = new LatLng(Double.parseDouble(list[i][2]), Double.parseDouble(list[i][1]));
                        mMap.addMarker(new MarkerOptions().position(point).title("other player"));
                    }
                }
            }

            LatLng myself = new LatLng(Latitude, Longitude);
            mMap.addMarker(new MarkerOptions().position(myself).title("me"));
        }
    }

    public void UseItem(int item_index){
        // int item_index = item_index;
        //int item_index = '1';
        //使用道具
        item_update(item_index);
    }

    public void item_update(final int item_index) {
        Thread itemDB = new Thread(new Runnable() {
            @Override
            public void run() {
                itemDB(item_index);
            }
        });
        itemDB.start();
        try {
            itemDB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }

    public void itemDB(int item_index){
        try{
            String item = DBconnect.executeQuery("SELECT item FROM map WHERE User_id = '"+ID+"' ");
            JSONObject itemID = new JSONArray(item).getJSONObject(0);
            String ItemID=(String)itemID.getString("item").toString();
            if(ItemID=="0"||ItemID==null||ItemID==""){
                String result = DBconnect.executeQuery("UPDATE map SET item= "+item_index+" WHERE User_id = '"+ID+"'");
                item_time_start();
            }else{
                //道具已經在使用中
            }

        }
        catch (JSONException e){
            System.out.println("itemDB error");
        }
    }

    //item time count down
    public void item_time_start(){
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                //倒數30 秒
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                item_finish();
            }
        }.start();
    }

    public void item_finish(){
        try{
            String item = DBconnect.executeQuery("SELECT item FROM map WHERE User_id = '"+ID+"' ");
            JSONObject itemID = new JSONArray(item).getJSONObject(0);
            String result = DBconnect.executeQuery("UPDATE map SET item= '0' WHERE User_id = '"+ID+"'");
        }
        catch (JSONException e){
            System.out.println("item_finish error");
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
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(location.getLatitude(),
//                            location.getLongitude())
//                    , 15));
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

