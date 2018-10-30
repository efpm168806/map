package com.example.hideseekapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registerpage2 extends Activity implements View.OnClickListener {

    Button back ,agreepage;
    EditText User_name ,User_height ,User_weight;
    String id ,pwd ,email ,name;
    int height ,weight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage2);
        back = (Button) findViewById(R.id.back);
        agreepage = (Button) findViewById(R.id.agreepage);
        User_name = (EditText)findViewById(R.id.personname);
        User_height = (EditText)findViewById(R.id.height);
        User_weight = (EditText)findViewById(R.id.weight);
        back.setOnClickListener(this);
        agreepage.setOnClickListener(this);



    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.back:
                intent.setClass(registerpage2.this, registerpage1.class);
                startActivity(intent);
                break;

            case R.id.agreepage:
                Intent intent_get = getIntent();
                id = intent_get.getStringExtra("UserID");
                pwd = intent_get.getStringExtra("UserPWD");
                email =intent_get.getStringExtra("UserEMAIL");
                name = User_name.getText().toString();
                height = Integer.parseInt(User_height.getText().toString());
                weight = Integer.parseInt(User_weight.getText().toString()) ;
                System.out.println("上傳:"+id+pwd+email+name+height+weight);
                registerItem registerItem = new registerItem(id, pwd ,email ,name ,height ,weight);
                registerItem.register();
                intent.setClass(registerpage2.this, agreepage.class);
                startActivity(intent);
                break;
        }
    }
}