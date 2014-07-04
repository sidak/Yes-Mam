package com.sidak.yesmam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class SplashScreenActivity extends Activity {
	
	private static int SPLASH_TIME_OUT = 1500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// call main activity
				Intent intent= new Intent(SplashScreenActivity.this, SemesterActivity.class);
				// if u directly use 'this' for context, then this refers to the
				// runnable and not the splashscreen activity
				startActivity(intent);
				finish();
				
			}
		}, SPLASH_TIME_OUT);
	}
}
