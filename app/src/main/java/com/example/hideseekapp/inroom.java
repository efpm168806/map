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

public class inroom extends Activity implements View.OnClickListener {
    Handler handler = new Handler();
    TextView player;
    String FileName = "Login",ID ,user_id ,RoomID ,player_id;
    LinearLayout Layout;
    Button back,start;
    JSONArray player_all;
    ArrayList<String> player_list = new ArrayList<>();
    ArrayList<TextView> player_list2 = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {    //讀取id
            FileInputStream inputStream = openFileInput(FileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            baos.flush();
            byte[] result = baos.toByteArray();
            ID = new String(result);
            System.out.println("id:" + ID);
            baos.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.inroom);
        Layout =(LinearLayout)findViewById(R.id.Layout);
        back = (Button)findViewById(R.id.seekroom);
        start = (Button)findViewById(R.id.start);

        Intent intent = getIntent();
        RoomID = intent.getStringExtra("RoomID");
        System.out.println("RoomID:" + RoomID);
        System.out.println("UserID:" + ID);
        Room2Item Room2Item = new Room2Item(Integer.parseInt(RoomID) ,ID);
        player_all = Room2Item.player_set();
        System.out.println("player_all:"+player_all);
        if(player_all != null) {
            try {
                for (int i = 0; i < player_all.length(); i++) {
                    JSONObject jsonObject = player_all.getJSONObject(i);
                    player_id = jsonObject.getString("User_id");
                    player_list.add(player_id);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inroom = inflater.inflate(R.layout.newplayer, null);
                    TextView AddPlayer = inroom.findViewById(R.id.Player);
                    Layout.addView(inroom, Layout.getChildCount() - 1);
                    player_list2.add(AddPlayer);
                    AddPlayer.setText("玩家:" + player_id);
                }

                System.out.println("player_data:" + player_list);


            } catch (JSONException e) {
                System.out.println("玩家抓取出錯!!");
            }
        }

        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                onCreate(null);
                System.out.println("重整!!");
            }}, 3000);

        player_list.clear();
        player_list2.clear();
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.back:
                intent.setClass(inroom.this, seekroom.class);
                startActivity(intent);
                break;
            case R.id.start:
                intent.setClass(inroom.this, ingame.class);
                startActivity(intent);
                break;
        }
    }
}