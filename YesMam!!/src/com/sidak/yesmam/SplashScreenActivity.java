package com.sidak.yesmam;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.WorkingDay;

public class SplashScreenActivity extends Activity {

	private static final String TAG = SplashScreenActivity.class.getSimpleName();
	private static int SPLASH_TIME_OUT = 1000;
	private WorkingDaysDataSource wDataSource;
	private List<WorkingDay> wdays;
	private WorkingDay wday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		// LOAD A WORKING DAT OBJECT BASED ON THE CURRENT DATE FROM THE WORKING DAYS DATABASE
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		boolean previouslyStarted = prefs.getBoolean(
				getString(R.string.prefPreviouslyStarted), false);
		if (!previouslyStarted) {
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean(getString(R.string.prefPreviouslyStarted),
					Boolean.TRUE);
			edit.commit();
			runDelayedNextActivity(SemesterActivity.class);
		} else {
			wDataSource = new WorkingDaysDataSource(this);
			wDataSource.open();
			Log.i(TAG, "after opening databasse");

			wdays = wDataSource.findCurrent(UIHelper.getCurrentDate());
			wday=wdays.get(0);
			Globals.workday=wday;
			runDelayedNextActivity(MainActivity.class);

		}
		
	}

	private void runDelayedNextActivity(final Class c) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// call main activity
				Intent intent = new Intent(SplashScreenActivity.this, c);
				// if u directly use 'this' for context, then this refers to the
				// runnable and not the splashscreen activity
				startActivity(intent);
				finish();

			}
		}, SPLASH_TIME_OUT);
	}
}
