package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class agreepage extends AppCompatActivity {
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreepage);
        Button button = (Button)findViewById(R.id.register2);
        Button button02 = (Button)findViewById(R.id.activity_main);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(agreepage.this, registerpage2.class);
                startActivity(intent);
                agreepage.this.finish();
            }
        });
        button02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(agreepage.this, MainActivity.class);
                startActivity(intent);
                agreepage.this.finish();
            }
        });
    }
}