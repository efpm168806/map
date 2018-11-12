package com.example.hideseekapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Timer;

public class seekroom extends Activity implements View.OnClickListener {
    static Handler handler = new Handler();
    Button creatroom ,back;
    boolean stop = false;
    TextView Room ,AddRoom = null;
    LinearLayout layout;
    String FileName="Login" ,ID ,Room_name ,room_detail ,roomClick;
    JSONArray Room_all; //房間全部資料
    int Room_id;
    ArrayList<TextView> add = new ArrayList<>();


    Runnable run =new Runnable(){
        @Override
        public void run() {
            onCreate(null);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{    //讀取id
            FileInputStream inputStream = openFileInput(FileName);
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
        setContentView(R.layout.seekroom);
        layout =(LinearLayout)findViewById(R.id.layout);
        creatroom = (Button)findViewById(R.id.creatroom);
        back = (Button)findViewById(R.id.back);
        Room =(TextView)findViewById(R.id.Room);
        creatroom.setOnClickListener(this);
        back.setOnClickListener(this);

        try{
            RoomItem RoomItem = new RoomItem(Room_id,ID);
            Room_all = RoomItem.room_download();
            System.out.println("房間的jsonArray:"+Room_all);
            if (Room_all != null) {
                for (int i = 0; i < Room_all.length(); i++) {
                    JSONObject jsonObject = Room_all.getJSONObject(i);
                    Room_id = jsonObject.getInt("room_id");
                    Room_name = jsonObject.getString("room_name");
                    System.out.println(Room_id);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View Open = inflater.inflate(R.layout.newroom, null);
                    AddRoom = Open.findViewById(R.id.Room);
                    layout.addView(Open, layout.getChildCount() - 1);
                    add.add(AddRoom);
                    System.out.println("AddRoom資訊:" + AddRoom);
                    AddRoom.setText("房號:" + Integer.toString(Room_id) + "\n" + Room_name);
                    final int room_click = Room_id;

                    AddRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            stop = true;
                            System.out.println("room_click:"+roomClick);
                            RoomItem RoomItem = new RoomItem(room_click, ID);
                            Intent intent = new Intent();
                            RoomItem.enter_room();
                            intent.setClass(seekroom.this, inroom.class);
                            intent.putExtra("RoomID",Integer.toString(room_click));
                            intent.putExtra("UserID",ID);
                            startActivity(intent);
                        }
                    });
                }
            }else{
                Room.setText("目前無房間，請開創新房間!!");
            }
        }catch (JSONException e){

            System.out.println("房間出錯!!");
        }

        add.clear();
        if (!stop) {
            handler.postDelayed(run, 3000);
            System.out.println("重整房間列表!");
        }else{
            handler.removeCallbacks(run);
            System.out.println("停止重整房間列表!");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        onCreate(null);
    }

    @Override
    public void onClick(View view){
        stop = true;
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.creatroom:
                RoomItem RoomItem = new RoomItem((Room_id+1),ID);
                RoomItem.room_update();
                RoomItem.enter_room_chief();
                intent.setClass(seekroom.this, inroom.class);
                intent.putExtra("RoomID",Integer.toString((Room_id+1)));
                startActivity(intent);
                break;
            case  R.id.back:
                intent.setClass(seekroom.this, gamemainpage.class);
                startActivity(intent);
                break;
        }
    }
}