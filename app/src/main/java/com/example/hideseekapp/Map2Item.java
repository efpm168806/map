package com.example.hideseekapp;

import android.util.Log;

import org.json.JSONArray;

public class Map2Item {
    String item_id;
    JSONArray jsonArray;

    public Map2Item(String item_id){
        this.item_id =item_id;
    }

    public JSONArray item_set(){
        Thread item_get = new Thread(new Runnable() {
            @Override
            public void run() {
                item_get();
            }
        });
        item_get.start();
        try {
            item_get.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
        return jsonArray;
    }

    public JSONArray item_get(){
        try {
            String result = DBconnect.executeQuery("SELECT * FROM item WHERE item_id <= '3'");
            JSONArray jsonArray_get = new JSONArray(result);
            System.out.println("SELECT * FROM item WHERE item_id <= '3'");
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
