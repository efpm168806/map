package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ingame extends AppCompatActivity {
    private Button cheek;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingame);
        Button button = (Button)findViewById(R.id.finishpage);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(ingame.this, finishpage.class);
                startActivity(intent);
                ingame.this.finish();
            }
        });
    }
}
