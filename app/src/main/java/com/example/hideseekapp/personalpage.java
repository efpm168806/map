package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class personalpage extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalpage);
        Button button = (Button)findViewById(R.id.activity_main);
        Button button01 = (Button)findViewById(R.id.changepassword);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(personalpage.this, gamemainpage.class);
                startActivity(intent);
                personalpage.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(personalpage.this, passwordchange.class);
                startActivity(intent);
                personalpage.this.finish();
            }
        });


    }
}