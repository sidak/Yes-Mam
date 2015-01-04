package com.sidak.yesmam;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Holiday;
import com.sidak.yesmam.model.WorkingDay;

public class WorkingDayList extends ListActivity {
	public static final String TAG=WorkingDayList.class.getSimpleName();
	private SharedPreferences coursePref, loadWdayPref,wdayPref;
	private List<WorkingDay> wdays;
	private WorkingDaysDataSource wDatasource;
	//private Button addWday;
	private Holiday holidayAdded;
	private boolean inPlanned;
	private ListView lv;
	ArrayAdapter<WorkingDay> adapter;
	private int NUM_COURSES;
	private WorkingDay wday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working_day_list);
		//addWday=(Button)findViewById(R.id.addWday);
		lv =(ListView)findViewById(android.R.id.list);
		
		coursePref=getApplicationContext()
				.getSharedPreferences("course prefs", 0);
		loadWdayPref=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		NUM_COURSES=coursePref.getInt("num courses", 0);
		
		Log.i(TAG, "in oncreate");
		// num of courses
		wDatasource = new WorkingDaysDataSource(this);
		wDatasource.open();
		Log.i(TAG, "after opening databasse");
		// display only working days that have passed already from the first working day
		
		wdays = wDatasource.findBeforeToday(wDatasource.findFirstDate(), UIHelper.getCurrentDate());
		Log.i(TAG, "after dtasrc.findall w/o if");

		/*if (holidays.size() == 0) {
			Log.i(TAG, "after dtasrc.findall in if");

			createData();
			Log.i(TAG, "after cretae data in if");

			holidays = datasource.findAll();
			Log.i(TAG, "after findall and cretae data in if");

		}*/
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				wday = wdays.get(position);
				Globals.workday=wday;
				// don't load the wday itself, since this activity already stores for it 
				/*SharedPreferences.Editor ed= loadWdayPref.edit();
				ed.putBoolean(getString(R.string.toLoadWday), Boolean.FALSE);
				ed.commit();*/
				// open a new activity for showing log template activity 
				Intent i = new Intent(WorkingDayList.this, PastWdayActivity.class);
				startActivity(i);
				// use this working day to initialise the state of the next activity
			}
		});
		
		refreshDisplayAndUpdate();
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		wDatasource.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		wDatasource.close();
	}
	
	public void refreshDisplayAndUpdate() {
		adapter = new WorkingDayAdapter(this, wdays);
		setListAdapter(adapter);
	}
	
	
}
