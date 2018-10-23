package com.example.use.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomActivity extends Activity implements View.OnClickListener {

    ConstraintLayout ParentLayout;
    TextView Room,Roomchange;
    Button Open;
    LinearLayout layout;
    JSONArray Room_all; //房間全部資料
    int Room_id;
    String Room_name;
    RoomItem RoomItem = new RoomItem();
    ArrayList<TextView> add = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);
        layout =(LinearLayout)findViewById(R.id.Layout);
        Room =(TextView)findViewById(R.id.Room);
        Open =(Button)findViewById(R.id.Open);
        ParentLayout =(ConstraintLayout)findViewById(R.id.ParentLayout);
        Open.setOnClickListener(this);
        Room.setOnClickListener(this);

        try{
            Room_all = RoomItem.room_download();
            System.out.println("房間的jsonArray:"+Room_all);
            for (int i = 0; i < Room_all.length(); i++) {
                JSONObject jsonObject = Room_all.getJSONObject(i);
                Room_id = jsonObject.getInt("room_id");
                Room_name = jsonObject.getString("room_name");
                System.out.println(Room_id);
                LayoutInflater inflater  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View Open = inflater.inflate(R.layout.newroom,null);
                TextView AddRoom = Open.findViewById(R.id.Room);
                layout.addView(Open, layout.getChildCount()-1);
                add.add(AddRoom);
                System.out.println("測試:"+AddRoom);
                AddRoom.setText(Integer.toString(Room_id));

            }
        }catch (JSONException e){
            System.out.println("JSONE出錯!!");
        }
    }


    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        System.out.println(view.getId());
        System.out.println(R.id.Open);
        switch (view.getId()){
            case R.id.Room:
                intent.setClass(RoomActivity.this , Room2Activity.class);
                startActivity(intent);
                break;
            case R.id.Open:
                LayoutInflater inflater  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View Open = inflater.inflate(R.layout.newroom,null);
                TextView AddRoom = Open.findViewById(R.id.Room);
                layout.addView(Open, layout.getChildCount()-1);
                add.add(AddRoom);
                RoomItem.room_update();
                intent.setClass(RoomActivity.this , Room2Activity.class);
                startActivity(intent);
                break;


        }

    }
}
