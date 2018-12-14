package com.example.hideseekapp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;

public class Room2Item {

    JSONArray jsonArray;
    int RoomID;
    String ID;

    public  Room2Item(int Roomid , String id){

        this.RoomID = Roomid;
        this.ID =id;
    }


    public void player_delete(){
        Thread player_delete = new Thread(new Runnable() {
            @Override
            public void run() {
                enter_delete();
            }
        });
        player_delete.start();
        try{
            player_delete.join();
        }catch (InterruptedException e){
            System.out.println("執行序被中斷");
        }
    }

    public JSONArray room_delete(){ //確認房間有無人
        Thread room_delete = new Thread(new Runnable() {
            @Override
            public void run() {
                room_select();
            }
        });
        room_delete.start();
        try{
            room_delete.join();
        }catch (InterruptedException e){
            System.out.println("執行序被中斷");
        }
        return jsonArray;
    }
    public void room_delete2(){ //房間無人 刪除房間
        Thread room_delete2 = new Thread(new Runnable() {
            @Override
            public void run() {
                room_det();
            }
        });
        room_delete2.start();
        try{
            room_delete2.join();
        }catch (InterruptedException e){
            System.out.println("執行序被中斷");
        }
    }

    public JSONArray player_set(){
        Thread player_get = new Thread(new Runnable() {
            @Override
            public void run() {
                player_get();
            }
        });
        player_get.start();
        try {
            player_get.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
        return jsonArray;
    }

    private JSONArray player_get() {  //玩家放置
        try {
            String result = DBconnect.executeQuery("SELECT * FROM enter_room WHERE room_id ='"+RoomID+"' ");
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

    private JSONArray enter_delete(){  //退出房間
        try{
            String result = DBconnect.executeQuery("DELETE  FROM enter_room WHERE User_id='"+ID+"' ");
            System.out.println("DELETE  FROM enter_room WHERE User_id='"+ID+"' ");
            JSONArray  jsonArray_delete =new JSONArray(result);
            System.out.println("connect ok");
            jsonArray =jsonArray_delete;
            return jsonArray;
        } catch (JSONException e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

        return jsonArray;
    }

    private JSONArray room_select(){  //查看房間是否有人
        try{
            String result = DBconnect.executeQuery("SELECT * FROM enter_room WHERE room_id ='"+RoomID+"' ");
            System.out.println("SELECT * FROM enter_room WHERE room_id ='"+RoomID+"' ");
            JSONArray  jsonArray_insert =new JSONArray(result);
            System.out.println("connect ok");
            jsonArray =jsonArray_insert;
            return jsonArray;
        } catch (JSONException e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

        return jsonArray;
    }

    private void room_det(){  //若無人，刪除房間
        try{
            String result = DBconnect.executeQuery("DELETE * FROM room WHERE room_id ='"+RoomID+"' ");
            System.out.println("DELETE * FROM room WHERE room_id ='"+RoomID+"' ");
            JSONArray  jsonArray_delete =new JSONArray(result);
            System.out.println("connect ok");
            jsonArray =jsonArray_delete;
        } catch (JSONException e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

    }
}
