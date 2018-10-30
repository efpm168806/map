package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
public class shoppage2 extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppage2);
        Button button = (Button) findViewById(R.id.gamemainpage);
        Button button01 = (Button) findViewById(R.id.shoppage);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(shoppage2.this, gamemainpage.class);
                startActivity(intent);
                shoppage2.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(shoppage2.this, shoppage.class);
                startActivity(intent);
                shoppage2.this.finish();
            }
        });
    }
}