package com.sidak.yesmam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final String TAG =MainActivity.class.getSimpleName();
	private SharedPreferences prefs;
	private int numWorkingDays;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getApplicationContext().getSharedPreferences(getString(R.string.semPrefs),0);
		numWorkingDays= prefs.getInt(getString(R.string.numWorkingDays), 0);
		Log.v(TAG, ""+numWorkingDays);
		TextView t = (TextView)findViewById(R.id.trial);
		t.setText(""+numWorkingDays);
	}
}
