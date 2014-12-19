package com.sidak.yesmam;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivityHoliday extends Activity {
	private TextView todayDate;
	private ImageButton log;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_holiday);
		todayDate = (TextView) findViewById(R.id.todayDate);
		log = (ImageButton) findViewById(R.id.log);
		log.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivityHoliday.this, WorkingDayList.class);
				startActivity(i);
			}
		});
		todayDate.setText(UIHelper.getCurrentDate());
		Log.v("ghjewfegwe", todayDate.getText().toString());
		
		
		
	}
}
