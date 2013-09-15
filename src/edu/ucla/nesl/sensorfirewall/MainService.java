// 5s: 616 on modified AOSP
// 5s: 616 on unmodified AOSP

package edu.ucla.nesl.sensorfirewall;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service implements SensorEventListener {
	public static final String LOG_TAG = "MainActivity";
	public static final String ACC = "acc";
	private final IBinder mBinder = new LocalBinder();
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;

	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(LOG_TAG, "in onBind of MainService");
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		MainService getService() {
			return MainService.this;
		}
	}
	
	public void testSensor(String sensorType) {
		Log.i(LOG_TAG, "in test sensor");
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public void stopSensor() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//Log.i("LOG_TAG", "acc data received " + event.values[0]);
		if (event.values[0] == -2) {
			Log.d(LOG_TAG, "system time=" + System.nanoTime());
			stopSensor();
		}
	}
}
