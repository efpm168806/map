package com.example.use.map;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomItem {
    JSONArray jsonArray;

    public void room_update() {
        Thread room_updateDB = new Thread(new Runnable() {
            @Override
            public void run() {
                room_updateDB();
            }
        });
        room_updateDB.start();
        try {
            room_updateDB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }

    public  JSONArray room_download(){
        Thread room_downloadDB = new Thread(new Runnable() {
            @Override
            public void run() {
                room_downloadDB();
            }
        });
        room_downloadDB.start();
        try{
            room_downloadDB.join();
        }catch(InterruptedException e){
            System.out.println("執行序被中斷");
        }
        return jsonArray;
    }

    private void room_updateDB() {  //註冊房間
        try {
            String result = DBconnect.executeQuery("INSERT INTO room (room_name) VALUES ('快來加入!!')");
            System.out.println("INSERT INTO room (room_name) VALUES ('快來加入!!')");
            System.out.println("connect ok");

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }
    }

    private JSONArray room_downloadDB() {  //抓取房間
        try {
            String result = DBconnect.executeQuery("SELECT * FROM room");
            JSONArray jsonArray_get = new JSONArray(result);
            System.out.println("connect ok");
            jsonArray =jsonArray_get;
            return jsonArray;
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }
        return jsonArray;
    }
}