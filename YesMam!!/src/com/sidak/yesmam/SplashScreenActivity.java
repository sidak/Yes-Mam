package com.sidak.yesmam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashScreenActivity extends Activity {

	private static int SPLASH_TIME_OUT = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

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
