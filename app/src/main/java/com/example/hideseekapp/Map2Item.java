package com.example.hideseekapp;

import android.util.Log;

import org.json.JSONArray;

public class Map2Item {
    String item_id;
    JSONArray jsonArray;

    public Map2Item(String item_id){
        this.item_id =item_id;
    }

    public JSONArray item_name(String item_id){
        try {
            String result = DBconnect.executeQuery("SELECT item_name FROM item WHERE item_id = '"+item_id+"'");
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

    public JSONArray item_num(String item_id){
        try {
            String result = DBconnect.executeQuery("SELECT item_name FROM playeritem WHERE item_id = '"+item_id+"'");
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
