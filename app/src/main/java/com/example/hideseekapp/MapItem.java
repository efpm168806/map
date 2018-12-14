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

    public MapItem(double longitude, double latitude,String ID){
        this.longitude = longitude;
        this.latitude = latitude;
        this.ID = ID;
        Log.i("FUCK2",latitude+"++++++"+longitude);
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
        Log.i("FUCK3",latitude+"++++++"+longitude);
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
    public void makepoint_middle_stay(String Ghost,String[][]list2,int a2){
        MapsActivity MapsActivity = new MapsActivity();
        MapsActivity.getlist(list2,a2,Ghost);
        Log.i("呼叫getlist && MAKEPOINT","XXXXXXXXXXXXXXXX");
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

            String ghost = DBconnect.executeQuery("SELECT ghost FROM map WHERE User_id = '"+ID+"' ");
            JSONObject ghost2 = new JSONArray(ghost).getJSONObject(0);
            String Ghost=(String)ghost2.getString("ghost").toString();

            if(Ghost.equals("0")){
                String ghost_info = DBconnect.executeQuery("SELECT User_id,longitude,latitude FROM map WHERE ghost = '1' AND map_id = '"+MapID+"'  ");
                String my_info = DBconnect.executeQuery("SELECT longitude,latitude,ghost_catch FROM map WHERE User_id = '"+ID+"' ");

                JSONArray ghostArray = new JSONArray(ghost_info);
                JSONArray myArray = new JSONArray(my_info);

                int g = ghostArray.length();
                int s = myArray.length();

                String[][] g_list = new String[g][3];
                for(int i=0;i<g;i++){
                    JSONObject jsonData = ghostArray.getJSONObject(i);
                    g_list[i][0]=(String)jsonData.getString("User_id").toString();
                    g_list[i][1]=(String)jsonData.getString("longitude").toString();
                    g_list[i][2]=(String)jsonData.getString("latitude").toString();
                    System.out.println("User_id:"+g_list[i][0]+"。longitude:"+g_list[i][1]+"。latitude:"+g_list[i][2]);
                }

                String[][] s_list = new String[s][3];
                for(int i=0;i<s;i++){
                    JSONObject jsonData = myArray.getJSONObject(i);
                    s_list[i][0]=(String)jsonData.getString("longitude").toString();
                    s_list[i][1]=(String)jsonData.getString("latitude").toString();
                    s_list[i][2]=(String)jsonData.getString("ghost_catch").toString();
                    System.out.println("longitude:"+s_list[i][0]+"。latitude:"+s_list[i][1]+"。ghost_catch:"+s_list[i][2]);
                }

                for(int i=0;i<g;i++){
                    double ghostLat = rad(Double.parseDouble(g_list[i][2]));
                    double myLat = rad(Double.parseDouble(s_list[0][1]));
                    double a = ghostLat - myLat;
                    double ghostLongt = rad(Double.parseDouble(g_list[i][1]));
                    double myLongt = rad(Double.parseDouble(s_list[0][0]));
                    double b = ghostLongt - myLongt;

                    double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(ghostLat)*Math.cos(myLat)*Math.pow(Math.sin(b/2),2)));
                    distance = distance * EARTH_RADIUS;
                    distance = Math.round(distance * 10000) / 10000;
                    Log.i("Distance", String.valueOf(distance));

                    //距離小於3KM時資料庫中ghost_catch會+1，ghost_catch大於3時則代表玩家被抓到了
                    if(distance<=3){
                        if(s_list[0][2].equals("2")){
                            String result = DBconnect.executeQuery("UPDATE map SET ghost = '1' WHERE User_Id ='"+ID+"'");
                        }else if(s_list[0][2].equals("1")){
                            String result = DBconnect.executeQuery("UPDATE map SET ghost_catch = '2' WHERE User_Id ='"+ID+"'");
                        }else{
                            String result = DBconnect.executeQuery("UPDATE map SET ghost_catch = '1' WHERE User_Id ='"+ID+"'");
                        }
                    }else{
                        //超出被鬼抓的範圍，ghost_catch就為0
                        String result = DBconnect.executeQuery("UPDATE map SET ghost_catch = '0' WHERE User_Id ='"+ID+"'");
                    }
                }
            }else{
                //當鬼沒事
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
