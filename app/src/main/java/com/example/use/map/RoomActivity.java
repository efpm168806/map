package com.example.use.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
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

public class RoomActivity extends Activity implements View.OnClickListener {
    Handler handler = new Handler();
    ConstraintLayout ParentLayout;
    TextView Room;
    Button Open;
    LinearLayout layout;
    JSONArray Room_all; //房間全部資料
    int Room_id;
    String FileName = "Login" ,ID ,Room_name;
    ArrayList<TextView> add = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_room);
        layout =(LinearLayout)findViewById(R.id.Layout);
        Room =(TextView)findViewById(R.id.Room);
        Open =(Button)findViewById(R.id.Open);
        ParentLayout =(ConstraintLayout)findViewById(R.id.ParentLayout);
        Open.setOnClickListener(this);
        Room.setOnClickListener(this);

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
                    TextView AddRoom = Open.findViewById(R.id.Room);
                    layout.addView(Open, layout.getChildCount() - 1);
                    add.add(AddRoom);
                    System.out.println("AddRoom資訊:" + AddRoom);
                    AddRoom.setText("房號:" + Integer.toString(Room_id) + "\n" + Room_name);
                    AddRoom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String room_click = Room.getText().toString();
                            System.out.println(room_click);
                            RoomItem RoomItem = new RoomItem(Room_id, ID);
                            Intent intent = new Intent();
                            RoomItem.enter_room();

                            intent.setClass(RoomActivity.this, Room2Activity.class);
                            System.out.println("按下的Room_id:"+Room_id);
                            intent.putExtra("RoomID",Integer.toString(Room_id));
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

        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                onCreate(null);
                System.out.println("重整!!");
            }}, 3000);
    }

    @Override
    public void onResume(){
        super.onResume();
        onCreate(null);
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        RoomItem RoomItem = new RoomItem((Room_id+1),ID);
        switch (view.getId()){
            case R.id.Open:
                LayoutInflater inflater  = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View Open = inflater.inflate(R.layout.newroom,null);
                TextView AddRoom = Open.findViewById(R.id.Room);
                layout.addView(Open, layout.getChildCount()-1);
                add.add(AddRoom);
                RoomItem.room_update();
                RoomItem.enter_room();
                intent.setClass(RoomActivity.this , Room2Activity.class);
                System.out.println("open_roomid:"+(Room_id+1));
                intent.putExtra("RoomID",Integer.toString(Room_id));
                intent.putExtra("UserID",ID);
                startActivity(intent);
                break;


        }

    }


}
