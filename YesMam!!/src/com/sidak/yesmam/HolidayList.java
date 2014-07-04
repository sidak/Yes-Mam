package com.sidak.yesmam;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.model.Holiday;

public class HolidayList extends ListActivity {
	public static final String TAG=HolidayList.class.getSimpleName();
	private List<Holiday> holidays;
	private HolidaysDataSource datasource;
	private Button addHoliday;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday_list);
		addHoliday=(Button)findViewById(R.id.addHoliday);
		Log.i(TAG, "in oncreate");
		datasource = new HolidaysDataSource(this);
		datasource.open();
		Log.i(TAG, "after opening databasse");

		holidays = datasource.findAll();
		Log.i(TAG, "after dtasrc.findall w/o if");

		if (holidays.size() == 0) {
			Log.i(TAG, "after dtasrc.findall in if");

			createData();
			Log.i(TAG, "after cretae data in if");

			holidays = datasource.findAll();
			Log.i(TAG, "after findall and cretae data in if");

		}
		refreshDisplay();
		
		
	}
	
	private void createData() {
		Holiday holiday1= new Holiday();
		holiday1.setDay(20);
		holiday1.setMonth(6);
		holiday1.setYear(2014);
		holiday1.setDescription("wej");
		holiday1.setType(Holiday.TYPE_INSTI);
		datasource.create(holiday1);
		Log.i(TAG, "after holiday1");

		Holiday holiday2= new Holiday();
		holiday2.setDay(21);
		holiday2.setMonth(6);
		holiday2.setYear(2014);
		holiday2.setDescription("bday");
		holiday2.setType(Holiday.TYPE_INSTI);
		datasource.create(holiday2);
		Log.i(TAG, "after holiday2");

		Holiday holiday3= new Holiday();
		holiday3.setDay(22);
		holiday3.setMonth(6);
		holiday3.setYear(2014);
		holiday3.setDescription("wegrej");
		holiday3.setType(Holiday.TYPE_PLANNED);
		datasource.create(holiday3);
		Log.i(TAG, "after holiday3");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_holidays,menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_all:
			holidays = datasource.findAll();
			refreshDisplay();
			break;

		case R.id.menu_planned:
			holidays = datasource.findFiltered("type = 'Planned'", null);
			refreshDisplay();
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void refreshDisplay() {
		ArrayAdapter<Holiday> adapter = new ArrayAdapter<Holiday>(this, 
				android.R.layout.simple_list_item_1, holidays);
		setListAdapter(adapter);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}
}
