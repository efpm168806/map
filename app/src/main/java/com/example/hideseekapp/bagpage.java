package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
public class bagpage extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bagpage);
        Button button = (Button) findViewById(R.id.gamemainpage);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(bagpage.this, gamemainpage.class);
                startActivity(intent);
                bagpage.this.finish();
            }
        });

    }


}