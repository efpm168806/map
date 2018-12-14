package com.example.hideseekapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapItem {
    double latitude ,longitude;
    String ID;
    String Ghost_stay = null;
    String[][]list;
    String[][]list2;
    int a;
    int a2;
    JSONArray jsonArray;
    private static final double EARTH_RADIUS = 6378.137;

    public MapItem(double  latitude, double longitude,String ID){
        this.longitude = longitude;
        this.latitude = latitude;
        this.ID = ID;
    }

    public void map_update() {
        Log.i("map_update","go to map_update function");
        Thread map_DB = new Thread(new Runnable() {
            @Override
            public void run() {
                mapDB();
            }
        });
        map_DB.start();
        try {
            map_DB.join();
            //抓取其他玩家的位置以及是否使用道具
            location_get();
            //判斷是否有進入鬼的抓人範圍
            ghostcatch();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
            Log.i("map_update","XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }

    private void mapDB(){
        String result = DBconnect.executeQuery("UPDATE map SET longitude = '" + longitude + "',latitude = '" + latitude + "' WHERE User_Id ='"+ID+"'");
    }


    public void location_get() {
        Thread location_DB = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDB();
            }
        });
        location_DB.start();
        try {
            location_DB.join();
            makepoint_middle_stay(Ghost_stay,list2,a2);
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
            Log.i("location_get","XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }
    //取得其他玩家的位置
    public void locationDB(){
        try{
            String map_index = DBconnect.executeQuery("SELECT map_id FROM map WHERE User_id = '"+ID+"' ");
            JSONObject mapID = new JSONArray(map_index).getJSONObject(0);
            String MapID=(String)mapID.getString("map_id").toString();

            String room_player_info = DBconnect.executeQuery("SELECT User_id,longitude,latitude,item,ghost FROM map WHERE map_id = '"+MapID+"' AND User_id != '"+ID+"' ");

            JSONArray jsonArray = new JSONArray(room_player_info);
            int a = jsonArray.length();
            list = new String [a][5];
            for(int i=0 ; i<a ; i++){
                JSONObject jsonData = jsonArray.getJSONObject(i);
                list[i][0]=(String)jsonData.getString("User_id").toString();
                list[i][1]=(String)jsonData.getString("longitude").toString();
                list[i][2]=(String)jsonData.getString("latitude").toString();
                list[i][3]=(String)jsonData.getString("item").toString();
                list[i][4]=(String)jsonData.getString("ghost").toString();
                System.out.println("User_id:"+list[i][0]+"。longitude:"+list[i][1]+"。latitude:"+list[i][2]+"。item:"+list[i][3]+"。ghost:"+list[i][4]);
            }

            String ghost = DBconnect.executeQuery("SELECT ghost FROM map WHERE User_id = '"+ID+"' ");
            JSONObject ghost2 = new JSONArray(ghost).getJSONObject(0);
            String Ghost=(String)ghost2.getString("ghost").toString();

            Log.i("XXXX",Ghost);


            //將同個房間內玩家的資訊(ID、經緯度、道具)放入list陣列並傳回MapsActivity來做標點
            Ghost_stay = null;
            Ghost_stay = Ghost;

            list2 = list;
            a2 = jsonArray.length();
            Log.i("XXXX", String.valueOf(list2));
            Log.i("a2", String.valueOf(a2));
        }
        catch(JSONException e){
            Log.i("locationDB","XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }
    //將同個房間內玩家的資訊(ID、經緯度、道具)放入list陣列並傳回MapsActivity來做標點
    public void makepoint_middle_stay(String Ghost,String[][]list2,int a2) {
        MapsActivity MapsActivity = new MapsActivity();
        MapsActivity.getlist(list2, a2);
        Log.i("呼叫getlist", "XXXXXXXXXXXXXXXX");
        MapsActivity.MakePoint(Ghost);
        Log.i("呼叫MAKEPOINT", "XXXXXXXXXXXXXXXX");
    }


    public void ghostcatch(){
        Thread ghostcatchDB = new Thread(new Runnable() {
            @Override
            public void run() {
                ghostcatch_DB();
            }
        });
        ghostcatchDB.start();
        try {
            ghostcatchDB.join();
        } catch (InterruptedException e) {
            Log.i("ghostcatch","XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }
    //鬼抓人判斷
    public void ghostcatch_DB(){
        try{
            String map_index = DBconnect.executeQuery("SELECT map_id FROM map WHERE User_id = '"+ID+"' ");
            JSONObject mapID = new JSONArray(map_index).getJSONObject(0);
            String MapID=(String)mapID.getString("map_id").toString();

            String ghost_info = DBconnect.executeQuery("SELECT User_id,longitude,latitude FROM map WHERE ghost = '1' AND map_id = '"+MapID+"'  ");
            String survivor_info = DBconnect.executeQuery("SELECT User_id,longitude,latitude FROM map WHERE ghost != '1' AND map_id = '"+MapID+"' ");

            JSONArray ghostArray = new JSONArray(ghost_info);
            JSONArray survivorArray = new JSONArray(survivor_info);

            int g = ghostArray.length();
            int s = survivorArray.length();

            String[][] g_list = new String[g][3];
            for(int i=0;i<g;i++){
                JSONObject jsonData = ghostArray.getJSONObject(i);
                g_list[i][0]=(String)jsonData.getString("User_id").toString();
                g_list[i][1]=(String)jsonData.getString("longitude").toString();
                g_list[i][2]=(String)jsonData.getString("latitude").toString();
            }
            String[][] s_list = new String[s][4];
            for(int i=0;i<s;i++){
                JSONObject jsonData = survivorArray.getJSONObject(i);
                s_list[i][0]=(String)jsonData.getString("User_id").toString();
                s_list[i][1]=(String)jsonData.getString("longitude").toString();
                s_list[i][2]=(String)jsonData.getString("latitude").toString();
                s_list[i][3]=(String)jsonData.getString("ghost_catch").toString();
            }

            for(int i=0;i<g;i++){
                for(int j=0;j<s;j++){
                    double ghostLat = rad(Double.parseDouble(g_list[i][2]));
                    double survivorLat = rad(Double.parseDouble(s_list[j][2]));
                    double a = ghostLat - survivorLat;
                    double ghostLongt = rad(Double.parseDouble(g_list[i][1]));
                    double survivorLongt = rad(Double.parseDouble(s_list[j][1]));
                    double b = ghostLongt - survivorLongt;

                    double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(ghostLat)*Math.cos(survivorLat)*Math.pow(Math.sin(b/2),2)));
                    distance = distance * EARTH_RADIUS;
                    distance = Math.round(distance * 10000) / 10000;

                    //距離小於3KM時資料庫中ghost_catch會+1，ghost_catch大於3時則代表玩家被抓到了
                    if(distance<=3){
                        if(s_list[j][3]=="2"){
                            String result = DBconnect.executeQuery("UPDATE map SET ghost = '1' WHERE User_Id ='"+s_list[j][0]+"'");
                        }else if(s_list[j][3]=="1"){
                            String result = DBconnect.executeQuery("UPDATE map SET ghost_catch = '2' WHERE User_Id ='"+s_list[j][0]+"'");
                        }else{
                            String result = DBconnect.executeQuery("UPDATE map SET ghost_catch = '1' WHERE User_Id ='"+s_list[j][0]+"'");
                        }
                    }else{
                        //沒事
                    }
                }
            }
        }
        catch(JSONException e){
            Log.i("ghostcatch_DB","XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }

    //抓人判斷要使用之公式
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
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

    private JSONArray item_get() {  //道具放置
        try {
            String result = DBconnect.executeQuery("SELECT * FROM playeritem WHERE User_id = '"+ID+"'");
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
