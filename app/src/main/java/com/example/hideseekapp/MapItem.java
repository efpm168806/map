package com.example.hideseekapp;

public class MapItem {
    double latitude ,longitude;

    public MapItem(double  latitude, double longitude){
        this.longitude = longitude;
        this.latitude = latitude;

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
        } catch (InterruptedException e) {
            System.out.println("執行序被中斷");
        }
    }

    private  void mapDB(){
        String result = DBconnect.executeQuery("INSERT INTO map (longitude ,latitude) VALUES ('"+longitude+"' ,'"+latitude+"')");

    }
}
