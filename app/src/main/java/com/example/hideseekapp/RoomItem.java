package com.example.hideseekapp;

import android.util.Log;

import com.example.hideseekapp.DBconnect;

import org.json.JSONArray;

public class RoomItem {
    JSONArray jsonArray;
    private int room_id;
    private String user_id;

    public RoomItem(int room_id , String user_id){
        this.room_id = room_id;
        this.user_id = user_id;
    }

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

    public void enter_room() {
        Thread enter_roomDB = new Thread(new Runnable() {
            @Override
            public void run() {
                enter_roomDB();
            }
        });
        enter_roomDB.start();
        try {
            enter_roomDB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }

    public void enter_room_chief() {
        Thread enter_roomDB_chief = new Thread(new Runnable() {
            @Override
            public void run() {
                enter_roomDB_chief();
            }
        });
        enter_roomDB_chief.start();
        try {
            enter_roomDB_chief.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
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

    private  void enter_roomDB(){
        String result = DBconnect.executeQuery("INSERT INTO enter_room (room_id ,user_id) VALUES ('"+room_id+"' ,'"+user_id+"')");

    }

    private  void enter_roomDB_chief(){
        String result = DBconnect.executeQuery("INSERT INTO enter_room (room_id ,user_id ,room_chief) VALUES ('"+room_id+"' ,'"+user_id+"' ,1)");
    }

}