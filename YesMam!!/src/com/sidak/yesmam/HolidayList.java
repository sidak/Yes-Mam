package com.sidak.yesmam;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.model.Holiday;

public class HolidayList extends ListActivity {
	public static final String TAG=HolidayList.class.getSimpleName();
	private List<Holiday> holidays;
	private HolidaysDataSource datasource;
	private Button addHoliday;
	private Holiday holidayAdded;
	private boolean inPlanned;
	private ListView lv;
	ArrayAdapter<Holiday> adapter;
	private int holidaysNum;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday_list);
		addHoliday=(Button)findViewById(R.id.addHoliday);
		lv =(ListView)findViewById(android.R.id.list);
		lv.setLongClickable(true);
		
		Log.i(TAG, "in oncreate");
		datasource = new HolidaysDataSource(this);
		datasource.open();
		Log.i(TAG, "after opening databasse");

		holidays = datasource.findAll();
		Log.i(TAG, "after dtasrc.findall w/o if");

		/*if (holidays.size() == 0) {
			Log.i(TAG, "after dtasrc.findall in if");

			createData();
			Log.i(TAG, "after cretae data in if");

			holidays = datasource.findAll();
			Log.i(TAG, "after findall and cretae data in if");

		}*/
		
		refreshDisplayAndUpdate();
		
		addHoliday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addHolidayInDatabase();
			}

			
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Holiday removeHoliday=adapter.getItem(position);
				//Log.v(TAG, removeHoliday.getDescription());
				boolean result= datasource.remove(removeHoliday);
				getHolidaysWithType();
				if(result){
					Toast.makeText(HolidayList.this, "holiday deleted", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(HolidayList.this, "holiday not  deleted", Toast.LENGTH_LONG).show();
				}
				return result;
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1){
			if (resultCode==RESULT_OK){
				holidayAdded= new Holiday();
				holidayAdded.setDescription(data.getExtras().getString("desc"));
				holidayAdded.setType(data.getExtras().getString("type"));
				String date = data.getExtras().getString("date");
				int[] dateEle=UIHelper.getDateFromText(date, this);
//				Date d = UIHelper.getDateFromText(date, this);
//				Calendar c = Calendar.getInstance();
//				c.setTime(d);
//				int day = c.get(Calendar.DATE);
//				int month = c.get(Calendar.MONTH)+1;
//				int year= c.get(Calendar.YEAR);
				holidayAdded.setDay(dateEle[0]);
				holidayAdded.setMonth(dateEle[1]);
				holidayAdded.setYear(dateEle[2]);
				datasource.open();//do it again since onstop was c/d
				datasource.create(holidayAdded);
				getHolidaysWithType();
				
			}
		}
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
			refreshDisplayAndUpdate();
			inPlanned=false;
			break;
			
		case R.id.menu_planned:
			holidays = datasource.findFiltered("type = 'Planned'", null);
			refreshDisplayAndUpdate();
			inPlanned=true;
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	// this method is called to send back the holiday num to semester activity
	@Override
	public void onBackPressed() {
		sendBackHolidayNum();
		super.onBackPressed();
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
	private void addHolidayInDatabase() {
		Intent intent= new Intent(HolidayList.this, AddHolidays.class);
		startActivityForResult(intent, 1);		
	}
	public int getHolidayNum(){
		return holidaysNum;
	}
	
	private void getHolidaysWithType() {
		datasource.open();
		if(inPlanned){
			holidays = datasource.findFiltered("type = 'Planned'", null);
			refreshDisplayAndUpdate();
		}
		else{
			holidays = datasource.findAll();
			refreshDisplayAndUpdate();
		}
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

		Holiday holiday2= new Holiday(21,6,2014,"bday", Holiday.TYPE_INSTI);
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

	public void refreshDisplayAndUpdate() {
		adapter = new HolidayListAdapter(this, holidays);
		setListAdapter(adapter);
		holidaysNum=datasource.getHolidayNum();
	}
	private void sendBackHolidayNum(){
		Intent returnIntent= new Intent();
		returnIntent.putExtra("holidayNum", getHolidayNum());
		setResult(RESULT_OK, returnIntent);
	}
	
}
