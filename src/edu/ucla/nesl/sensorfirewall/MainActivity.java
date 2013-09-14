package edu.ucla.nesl.sensorfirewall;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import edu.ucla.nesl.sensorfirewall.MainService.LocalBinder;

public class MainActivity extends Activity {
	MainService mService;
	public static final String LOG_TAG = "MainActivity";
	boolean mBound = false;
	
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            Log.i(LOG_TAG, "Connected to " + mService.toString());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    
    public void onBtnClicked(View v){
        if(v.getId() == R.id.button1){
        	Log.i(LOG_TAG, "Start");
        	mService.testSensor(MainService.ACC);
        }
        if(v.getId() == R.id.button2) {
        	Log.i(LOG_TAG, "Stop");
        	mService.stopSensor();
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent bindIntent = new Intent(this, MainService.class);
        bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
        Log.i(LOG_TAG, "bind to service");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mService = null;
	}
}
