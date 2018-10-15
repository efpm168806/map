package com.example.use.map;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class LoginItem {
    int jsonArrayLength = 0;
    JSONArray jsonArray;
    private String id;
    private String pwd;

    public LoginItem(String id , String pwd){
        this.id = id;
        this.pwd = pwd;
    }


    private JSONArray LoginSELECT(){  //確認帳密
        try{
            String result = DBconnect.executeQuery("SELECT * FROM user WHERE User_id='"+id+"' and User_pwd='"+pwd+"' ");
            System.out.println("SELECT * FROM user WHERE User_id='"+id+"' and User_pwd='"+pwd+"' ");
            jsonArray =new JSONArray(result);

            jsonArrayLength = jsonArray.length();
            System.out.println("jsonArray.length()"+jsonArray.length());
            System.out.println("connect ok"+result);

        } catch (JSONException e) {
            Log.e("log_tag", e.toString());
            System.out.println("connect failed");
        }

        return jsonArray;
    }

    public String login(){
        Thread LoginDB = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = LoginSELECT();
            }
        });
        LoginDB.start();
        try{
            LoginDB.join();
        }catch ( InterruptedException e){
            System.out.println("執行序被中斷");
        }

        if (jsonArrayLength == 1){
            JSONObject jsonData = null;

            try{

                jsonData = jsonArray.getJSONObject(0);
                String user_id = jsonData.getString("User_id");
                return user_id;
            }catch (JSONException e){
                e.printStackTrace();

                return "wrong";
            }
        }else{
            return "wrong";
        }
    }
}
