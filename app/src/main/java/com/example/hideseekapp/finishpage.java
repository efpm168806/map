package com.example.hideseekapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class finishpage extends AppCompatActivity {
    static MediaPlayer mPlayer;
    private Button cheek;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finishpage);
        Button button = (Button)findViewById(R.id.return_Mainactivity);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(finishpage.this, MainActivity.class);
                startActivity(intent);
                finishpage.this.finish();
            }
        });

        MapsActivity.mPlayer.stop();
        musicPlay();
    }

    //放音樂
    public void musicPlay() {
        try
        {
            mPlayer = MediaPlayer.create(this, R.raw.finish);
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
}