package com.example.hideseekapp;

import android.util.Log;

import org.json.JSONArray;

public class shopItem {
    String ID;
    int item_id;
    JSONArray jsonArray;
    public shopItem(int item_id , String ID){
        this.ID = ID;
        this.item_id = item_id;
    }

    public JSONArray item_Judgment(){
        Thread item_select = new Thread(new Runnable() {
            @Override
            public void run() {
                item_select();
            }
        });
        item_select.start();
        try {
            item_select.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
        return jsonArray;
    }
    public void item_update() {
        Thread item_updateDB = new Thread(new Runnable() {
            @Override
            public void run() {
                item_buy();
            }
        });
        item_updateDB.start();
        try {
            item_updateDB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }
    public void item_update2() {
        Thread item_updateDB2 = new Thread(new Runnable() {
            @Override
            public void run() {
                item_buy2();
            }
        });
        item_updateDB2.start();
        try {
            item_updateDB2.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }
    private  JSONArray item_select(){ //判斷是否購買過道具
        try {
            String result = DBconnect.executeQuery("SELECT * FROM playeritem WHERE item_id = '"+item_id+"' AND user_id ='"+ID+"' ");
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
    private  void item_buy(){ //購買道具(尚未擁有的情況下)
        String result = DBconnect.executeQuery("INSERT INTO playeritem (user_id ,item_id ,item_num) VALUES ('"+ID+"' ,'"+item_id+"' ,'1')");

    }
    private  void item_buy2(){ //購買道具(擁有的情況下)
        String result = DBconnect.executeQuery("UPDATE  playeritem SET item_num=item_num+1 WHERE item_id ='"+item_id+"' ");

    }
}
