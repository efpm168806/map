package com.example.use.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.use.map.LoginItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends Activity implements View.OnClickListener {

    ConstraintLayout layout;
    EditText user_id,user_pwd;
    String id,pwd;
    String fileName="Login";
    TextView textView;
    Button Login,Sign,Room;

    FileInputStream inputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        user_id = (EditText) findViewById(R.id.user_id);
        user_pwd = (EditText) findViewById(R.id.user_pwd);
        layout =(ConstraintLayout) findViewById(R.id.layout);
        textView =(TextView)findViewById(R.id.textView);
        Login =(Button)findViewById(R.id.Login);
        Sign =(Button)findViewById(R.id.Sign);
        Room =(Button)findViewById(R.id.Room);
        layout.setOnClickListener(this);
        Login.setOnClickListener(this);
        Sign.setOnClickListener(this);
        Room.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.Login:
                id = user_id.getText().toString();
                pwd = user_pwd.getText().toString();
                LoginItem LoginItem = new LoginItem(id,pwd);
                String user = LoginItem.login();
                if (user != "wrong") {
                    try {       //將id 儲存
                        String data = String.valueOf(user);
                        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        outputStream.write(data.getBytes());
                        outputStream.close();

                        outputStream = openFileOutput("id", Context.MODE_PRIVATE);     //存帳號
                        outputStream.write(id.getBytes());
                        outputStream.close();

                        outputStream = openFileOutput("pwd", Context.MODE_PRIVATE);          //存密碼
                        outputStream.write(pwd.getBytes());

                        outputStream.close();
                        textView.setText("登入成功!!");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    textView.setText("帳號密碼有誤!!");
                    //帳密輸入失敗

                }

            case R.id.Sign:
                intent.setClass(LoginActivity.this , registerActivity.class);
                startActivity(intent);

                break;


            case R.id.Room:
                intent.setClass(LoginActivity.this , RoomActivity.class);
                startActivity(intent);
        }
    }
}