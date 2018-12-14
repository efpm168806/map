package com.example.hideseekapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,View.OnClickListener {
    Handler handler = new Handler();
    static GoogleMap mMap;
    private static final int REQUEST_LOCATION = 2;
    static double Latitude;
    static double Longitude;
    static double Latitude2=0;
    static double Longitude2=0;
    double totaldis=0;
    ArrayList<String> item_list = new ArrayList<>();
    ImageButton baggage;
    JSONArray item_all ,item2_all;
    LocationRequest locationRequest;
    String fileName="Login" ,item_id ,item_name ,item_num;
    String ID;
    String[][] list;
    int a;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private TestSensorListener mSensorListener;

    Context context = MapsActivity.this;
    int missionResult=0;//任務結果,0為失敗1為成功
    CountDownTimer time;
    String ex=new String();//運算式
    int correct=0;//運算式解答
    double sensor_num;
    String number;
    double numberdo;
    double check = 0;
    int mission_times=0;
    private static final double EARTH_RADIUS = 6378.137;

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

    public void getlist(String[][] list,int a){
        Log.i("list", String.valueOf(list));
        Log.i("a", String.valueOf(a));
        this.list = list;
        this.a = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        baggage =(ImageButton)findViewById(R.id.baggage);
        baggage.setOnClickListener(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        game_state();

        mSensorListener = new TestSensorListener();
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //發放第一次任務
        new CountDownTimer(10000,1000){
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //隨機挑選任務
                int missionNum = (int) (Math.random() * 3 + 1);
                //missionNum=2;
                //彈出視窗
                AlertDialog(missionNum);
            }
        }.start();

        //發放第二次任務
        new CountDownTimer(2500000,1000){
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //隨機挑選任務
                int missionNum = (int) (Math.random() * 3 + 1);
                //missionNum=2;
                //彈出視窗
                AlertDialog(missionNum);
            }
        }.start();

        //發放第三次任務
        new CountDownTimer(4800000,1000){
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //隨機挑選任務
                int missionNum = (int) (Math.random() * 3 + 1);
                //missionNum=2;
                //彈出視窗
                AlertDialog(missionNum);
            }
        }.start();

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
        game_state_DB();
    }

    public void game_state_DB(){

        Thread game_stateDB = new Thread(new Runnable() {
            @Override
            public void run() {

                game_state();
            }
        });
        game_stateDB.start();
        try {
            game_stateDB.join();
            game_start();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
            Log.i("game_state_DB","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }

    //check game statement start or not
    public void game_state(){
        try{
            String room = DBconnect.executeQuery("SELECT room_id FROM enter_room WHERE User_id = '"+ID+"' ");
            JSONObject room_id = new JSONArray(room).getJSONObject(0);
            String Room_id=(String)room_id.getString("room_id").toString();
            Log.i("XXXXX",Room_id);

            //String result = DBconnect.executeQuery("INSERT INTO map (User_id,map_id,game_state) VALUES ('"+ID+"','"+Room_id+"','1')");
            //測試讓他先不是鬼
            String result = DBconnect.executeQuery("INSERT INTO map (User_id,map_id,game_state,ghost) VALUES ('"+ID+"','"+Room_id+"','1','0')");
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
            Log.i("game_finish","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
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
    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;
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

            //計算距離

            if (Latitude2 == 0 && Longitude2 == 0) {
                Latitude2 = Latitude;
                Longitude2 = Longitude;
            } else {
                totaldis += D_jw(Latitude, Longitude, Latitude2, Longitude2);
                Latitude2 = Latitude;
                Longitude2 = Longitude;
            }


            MapItem MapItem = new MapItem(Longitude ,Latitude,ID);
            MapItem.map_update();

        }
    }
    //標誌全部玩家的位置
    public void MakePoint(String Ghost){
        Log.i("MAKEPOINT","MAKEPOINT");
        Log.i("list", String.valueOf(list));
        String test123 = list[0][1];
        String test456 = list[0][2];
        Log.i("test123", list[0][1]);
        Log.i("test456", list[0][2]);
        mMap.clear();
//        不是鬼的人的畫面
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
        }else if(Ghost=="1"){
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
        Log.i("XXXXXXXX",ID);
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
                String item_num_check = DBconnect.executeQuery("SELECT item_num FROM playeritem WHERE User_id='"+ID+"' AND item_id ='"+item_index+"'");
                    JSONObject item_num = new JSONArray(item_num_check).getJSONObject(0);
                    String Item_num=(String)item_num.getString("item_num").toString();
                    int num = parseInt(Item_num);
                    int one = '1';
                    int end = num - one;
                    String result2 = DBconnect.executeQuery("UPDATE playeritem SET item_num = "+end+" WHERE User_id = '"+ID+"' AND item_id ='"+item_index+"' ");
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

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.baggage:
                item_list.clear();
                MapItem MapItem = new MapItem(0, 0,ID);
                item_all = MapItem.item_set();
                System.out.println("item_all:"+item_all);
                try{
                    for(int i= 0 ; i<item_all.length(); i++){
                        JSONObject jsonObject = item_all.getJSONObject(i);
                        item_id = jsonObject.getString("item_id");
                        item_num = jsonObject.getString("item_num");
                        Map2Item Map2Item = new Map2Item(item_id);
                        item2_all = Map2Item.item_set();
                        JSONObject jsonObject2 = item2_all.getJSONObject(i);
                        item_name = jsonObject2.getString("item_name");
                        item_list.add(item_name + "   X " + item_num);
                    }
                    System.out.println("item_list:" + item_list);
                } catch (JSONException e) {
                    System.out.println("道具抓取出錯!!");
                }

                String[] array = new String[item_list.size()];
                for (int i = 0; i < item_list.size(); i++) {
                    array[i] = item_list.get(i);
                }
                AlertDialog.Builder dialog_list = new AlertDialog.Builder(this);
                dialog_list.setTitle("利用List呈現");
                dialog_list.setItems(array, new DialogInterface.OnClickListener(){
                    @Override
                    //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//                Toast.makeText(this, "你選的是" + dinner[which], Toast.LENGTH_SHORT).show();
                        //int a = '1';
                        //UseItem(a);
                        UseItem(which);
                    }
                });
                dialog_list.show();
                break;

        }
    }

    public void AlertDialog(final int missionNum) {
        String missionStr[] = {"30秒內跑完100公尺","30秒內跳完25下開合跳","10秒內解出運算式解答"};
        ImageView iv = new ImageView(context);
        iv.setImageResource(R.drawable.gift);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("自由任務")
                .setMessage("\n\n\n" + missionStr[missionNum-1] + "\n\n\n\n完成可獲得道具獎勵!\n\n\n")
                .setView(iv)
                .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //呼叫mission alert
                        dialog.dismiss();
                        mission(missionNum);
                    }
                });
        builder.setNegativeButton("拒絕", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        // 取得 AlertDialog Message, 然後設定對齊位置為水平置中
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER_HORIZONTAL);
        messageText.setTextSize(20);
        messageText.setTextColor(Color.DKGRAY);
        //設定title
        TextView tvTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(Color.BLUE);
        //設定按鈕
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextSize(18);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(18);
    }


    public void mission(final int missionNum){

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        final EditText editText=new EditText(context);

        //產生算數
        char[] operator=new char[]{'+','-','*','/'};
        Random random=new Random();

        //產生運算式
        int n=2;//兩個運算元
        int[] number=new int[n+1];

        String[] symbol=new String[n];
        for(int j=0;j<=n;j++){
            number[j]=random.nextInt(10)+1;
        }
        for(int j=0;j<n;j++){
            int s=random.nextInt(4);
            ex+=String.valueOf(number[j])+String.valueOf(operator[s]);
            if(s==3){number[j+1]=decide(number[j],number[j+1]);}
            symbol[j]= String.valueOf(operator[s]);
        }
        ex+=String.valueOf(number[n]);

        //運算答案
        switch (symbol[0]){
            case "+":
                switch (symbol[1]){
                    case "+":
                        correct=number[0]+number[1]+number[2];
                        break;
                    case "-":
                        correct=number[0]+number[1]-number[2];
                        break;
                    case "*":
                        correct=number[0]+number[1]*number[2];
                        break;
                    case "/":
                        correct=number[0]+number[1]/number[2];
                        break;
                }
                break;
            case "-":
                switch (symbol[1]){
                    case "+":
                        correct=number[0]-number[1]+number[2];
                        break;
                    case "-":
                        correct=number[0]-number[1]-number[2];
                        break;
                    case "*":
                        correct=number[0]-number[1]*number[2];
                        break;
                    case "/":
                        correct=number[0]-number[1]/number[2];
                        break;
                }
                break;
            case "*":
                switch (symbol[1]){
                    case "+":
                        correct=number[0]*number[1]+number[2];
                        break;
                    case "-":
                        correct=number[0]*number[1]-number[2];
                        break;
                    case "*":
                        correct=number[0]*number[1]*number[2];
                        break;
                    case "/":
                        correct=number[0]*number[1]/number[2];
                        break;
                }
                break;
            case "/":
                switch (symbol[1]){
                    case "+":
                        correct=number[0]/number[1]+number[2];
                        break;
                    case "-":
                        correct=number[0]/number[1]-number[2];
                        break;
                    case "*":
                        correct=number[0]/number[1]*number[2];
                        break;
                    case "/":
                        correct=number[0]/number[1]/number[2];
                        break;
                }
                break;
        }

        //呼叫任務alert
        if(missionNum == 1){
            builder.setMessage("000");
        }else if(missionNum == 2){
            builder.setMessage("000");
        }else if(missionNum == 3) {
            builder.setMessage("000")
                    .setView(editText)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //判斷答案對錯寫在這,如果對就missionResult=1
                            String culResult=editText.getText().toString();
                            Log.i("culresult",culResult);
                            if (parseInt(String.valueOf(culResult)) == correct) {
                                missionResult = 1;
                                time.cancel();
                                result(missionResult);
                            } else {
                                missionResult = 0;
                                time.cancel();
                                result(missionResult);
                            }

                        }
                    });

        }

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        // 取得 AlertDialog Message, 然後設定對齊位置為水平置中
        final TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER_HORIZONTAL);
        messageText.setTextSize(25);
        messageText.setTextColor(Color.DKGRAY);
        //設定title
        final TextView tvTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(Color.BLUE);
        //設定按鈕
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextSize(18);




        //計時器倒數
        int missionTime[]={31000,31000,11000};
        time =new CountDownTimer(missionTime[missionNum-1], 1000) {
            public void onTick(long millisUntilFinished) {
                switch (missionNum){
                    case 1:
                        messageText.setText("\n\n"+millisUntilFinished / 1000+"秒內跑完100公尺\n\n目前已完成"+(int)totaldis+"公尺\n\n");
                        if((int)totaldis>=100){
                            missionResult=1;
                            result(missionResult);
                            Latitude2=0;
                            Longitude2=0;
                            time.cancel();
                        }
                        break;
                    case 2:
                        call_z();
                        messageText.setText("\n\n"+millisUntilFinished / 1000+"秒內跳完25下開合跳\n\n目前已完成"+mission_times+"下\n\n");
                        //任務內容
                        //相Z值設定為正到負之間(負值-正值 手臂放下到舉過肩膀)
                        if(numberdo<0&&check==0){
                            check=1;
                        }else if(numberdo>0) {
                            if (check == 1) {
                                mission_times++;
                                check = 0;
                            }
                        }
                        if(mission_times>=25){
                            missionResult=1;
                            result(missionResult);
                            time.cancel();
                        }

                        break;
                    case 3:
                        messageText.setText("\n\n"+millisUntilFinished / 1000+"秒內解出答案\n\n\n"+ex+"\n\n");
                        break;
                }
            }
            public void onFinish() {
                messageText.setText("\n\n\n時間到！\n\n\n");
                missionResult=1;
                result(missionResult);
            }
        }.start();
    }

    public void result(int missionResult){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        ImageView iv=new ImageView(context);
        iv.setImageResource(R.drawable.crying);
        int item=(int) (Math.random() * 3 + 1);//隨機道具
        String itemText[]={"FakeGPS","影分身之術","隱形斗篷"};

        ImageView itemPic=new ImageView(context);//道具圖片
        switch (item){
            case 1:
                itemPic.setImageResource(R.drawable.fakegps);
                itemPic.setMaxHeight(10);
                break;
            case 2:
                itemPic.setImageResource(R.drawable.shadow);
                itemPic.setMaxHeight(10);
                break;
            case 3:
                itemPic.setImageResource(R.drawable.invisible);
                itemPic.setMaxHeight(10);
                break;
        }


        if(missionResult == 0){
            builder.setMessage("任務失敗")
                    .setView(iv);

        }else if(missionResult == 1){
            builder.setMessage("任務成功\n\n獲得  "+itemText[item-1])
                    .setView(itemPic);
        }

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        // 取得 AlertDialog Message, 然後設定對齊位置為水平置中
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER_HORIZONTAL);
        messageText.setTextSize(30);
        messageText.setTextColor(Color.DKGRAY);
    }
    private static int decide(int x,int y){
        Random random=new Random();
        if(x%y!=0){
            y=random.nextInt(100)+1;
            return decide(x,y);
        }
        else{
            return y;
        }
    }

    @Override protected void onResume() {
        super.onResume(); // 註冊感測器監聽函式
        mSensorManager.registerListener(mSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override protected void onPause() { super.onPause();
        // 登出監聽函式
        mSensorManager.unregisterListener(mSensorListener);

    }

//    private void initViews() {
//        mSensorInfoA = (TextView) findViewById(R.id.sensor_info_a);
//    }

    class TestSensorListener implements SensorEventListener {

        @Override

        public void onSensorChanged(SensorEvent event) {
            // 讀取加速度感測器數值,values陣列0,1,2分別對應x,y,z軸的加速度
            Log.i("onSensorChanged", "onSensorChanged: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
            sensor_num = event.values[2];
        }
        @Override

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i("onAccuracyChanged", "onAccuracyChanged");
        }
    }

    public void call_z(){
        Log.i("call_z", "call function call");
        numberdo = sensor_num;
    }

    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
    public static double D_jw(double wd1,double jd1,double wd2,double jd2)
    {
        double x,y,out;
        double PI=3.14159265;
        double R=6.371229*1e6;

        x=(jd2-jd1)*PI*R*Math.cos( ((wd1+wd2)/2) *PI/180)/180;
        y=(wd2-wd1)*PI*R/180;
        out=Math.hypot(x,y);
        return out;
    }

}

