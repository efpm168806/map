package com.example.hideseekapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapItem {
    double latitude ,longitude;
    String ID;
    String[][]list;
    String[][]list2;
    int a;
    JSONArray jsonArray;
    private static final double EARTH_RADIUS = 6378.137;

    public MapItem(double  latitude, double longitude,String ID){
        this.longitude = longitude;
        this.latitude = latitude;
        this.ID = ID;
    }

    public void map_update() {
        Thread mapDB = new Thread(new Runnable() {
            @Override
            public void run() {
                mapDB();
            }
        });
        mapDB.start();
        try {
            mapDB.join();
            //抓取其他玩家的位置以及是否使用道具
            location_get();
            //判斷是否有進入鬼的抓人範圍
            ghostcatch();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }

    private void mapDB(){
        try{
            String check = DBconnect.executeQuery("SELECT longitude,latitude FROM map WHERE User_id = '"+ID+"'  ");
            JSONObject check_res = new JSONArray(check).getJSONObject(0);
            String Check_res=(String)check_res.getString("room_id").toString();
            String longitude_check=(String)check_res.getString("longitude").toString();
            String latitude_check=(String)check_res.getString("latitude").toString();

            String room = DBconnect.executeQuery("SELECT room_id FROM enter_room WHERE User_id = '"+ID+"'  ");
            JSONObject room_id = new JSONArray(room).getJSONObject(0);
            String Room_id=(String)room_id.getString("room_id").toString();

            if(Check_res == null ||Check_res == ""){
                String result = DBconnect.executeQuery("INSERT INTO map (map_id,User_id,longitude,latitude) VALUE ('"+Room_id+"','"+ID+"','"+longitude+"','"+latitude+"')");
            }else{
                String result = DBconnect.executeQuery("UPDATE map SET longitude = '" + longitude + "',latitude = '" + latitude + "' WHERE User_Id ='"+ID+"'");
            }
        }
        catch (JSONException e){
            System.out.println("mapDB connect failed");
        }
    }


    public void location_get() {
        Thread locationDB = new Thread(new Runnable() {
            @Override
            public void run() {
                locationDB();
            }
        });
        locationDB.start();
        try {
            locationDB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
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

            //將同個房間內玩家的資訊(ID、經緯度、道具)放入list陣列並傳回MapsActivity來做標點
            MapsActivity MapsActivity = new MapsActivity();
            MapsActivity.MakePoint(Ghost);
            MapsActivity.getlist(MapsActivityList(list),MapsActivityA(a));
        }
        catch(JSONException e){
            System.out.println("location_get connect failed");
        }
    }

    public String[][] MapsActivityList(String[][] list2) {
        this.list2 = list2;
        return list2;
    }

    public int MapsActivityA(int a) {
        this.a = a;
        return a;
    }

    public void ghostcatch(){
        Thread ghostcatch_DB = new Thread(new Runnable() {
            @Override
            public void run() {
                ghostcatch_DB();
            }
        });
        ghostcatch_DB.start();
        try {
            ghostcatch_DB.join();
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
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

            String[][] g_list = new String[g][2];
            for(int i=0;i<g;i++){
                JSONObject jsonData = ghostArray.getJSONObject(i);
                g_list[i][0]=(String)jsonData.getString("User_id").toString();
                g_list[i][1]=(String)jsonData.getString("longitude").toString();
                g_list[i][2]=(String)jsonData.getString("latitude").toString();
            }
            String[][] s_list = new String[s][3];
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
            System.out.println("ghostcatch_DB");
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
