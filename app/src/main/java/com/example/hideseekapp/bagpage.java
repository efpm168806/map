package com.example.hideseekapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

public class bagpage extends AppCompatActivity {
    private Button cheek;
    String fileName = "Login", ID;
    JSONArray jsonArray;
    String item1, item2, item3;
    TextView invisible_txt, shadow_txt, fakegps_txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bagpage);
        Button button = (Button) findViewById(R.id.gamemainpage);
        invisible_txt = (TextView) findViewById(R.id.invisible_txt);
        shadow_txt = (TextView) findViewById(R.id.shadow_txt);
        fakegps_txt = (TextView) findViewById(R.id.fakegps_txt);

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(bagpage.this, gamemainpage.class);
                startActivity(intent);
                bagpage.this.finish();
            }
        });

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

        setBagItem();
    }

    private void setBagItem(){
        Thread setItem = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String Item1 = DBconnect.executeQuery("SELECT item_num FROM playeritem WHERE User_id = '"+ID+"' AND item_id = 1");
                    JSONObject item1json = new JSONArray(Item1).getJSONObject(0);
                    item1= item1json.getString("item_num");
                    fakegps_txt.setText("FakeGPS\n數量:"+item1);

                    String Item2 = DBconnect.executeQuery("SELECT item_num FROM playeritem WHERE User_id = '"+ID+"' AND item_id = 2");
                    JSONObject item2json = new JSONArray(Item2).getJSONObject(0);
                    item2= item2json.getString("item_num");
                    shadow_txt.setText("多重影分身\n數量:"+item2);

                    String Item3 = DBconnect.executeQuery("SELECT item_num FROM playeritem WHERE User_id = '"+ID+"' AND item_id = 3");
                    JSONObject item3json = new JSONArray(Item1).getJSONObject(0);
                    item3= item3json.getString("item_num");
                    invisible_txt.setText("隱身術\n數量:"+item3);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        setItem.start();
    }

}