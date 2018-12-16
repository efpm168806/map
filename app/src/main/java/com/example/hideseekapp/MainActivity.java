package com.example.hideseekapp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

    static MediaPlayer mPlayer;
    TextView textView;
    EditText User_id ,User_pwd;
    Button register, forget_pwd, login;
    String fileName="Login",ID ,id ,pwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{    //讀取id
            FileInputStream inputStream = openFileInput(fileName);
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
        setContentView(R.layout.activity_main);
        register = (Button) findViewById(R.id.register);
        forget_pwd = (Button) findViewById(R.id.forget_pwd);
        login = (Button) findViewById(R.id.login);
        User_id =(EditText)findViewById(R.id.id);
        User_pwd =(EditText)findViewById(R.id.pwd);
        textView =(TextView)findViewById(R.id.textView);
        register.setOnClickListener(this);
        forget_pwd.setOnClickListener(this);
        login.setOnClickListener(this);
        if (ID != null){
            User_id.setText(ID);
        }
        if(finishpage.mPlayer != null) {
            finishpage.mPlayer.stop();
        }
        musicPlay();
    }
    public void musicPlay() {
        try
        {
            mPlayer = MediaPlayer.create(this, R.raw.background);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setLooping(true);
            mPlayer.start();
            Log.i("music","");
            //重複播放
        }catch (IllegalStateException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("musicerror",e.toString());

        }
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.login:
                id = User_id.getText().toString();
                pwd = User_pwd.getText().toString();
                System.out.println("帳密:"+id+pwd);
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
                        textView.setTextColor(Color.GREEN);
                        intent.setClass(MainActivity.this, gamemainpage.class);
                        startActivity(intent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    textView.setText("帳號密碼有誤!!");
                    textView.setTextColor(Color.RED);
                    //帳密輸入失敗

                }
                break;
            case R.id.forget_pwd:
                intent.setClass(MainActivity.this, forgetpwd.class);
                startActivity(intent);
                break;
            case R.id.register:
                intent.setClass(MainActivity.this, registerpage1.class);
                startActivity(intent);
                break;
        }
    }
}
