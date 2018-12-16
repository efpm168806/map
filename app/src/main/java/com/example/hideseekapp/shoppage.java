package com.example.hideseekapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class shoppage extends AppCompatActivity implements View.OnClickListener {
    private Button cheek;
    String FileName="Login" ,ID;
    JSONArray Judgment;
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
        setContentView(R.layout.shoppage);
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        ImageButton buyInvisible = (ImageButton)findViewById(R.id.buyInvisible);
        ImageButton buyShadow = (ImageButton)findViewById(R.id.buyShadow);
        ImageButton buyfakeGPS = (ImageButton)findViewById(R.id.buyfakeGPS);
        buyInvisible.setOnClickListener(this);
        buyShadow.setOnClickListener(this);
        buyfakeGPS.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            //1216
            case R.id.back:
                intent.setClass(shoppage.this, gamemainpage.class);
                startActivity(intent);
                break;
            case R.id.buyInvisible:
                shopItem shopItem =new shopItem(3 ,ID);
                Judgment = shopItem.item_Judgment();
                System.out.println(Judgment);
                if(Judgment!=null){
                    shopItem.item_update2();
                    System.out.println("有道具!!");
                }else {
                    shopItem.item_update();
                    System.out.println("沒道具!!");
                }
                break;
            case R.id.buyShadow:
                shopItem shopItem2 =new shopItem(2 ,ID);
                Judgment = shopItem2.item_Judgment();
                if(Judgment!=null){
                    shopItem2.item_update2();
                    System.out.println("有道具!!");
                }else {
                    shopItem2.item_update();
                    System.out.println("沒道具!!");
                }
                break;
            case R.id.buyfakeGPS:
                shopItem shopItem3 =new shopItem(1 ,ID);
                Judgment = shopItem3.item_Judgment();
                if(Judgment!=null){
                    shopItem3.item_update2();
                    System.out.println("有道具!!");
                }else {
                    shopItem3.item_update();
                    System.out.println("沒道具!!");
                }
                break;
        }
    }
}