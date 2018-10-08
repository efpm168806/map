package com.example.use.map;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends Activity implements SensorEventListener{
    private TextView lab_X;
    private TextView lab_Y;
    private TextView lab_Z;

    private SensorManager sensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        FindViews();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetSensor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void FindViews()
    {
        lab_X = (TextView) this.findViewById(R.id.lab_X);
        lab_Y = (TextView) this.findViewById(R.id.lab_Y);
        lab_Z = (TextView) this.findViewById(R.id.lab_Z);
    }

    protected void SetSensor()
    {
        List sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if (sensors.size()>0)
        {
            sensorManager.registerListener(SensorActivity.this, (Sensor) sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        float[] values = event.values;
        lab_X.setText("X：" + String.valueOf(values[0]));
        lab_Y.setText("Y：" + String.valueOf(values[1]));
        lab_Z.setText("Z：" + String.valueOf(values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }
}