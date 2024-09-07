package com.android.testdemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {
    //使用方向传感器编写指南针

    private var imageView: ImageView? = null
    private var currentDegree = 0f
    private var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById<View>(R.id.imageview) as ImageView
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }


    override fun onResume() {
        //为方向传感器注册监听器
        super.onResume();
        sensorManager?.registerListener(this,sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME)
    }


    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type ==Sensor.TYPE_ORIENTATION){
            println("执行这里了嘛？？？？？？？？   ${event.values[0]}")
            //取围绕Z轴转过的角度
            val degree = event.values[0]
            val rotateAnimation= RotateAnimation(currentDegree,degree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            rotateAnimation.setDuration(200) //设置动画持续时间
            rotateAnimation.fillAfter = true //设置动画结束后的保留状态
            rotateAnimation.setRepeatCount(0)
            imageView!!.setAnimation(rotateAnimation)
            currentDegree=degree
        }
    }
//----------------------------------------------------------------
//    private var sensorManager: SensorManager? = null
//    private var imageView: ImageView? = null
//    private var lastRotateDegree = 0f
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
//        imageView = findViewById<View>(R.id.imageview) as ImageView
//        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
//        //加速度感应器
//        val magneticSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
////        地磁感应器
//        val accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//
//        sensorManager?.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_GAME)
//
//        sensorManager?.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME)
////        sensorManager?.registerListener(this,sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (sensorManager != null) {
//            sensorManager?.unregisterListener(this)
//        }
//    }
//
//    var accelerometerValues: FloatArray = FloatArray(3)
//
//    var magneticValues: FloatArray = FloatArray(3)
//
//    override fun onSensorChanged(event: SensorEvent) {
//        // 判断当前是加速度感应器还是地磁感应器
//        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
//            //赋值调用clone方法
//            accelerometerValues = event.values.clone()
//        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
//            //赋值调用clone方法
//            magneticValues = event.values.clone()
//        }
//        val R = FloatArray(9)
//        val values = FloatArray(3)
//        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues)
//        SensorManager.getOrientation(R, values)
//        Log.d("Main", "values[0] :" + Math.toDegrees(values[0].toDouble()))
//
//        //values[0]的取值范围是-180到180度。
//        //+-180表示正南方向，0度表示正北，-90表示正西，+90表示正东
//
//        //将计算出的旋转角度取反，用于旋转指南针背景图
//        val rotateDegree = -Math.toDegrees(values[0].toDouble()).toFloat()
//        if (abs((rotateDegree - lastRotateDegree).toDouble()) > 2) {
//
//            println("这里的角度时》》》》  $rotateDegree     $lastRotateDegree")
//            val animation = RotateAnimation(
//                lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f
//            )
//            animation.fillAfter = true
//            imageView!!.startAnimation(animation) //动画效果转动传感器
//            lastRotateDegree = rotateDegree
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//    }
}