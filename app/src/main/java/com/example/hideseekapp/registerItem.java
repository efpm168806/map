package com.example.hideseekapp;

import android.util.Log;
import com.example.hideseekapp.DBconnect;
import org.json.JSONArray;

public class registerItem {
    int jsonArrayLength = 0;
    JSONArray jsonArray;
    private String id ,pwd ,email ,name;
    private int height ,weight;


    public registerItem(String id ,String pwd ,String email ,String name ,int height ,int weight){
        this.id = id;
        this.pwd = pwd;
        this.email = email;
        this.name =name;
        this.height = height;
        this.weight = weight;
    }

    private JSONArray registerSELECT(){  //確認帳密
        try{
            String result = DBconnect.executeQuery("SELECT * FROM user WHERE User_id='"+id+"' ");
            System.out.println("確認帳密："+result);
            if (result == null){

            }else{
                jsonArray = new JSONArray(result);
                jsonArrayLength =jsonArray.length();
            }
            System.out.println("jsonArrayLength"+jsonArrayLength);
            System.out.println("connect ok");

        } catch (Exception e) {
            jsonArrayLength = 0;
            Log.e("log_tag", e.toString());
            System.out.println("尚未註冊過此帳號!!");
        }

        return jsonArray;
    }

    public boolean register(){
        Thread registerDB = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = registerSELECT();
            }
        });
        registerDB.start();
        try{
            registerDB.join();
        }catch ( InterruptedException e){
            System.out.println("執行序被中斷");
        }

        if (jsonArrayLength >0){
            return false;
        }else {
            Thread saveDB = new Thread(new Runnable() {
                public void run() {
                    registersaveDB();
                }
            });

            saveDB.start();
            try {
                saveDB.join();
            } catch (InterruptedException e) {
                System.out.println("執行緒被中斷");

            }
            return true;
        }
    }

    private void registersaveDB(){  //註冊帳密
        try{
            //1216
            String result2 = DBconnect.executeQuery("INSERT INTO playeritem (item_id,user_id,item_num) VALUES ('3','"+id+"','0')");
            String result3 = DBconnect.executeQuery("INSERT INTO playeritem (item_id,user_id,item_num) VALUES ('2','"+id+"','0')");
            String result4 = DBconnect.executeQuery("INSERT INTO playeritem (item_id,user_id,item_num) VALUES ('1','"+id+"','0')");

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

    }
}
