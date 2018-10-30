package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class gamemainpage extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemainpage);
        ImageButton button = (ImageButton)findViewById(R.id.personalbutton);
        ImageButton button01 = (ImageButton)findViewById(R.id.settingbutton);
        ImageButton button02 = (ImageButton)findViewById(R.id.bagbuttom);
        ImageButton button03 = (ImageButton)findViewById(R.id.shopbutton);
        Button button04 = (Button)findViewById(R.id.chooseroom);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(gamemainpage.this, personalpage.class);
                startActivity(intent);
                gamemainpage.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(gamemainpage.this, settingpage.class);
                startActivity(intent);
                gamemainpage.this.finish();
            }
        });
        button02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(gamemainpage.this, bagpage.class);
                startActivity(intent);
                gamemainpage.this.finish();
            }
        });
        button03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(gamemainpage.this, shoppage.class);
                startActivity(intent);
                gamemainpage.this.finish();
            }
        });
        button04.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(gamemainpage.this, seekroom.class);
                startActivity(intent);
                gamemainpage.this.finish();
            }
        });
    }

}