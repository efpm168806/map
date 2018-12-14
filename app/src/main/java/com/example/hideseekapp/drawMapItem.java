package com.example.hideseekapp;

public class drawMapItem {
    double latitude ,longitude;

    public drawMapItem(double  latitude, double longitude){
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
        String result = DBconnect.executeQuery("INSERT INTO maprange (longitude ,latitude) VALUES ('"+longitude+"' ,'"+latitude+"')");

    }
}
