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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class TypeTwoActivity extends Activity implements SensorEventListener {
    //通过加速度传感器和地磁传感器编写指南针
    public SensorManager sensorManager;

    public ImageView ivArrow;     //指南针指针控件
    private TextView tvDirection;       //指南针文字显示方位控件

    private final float[] mGravity = new float[3];
    private final float[] mGeomagnetic = new float[3];

    private float azimuth = 0f;
    private float currentAzimuth = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass01);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ivArrow = findViewById(R.id.compass);
        tvDirection = findViewById(R.id.value02);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        findViewById(R.id.button1).setOnClickListener(v -> {
            Intent intent = new Intent(this, TypeOneActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != sensorManager) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != sensorManager) {
            sensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.98f;

        synchronized (this) {
            //指南针转动角度算法
            //判断当前是加速度感应器还是地磁感应器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                TextView value00 = findViewById(R.id.value00);
                float[] values = event.values;
                String stringBuilder = "加速度计的返回值：" +
                        "\nX:" + values[0] +
                        "\nY:" + values[1] +
                        "\nZ:" + values[2];
                value00.setText(stringBuilder);
                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                TextView value01 = findViewById(R.id.value01);
                float[] values = event.values;
                String stringBuilder = "磁场传感器的返回值：" +
                        "\nX:" + values[0] +
                        "\nY:" + values[1] +
                        "\nZ:" + values[2];
                value01.setText(stringBuilder);
                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
            }

            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + 360) % 360;
                adjustArrow();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 陀螺仪方位显示&角度转动
     */
    @SuppressLint("SetTextI18n")
    private void adjustArrow() {
        if (ivArrow == null) return;

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;
        Log.i("TAG", "azimuth:" + azimuth);
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

        an.setDuration(200);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        ivArrow.startAnimation(an);//动画效果转动传感器
    }
}



