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
        Button next = (Button) findViewById(R.id.next);
        TextView item1 = (TextView)findViewById(R.id.item1);
        TextView item2 = (TextView)findViewById(R.id.item2);
        TextView item3 = (TextView)findViewById(R.id.item3);
        TextView item4 = (TextView)findViewById(R.id.item4);
        back.setOnClickListener(this);
        next.setOnClickListener(this);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        item4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.next:
                intent.setClass(shoppage.this, shoppage2.class);
                break;
            case R.id.back:
                intent.setClass(shoppage.this, gamemainpage.class);
                startActivity(intent);
                break;
            case R.id.item1:
                shopItem shopItem =new shopItem(1 ,ID);
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
            case R.id.item2:
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
            case R.id.item3:
                shopItem shopItem3 =new shopItem(3 ,ID);
                Judgment = shopItem3.item_Judgment();
                if(Judgment!=null){
                    shopItem3.item_update();
                    System.out.println("有道具!!");
                }else {
                    shopItem3.item_update();
                    System.out.println("沒道具!!");
                }
                break;
            case R.id.item4:
                shopItem shopItem4 =new shopItem(4 ,ID);
                Judgment = shopItem4.item_Judgment();
                if(Judgment!=null){
                    shopItem4.item_update2();
                    System.out.println("有道具!!");
                }else {
                    shopItem4.item_update();
                    System.out.println("沒道具!!");
                }
                break;
        }
    }
}