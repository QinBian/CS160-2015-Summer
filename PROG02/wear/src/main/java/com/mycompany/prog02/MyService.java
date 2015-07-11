package com.mycompany.prog02;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by qinbian on 7/6/15.
 */
public class MyService extends Service implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int MOVE_THRESHOLD = 400;

    /*
    This method is used to detect the shake gesture. It is invoked every time the
    built-in sensor detects a change.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x_val = sensorEvent.values[0];
            float y_val = sensorEvent.values[1];
            float z_val = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();
            if((curTime - lastUpdate) > 100) {
                long diffTime = curTime-lastUpdate;
                lastUpdate = curTime;

                float speed = Math.abs(x_val+y_val+z_val-last_x-last_y-last_z)/diffTime * 10000;
                if (speed > MOVE_THRESHOLD){
                    showNotification();
                }
                last_x = x_val;
                last_y = y_val;
                last_z = z_val;
            }
        }
    }

    protected void showNotification(){
        Intent resultIntent = new Intent(this, PhotoActivity.class);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Excited?")
                .setContentText("Let's record it!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_background));
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
