package com.example.mcmorrisgray.imtilted;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Josh on 2/27/2018.
 */

public class TiltManager implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor magSensor;
    float accelValues[] = new float[9];
    float magValues[] = new float[9];

    public float[] getTilt() {
        return tilt;
    }

    private float[] tilt = new float[3];

    public float[] getInitialTilt() {
        return initialTilt;
    }

    private float[] initialTilt = null;

    public TiltManager(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    //USE THIS IF GAME PAUSED
    //DON'T MAKE PHONE KEEP DOING THIS
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelValues = sensorEvent.values;
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magValues = sensorEvent.values;
        }

        if(accelValues != null && magValues != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            if(SensorManager.getRotationMatrix(R, I, accelValues, magValues)) {
                tilt = SensorManager.getOrientation(R, tilt);
                if(initialTilt == null) {
                    initialTilt = new float[tilt.length];
                    System.arraycopy(tilt,0, initialTilt, 0, tilt.length);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
