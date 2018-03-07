package com.example.mcmorris.imtilted;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Josh on 2/27/2018.
 * Manages the Tilt
 * TODO: Potential improve efficiency, research delays
 */

public class TiltManager implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor magSensor;
    private float accelValues[] = new float[9];
    private float magValues[] = new float[9];

    float[] getTilt() {
        return tilt;
    }

    private float[] tilt = new float[3];

    float[] getInitialTilt() {
        return initialTilt;
    }

    private float[] initialTilt = null;

    TiltManager(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        try {
            accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        } catch(java.lang.NullPointerException e) {
            //Like log something or literally do anything to tell the user they can't play this game
            //Or just give them button controls
        }
    }

    void start() {
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    //USE THIS IF GAME PAUSED
    //DON'T MAKE PHONE KEEP DOING THIS
    void stop() {
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
