package com.example.hideseekapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    boolean stop = false;
    TextView player;
    String FileName = "Login",ID ,user_id ,RoomID ,player_id;
    LinearLayout Layout;
    Button back,start;
    JSONArray player_all;
    ArrayList<String> player_list = new ArrayList<>();
    ArrayList<TextView> player_list2 = new ArrayList<>();
    String sexStr;
    ImageView playerImg;
    private Runnable run =new Runnable(){
        @Override
        public void run() {
            onCreate(null);
        }
    };

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
        back = (Button)findViewById(R.id.back);
        start = (Button)findViewById(R.id.start);
        back.setOnClickListener(this);
        start.setOnClickListener(this);

        Intent intent = getIntent();
        RoomID = intent.getStringExtra("RoomID");
        System.out.println("RoomID:" + RoomID);
        System.out.println("UserID:" + ID);
        Room2Item Room2Item = new Room2Item(Integer.parseInt(RoomID) ,ID);
        player_all = Room2Item.player_set();
        if(player_all != null) {
            try {
                for (int i = 0; i < player_all.length(); i++) {
                    JSONObject jsonObject = player_all.getJSONObject(i);
                    player_id = jsonObject.getString("User_id");
                    player_list.add(player_id);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View inroom = inflater.inflate(R.layout.newplayer, null);
                    TextView AddPlayer = inroom.findViewById(R.id.Player);
                    playerImg = inroom.findViewById(R.id.playerImg);
                    Layout.addView(inroom, Layout.getChildCount() - 1);
                    player_list2.add(AddPlayer);
                    AddPlayer.setText("\n玩家 : " + player_id);
                    AddPlayer.setTextSize(21);
                    AddPlayer.setTextColor(Color.BLACK);
                    setPlayerImg();
                }
                System.out.println("player_data:" + player_list);
            } catch (JSONException e) {
                System.out.println("玩家抓取出錯!!");
            }
        }

        player_list.clear();
        player_list2.clear();
        if (!stop) {
            handler.postDelayed(run, 3000);
            System.out.println("重整房間!");
        }else{
            handler.removeCallbacks(run);
            System.out.println("停止重整房間!");
        }
    }

    @Override
    public void onClick(View view){
        stop =true;
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.back:
                Room2Item Room2Item = new Room2Item(Integer.parseInt(RoomID),ID);
                JSONArray select;
                select =Room2Item.room_delete();
                Room2Item.player_delete();
                if (select!=null){
                    Room2Item.room_delete2();
                }
                intent.setClass(inroom.this, seekroom.class);
                startActivity(intent);
                break;
            case R.id.start:
                intent.setClass(inroom.this, MapsActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void setPlayerImg() {

        Thread setImg=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String Sex = DBconnect.executeQuery("SELECT User_sex FROM user WHERE User_id = '"+ID+"' ");
                    JSONObject sexjson = new JSONArray(Sex).getJSONObject(0);
                    sexStr= sexjson.getString("User_sex");
                    Log.i("sex",sexStr);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
        setImg.start();
        try {
            setImg.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String male="男",female="女";
        if(sexStr.equals(male)){
            playerImg.setImageResource(R.drawable.boy);
        }else if(sexStr.equals(female)){
            playerImg.setImageResource(R.drawable.girl);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }
}