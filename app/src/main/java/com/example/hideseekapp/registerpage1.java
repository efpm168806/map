package com.example.hideseekapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class registerpage1 extends Activity implements View.OnClickListener {
    Button back, register;
    EditText User_id ,User_pwd ,User_email;
    String FileName = "Login" ,ID ,id ,pwd ,email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{    //讀取id
            FileInputStream inputStream = openFileInput(FileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while((len = inputStream.read(bytes))!=-1){
                baos.write(bytes,0,len);
            }
            baos.flush();
            byte[] result = baos.toByteArray();
            ID = new String(result);
            System.out.println("id:"+ID);
            baos.close();
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.registerpage1);
        back = (Button) findViewById(R.id.back);
        register = (Button) findViewById(R.id.register);
        User_id = (EditText)findViewById(R.id.id);
        User_pwd = (EditText)findViewById(R.id.pwd);
        User_email =(EditText)findViewById(R.id.email);
        back.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.back:
                intent.setClass(registerpage1.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                id = User_id.getText().toString();
                pwd = User_pwd.getText().toString();
                email = User_email.getText().toString();
                System.out.println("帳號:"+id+"，密碼:"+pwd+"，信箱:"+email);
                intent.putExtra("UserID",id);
                intent.putExtra("UserPWD",pwd);
                intent.putExtra("UserEMAIL",email);
                intent.setClass(registerpage1.this, registerpage2.class);
                startActivity(intent);
                break;
        }
    }
}