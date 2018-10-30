package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;


public class settingpage extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingpage);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(settingpage.this,
                R.array.lunch,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(lunchList);
        Button button = (Button)findViewById(R.id.gamemainpage);
        Button button01 = (Button)findViewById(R.id.gamemainpage2);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(settingpage.this, gamemainpage.class);
                startActivity(intent);
                settingpage.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(settingpage.this, gamemainpage.class);
                startActivity(intent);
                settingpage.this.finish();
            }
        });

    }

}