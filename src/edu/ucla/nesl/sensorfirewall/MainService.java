// 5s: 616 on modified AOSP
// 5s: 616 on unmodified AOSP

package edu.ucla.nesl.sensorfirewall;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
		PackageManager packageManager = this.getPackageManager();
		try {
			ApplicationInfo app = packageManager.getApplicationInfo("edu.ucla.nesl.sensorfirewall", PackageManager.GET_META_DATA);
			Toast.makeText(this, "uid=" + app.uid, Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException e) {
			//e.printStackTrace();
			Toast.makeText(this, "app not found", Toast.LENGTH_LONG).show();
		}
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
		Log.i("LOG_TAG", "acc data received " + event.values[0]);
	}
}
