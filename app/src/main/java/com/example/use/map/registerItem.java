package com.example.use.map;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class registerItem {
    int jsonArrayLength = 0;
    JSONArray jsonArray;
    private String id;
    private String pwd;
    private String sex;
    private String email;

        public registerItem(String id , String pwd){
            this.id = id;
            this.pwd = pwd;
        }

        private JSONArray registerSELECT(){  //確認帳密
            try{
                String result = DBconnect.executeQuery("SELECT * FROM user WHERE User_id='"+id+"' ");
                System.out.println(result);
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
            String result = DBconnect.executeQuery("INSERT INTO user (User_id, User_pwd) VALUES ('"+id+"', '"+pwd+"')");
            System.out.println("INSERT INTO user (User_id, User_pwd) VALUES ('"+id+"', '"+pwd+"')");
            System.out.println("connect ok");

        } catch (Exception e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

    }
}

