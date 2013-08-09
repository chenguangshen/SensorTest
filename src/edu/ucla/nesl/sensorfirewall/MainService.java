// 5s: 616 on modified AOSP
// 5s: 616 on unmodified AOSP

package edu.ucla.nesl.sensorfirewall;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service implements SensorEventListener {
	public static final String LOG_TAG = "MainActivity";
	public static final String ACC = "acc";
	private final IBinder mBinder = new LocalBinder();
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private static int count = 0;
    private static MainService service;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "in onBind of MainService");
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        service = this;
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		MainService getService() {
			return MainService.this;
		}
	}
	
	public void testSensor(String sensorType) {
		Log.i(LOG_TAG, "in test sensor");
		count = 0;
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		//new CountTime().execute("null");
		new CountTime().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "null");
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		count++;
		Log.i("LOG_TAG", "acc data received");
	}
	
	private class CountTime extends AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mSensorManager.unregisterListener(service);
			Log.i("LOG_TAG", "sensor event num: " + count);
			return count;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
		}	
	}
}
