package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
public class shoppage extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppage);
        Button button = (Button) findViewById(R.id.gamemainpage);
        Button button01 = (Button) findViewById(R.id.shoppage2);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(shoppage.this, gamemainpage.class);
                startActivity(intent);
                shoppage.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(shoppage.this, shoppage2.class);
                startActivity(intent);
                shoppage.this.finish();
            }
        });
    }
}