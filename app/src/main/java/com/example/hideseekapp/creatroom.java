package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class creatroom extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatroom);
        Button button = (Button)findViewById(R.id.seekroom);
        Button button02 = (Button)findViewById(R.id.inroom);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(creatroom.this,
                R.array.roomnumber,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(lunchList);

        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> lunchList2 = ArrayAdapter.createFromResource(creatroom.this,
                R.array.location,
                android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(lunchList2);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(creatroom.this,seekroom.class);
                startActivity(intent);
                creatroom.this.finish();
            }
        });
        button02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(creatroom.this,inroom.class);
                startActivity(intent);
                creatroom.this.finish();
            }
        });

    }
}
