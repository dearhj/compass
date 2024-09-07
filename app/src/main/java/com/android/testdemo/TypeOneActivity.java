package com.android.testdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class TypeOneActivity extends Activity implements SensorEventListener {
    //使用方向传感器编写指南针
    public SensorManager sensorManager;
    private float currentDegree;
    private TextView tvDirection;       //指南针文字显示方位控件

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currentDegree = 0f;
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.compass00);
        tvDirection = findViewById(R.id.value01);

        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(this, TypeTwoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        //为方向传感器注册监听器
        super.onResume();
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        for (Sensor s : sensors) {
            sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (null != sensorManager) {
            sensorManager.unregisterListener(this);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView value = findViewById(R.id.value);
        ImageView imageView = findViewById(R.id.compass);
        float[] values = event.values;
        StringBuilder stringBuilder = new StringBuilder();
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            stringBuilder.append("方向传感器的返回值：");
            stringBuilder.append("\nX:").append(values[0]);
            stringBuilder.append("\nY:").append(values[1]);
            stringBuilder.append("\nZ:").append(values[2]);
            value.setText(stringBuilder.toString());
            float degree = event.values[0];
            float azimuth = (degree + 360) % 360;
            RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            if (azimuth <= 22.5 || azimuth >= 337.5) {
                tvDirection.setText("方位：北 " + (int) azimuth + "°");
            } else if (22.5 < azimuth && azimuth < 67.5) {
                tvDirection.setText("方位：东北 " + (int) azimuth + "°");
            } else if (67.5 <= azimuth && azimuth <= 112.5) {
                tvDirection.setText("方位：东 " + (int) azimuth + "°");
            } else if (112.5 < azimuth && azimuth < 157.5) {
                tvDirection.setText("方位：东南 " + (int) azimuth + "°");
            } else if (157.5 <= azimuth && azimuth <= 202.5) {
                tvDirection.setText("方位：南 " + (int) azimuth + "°");
            } else if (202.5 < azimuth && azimuth < 247.5) {
                tvDirection.setText("方位：西南 " + (int) azimuth + "°");
            } else if (247.5 <= azimuth && azimuth <= 292.5) {
                tvDirection.setText("方位：西 " + (int) azimuth + "°");
            } else if (292.5 < azimuth && azimuth < 337.5) {
                tvDirection.setText("方位：西北 " + (int) azimuth + "°");
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            imageView.setAnimation(rotateAnimation);
            currentDegree = -degree;
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

