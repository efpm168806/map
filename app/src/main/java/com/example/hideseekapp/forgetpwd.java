package com.example.hideseekapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class forgetpwd extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpwd);
        Button button = (Button)findViewById(R.id.activity_main);
        Button button01 = (Button)findViewById(R.id.return_activity_main);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(forgetpwd.this, MainActivity.class);
                startActivity(intent);
                forgetpwd.this.finish();
            }
        });
        button01.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(forgetpwd.this, MainActivity.class);
                startActivity(intent);
                forgetpwd.this.finish();
            }
        });

    }
}