package com.example.use.map;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class registerActivity extends Activity implements View.OnClickListener {

    String id,pwd,sex;
    ConstraintLayout layout;
    EditText register_id,register_pwd;
    RadioGroup register_sex;
    RadioButton man,female;
    TextView textView;
    String fileName="login";
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_id = (EditText) findViewById(R.id.register_id);
        register_pwd = (EditText) findViewById(R.id.register_pwd);
//        sign_sex = (RadioGroup)findViewById(R.id.signup_sex);
//        man = (RadioButton)findViewById(R.id.man);
//        female = (RadioButton)findViewById(R.id.female);
        textView =(TextView)findViewById(R.id.textView);
        layout =(ConstraintLayout) findViewById(R.id.layout);
        register =(Button)findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.register:
                id = register_id.getText().toString();
                pwd = register_pwd.getText().toString();
                System.out.println("帳號:"+id+"密碼:"+pwd);
                registerItem registerItem = new registerItem(id,pwd);
                registerItem.register();

            break;
        }
    }
}
