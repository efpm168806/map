package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.DecimalFormat;

public class personalpage extends AppCompatActivity {
    String ID;
    String fileName="Login" ,item_id ,item_name ,item_num;
    double height,weight,bmi;
    TextView heightTxt;
    TextView weightTxt;
    TextView bmiTxt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalpage);
        Button button = (Button)findViewById(R.id.activity_main);
        Button button01 = (Button)findViewById(R.id.changepassword);
        heightTxt=(TextView)findViewById(R.id.height);
        weightTxt=(TextView)findViewById(R.id.weight);
        bmiTxt=(TextView)findViewById(R.id.bmi);

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

        getHeight();
        getWeight();
        setBmi();

        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(personalpage.this, gamemainpage.class);
                startActivity(intent);
                personalpage.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(personalpage.this, passwordchange.class);
                startActivity(intent);
                personalpage.this.finish();
            }
        });


    }
    public void getHeight(){

        Thread heightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String heightStr="";
                    String Height = DBconnect.executeQuery("SELECT User_height FROM user WHERE User_id = '"+ID+"' ");
                    JSONObject hei = new JSONArray(Height).getJSONObject(0);
                    heightStr= hei.getString("User_height");
                    heightTxt.setText(heightStr);
                    height=Integer.parseInt(heightStr);

                }
                catch (JSONException e){
                    e.printStackTrace();
                    Log.e("heighterr",e.toString());
                }
            }
        });
        heightThread.start();
        try {
            heightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void getWeight(){

        Thread weightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String weightStr="";
                    String Weight = DBconnect.executeQuery("SELECT User_weight FROM user WHERE User_id = '"+ID+"' ");
                    JSONObject wei = new JSONArray(Weight).getJSONObject(0);
                    weightStr= wei.getString("User_weight");
                    weightTxt.setText(weightStr);
                    Log.i("weight",weightStr);
                    weight=Integer.parseInt(weightStr);
                }
                catch (JSONException e){
                    e.printStackTrace();
                    Log.e("weighterr",e.toString());
                }
            }
        });
        weightThread.start();
        try {
            weightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setBmi() {
        bmi=weight/((height/100)*(height/100));
        DecimalFormat df = new DecimalFormat("##.00");
        bmi = Double.parseDouble(df.format(bmi));
        bmiTxt.setText(String.valueOf(bmi));
        Log.i("bmi",""+bmi);
    }
}