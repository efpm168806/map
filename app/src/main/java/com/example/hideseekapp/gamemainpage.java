package com.example.hideseekapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class gamemainpage extends AppCompatActivity implements View.OnClickListener {
//    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//     dialog.setTitle("基本訊息對話按鈕");
//     dialog.setMessage("基本訊息對話功能介紹");
//     dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
////        @Override
////        public void onClick(DialogInterface arg0, int arg1) {
////            // TODO Auto-generated method stub
////            Toast.makeText(this, "我還尚未了解",Toast.LENGTH_SHORT).show();
////        }
//
//    });
        Button chooseroom;
        ImageButton personal ,setting ,bag ,shop;
        String FileName ="Login" ,ID ,sexStr;

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
        setContentView(R.layout.gamemainpage);
        personal = (ImageButton)findViewById(R.id.personal);
        setting = (ImageButton)findViewById(R.id.setting);
        bag = (ImageButton)findViewById(R.id.bag);
        shop = (ImageButton)findViewById(R.id.shop);
        chooseroom = (Button)findViewById(R.id.chooseroom);
        personal.setOnClickListener(this);
        setting.setOnClickListener(this);
        bag.setOnClickListener(this);
        shop.setOnClickListener(this);
        chooseroom.setOnClickListener(this);
        setPlayerImg();

        Button draw;
        draw =(Button)findViewById(R.id.draw);
        draw.setOnClickListener(this);

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
            personal.setImageResource(R.drawable.boy);
        }else if(sexStr.equals(female)){
            personal.setImageResource(R.drawable.girl);
        }
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.personal:
                intent.setClass(gamemainpage.this, personalpage.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent.setClass(gamemainpage.this, settingpage.class);
                startActivity(intent);
                break;
            case R.id.bag:
                intent.setClass(gamemainpage.this, bagpage.class);
                startActivity(intent);
                break;
            case R.id.shop:
                intent.setClass(gamemainpage.this, shoppage.class);
                startActivity(intent);
                break;
            case R.id.chooseroom:
                intent.setClass(gamemainpage.this, seekroom.class);
                startActivity(intent);
                break;
            case R.id.draw:
                intent.setClass(gamemainpage.this, MapsActivity2.class);
                startActivity(intent);
                break;
        }
    }
}