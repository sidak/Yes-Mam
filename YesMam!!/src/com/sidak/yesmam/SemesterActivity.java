package com.sidak.yesmam;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.sidak.yesmam.db.HolidaysDataSource;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SemesterActivity extends Activity {
	private TextView semesterText;
	private TextView startDate;
	private TextView endDate;
	private TextView showStartDate;
	private TextView showEndDate;
	private CheckBox enableSaturday;
	private CheckBox enableSunday;
	private Button reset;
	private Button addCourses;
	private Button planHolidays;
	private Date start;
	private Date end;
	private HolidaysDataSource datasource;

	
	public final String TAG =SemesterActivity.class.getSimpleName();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);

		startDate = (TextView) findViewById(R.id.startDate);
		endDate = (TextView)findViewById(R.id.endDate);
		showStartDate = (TextView) findViewById(R.id.showHolidayDate);
		showEndDate = (TextView) findViewById(R.id.showEndDate);
		enableSaturday = (CheckBox) findViewById(R.id.enableSaturday);
		enableSunday = (CheckBox) findViewById(R.id.enableSunday);
		reset =(Button)findViewById(R.id.reset);
		addCourses =(Button)findViewById(R.id.addCourses);
		planHolidays =(Button)findViewById(R.id.planHolidays);
		datasource = new HolidaysDataSource(this);
		datasource.open();

		showStartDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog((TextView) v);
			}
		});
		
		showEndDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog((TextView) v);
			}
		});
		reset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetFields();
			}
		});
		planHolidays.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(SemesterActivity.this, HolidayList.class);
				startActivity(intent);
			}
		});
		addCourses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validateCalculateSaveSemester();
			}
		});
	}
	// showStartDate and showEndDate contain the date entered
	protected void validateCalculateSaveSemester() {
		if(ifEmptyTextview(showStartDate)||ifEmptyTextview(showEndDate)){
			Toast.makeText(this, "Please enter the required dates", Toast.LENGTH_LONG).show();
			return;
		}
		String dateStart= UIHelper.getTextFromTextview(this, showStartDate.getId());
		String dateEnd= UIHelper.getTextFromTextview(this, showEndDate.getId());
		start=UIHelper.getDateObjectFromText(dateStart);
		end=UIHelper.getDateObjectFromText(dateEnd);
		if(!validateDates(start, end)){
			Toast.makeText(this, "Please check the dates entered", Toast.LENGTH_LONG).show();
			return;
		}
		Log.v(TAG, start.toString());
		Log.v(TAG, end.toString());

		//--------------- dates validated------------------
		int workingDays=calculateWorkingDays(start, end);
		saveSemesterInfo();
	}

	private void saveSemesterInfo() {
		// TODO Auto-generated method stub
		Log.i(TAG, "data saved");
	}
	private boolean ifEmptyTextview(TextView tv) {
		return (UIHelper.getTextFromTextview(this, tv.getId())==getString(R.string.enterDate));
	}
	private void showDatePickerDialog(TextView tv) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new DatePickerFragment(tv);
		newFragment.show(ft, "datePicker");
	}
	public void resetFields(){
		if(enableSaturday.isChecked()){
			enableSaturday.setChecked(false);
		}
		if(enableSunday.isChecked()){
			enableSunday.setChecked(false);
		}
		showEndDate.setText(getString(R.string.enterDate));
		showStartDate.setText(getString(R.string.enterDate));

	}
//	public Date getDateFromText(String date){
//		if(date==getString(R.string.enterDate)){
//			return null;
//		}
//		String[] dateElements=date.split("/");
//		int day =Integer.parseInt(dateElements[0]);
//		int month =Integer.parseInt(dateElements[1]);
//		int year =Integer.parseInt(dateElements[2]);
//		Date d = null;
//		Calendar cal = GregorianCalendar.getInstance();
//		cal.set(1900 + year, month-1, day);
//		d = cal.getTime();
//		return d;
//
//	}
	public boolean validateDates(Date before, Date after){
		if(after.compareTo(before)<=0){
			return false;
		}
		return true;
	}
	
	public int calculateWorkingDays(Date olderDate, Date newerDate){
		//TODO: calculate working days on the basis of start date, end date, sat, sun, 
		//holidays
		int diffInDays = (int)( (newerDate.getTime() - olderDate.getTime()) 
                / (1000 * 60 * 60 * 24) );
		int numHolidays= datasource.getHolidayNum();
		int numSat;
		int numSun;
		
		return 0;
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
