package com.sidak.yesmam;

import java.util.List;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Holiday;
import com.sidak.yesmam.model.WorkingDay;

public class WorkingDayList extends ListActivity {
	public static final String TAG=WorkingDayList.class.getSimpleName();
	private SharedPreferences coursePref;
	private List<WorkingDay> wdays;
	private WorkingDaysDataSource wDatasource;
	//private Button addWday;
	private Holiday holidayAdded;
	private boolean inPlanned;
	private ListView lv;
	ArrayAdapter<WorkingDay> adapter;
	private int NUM_COURSES;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working_day_list);
		//addWday=(Button)findViewById(R.id.addWday);
		lv =(ListView)findViewById(android.R.id.list);
		
		coursePref=getApplicationContext()
				.getSharedPreferences("course prefs", 0);
		NUM_COURSES=coursePref.getInt("num courses", 0);
		
		Log.i(TAG, "in oncreate");
		// num of courses
		wDatasource = new WorkingDaysDataSource(this,NUM_COURSES);
		wDatasource.open();
		Log.i(TAG, "after opening databasse");

		wdays = wDatasource.findAll();
		Log.i(TAG, "after dtasrc.findall w/o if");

		/*if (holidays.size() == 0) {
			Log.i(TAG, "after dtasrc.findall in if");

			createData();
			Log.i(TAG, "after cretae data in if");

			holidays = datasource.findAll();
			Log.i(TAG, "after findall and cretae data in if");

		}*/
		
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
