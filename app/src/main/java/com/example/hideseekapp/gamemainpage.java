package com.example.hideseekapp;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class gamemainpage extends AppCompatActivity implements View.OnClickListener {
        Button chooseroom;
        ImageButton personal ,setting ,bag ,shop;
        String FileName ="Login" ,ID;

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
                intent.setClass(gamemainpage.this, shoppage.class);
                startActivity(intent);
                break;
            case R.id.shop:
                intent.setClass(gamemainpage.this, seekroom.class);
                startActivity(intent);
                break;
            case R.id.chooseroom:
                intent.setClass(gamemainpage.this, seekroom.class);
                startActivity(intent);
                break;
        }
    }
}