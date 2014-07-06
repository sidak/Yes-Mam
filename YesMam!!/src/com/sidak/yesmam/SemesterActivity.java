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
	private final int PLAN_HOLIDAYS_CODE=1;
	private int numHolidays;

	
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
				startActivityForResult(intent, PLAN_HOLIDAYS_CODE);
			}
		});
		addCourses.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				validateCalculateSaveSemester();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==PLAN_HOLIDAYS_CODE && resultCode==RESULT_OK){
			numHolidays=data.getExtras().getInt("holidayNum");
			Log.i(TAG, ""+numHolidays+": number of holidays ");
		}
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
		// TODO:make an intent to add courses view
		// implement that as list like holidays and set it various fields
		
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

	public boolean validateDates(Date before, Date after){
		if(after.compareTo(before)<=0){
			return false;
		}
		return true;
	}
	
	public int calculateWorkingDays(Date olderDate, Date newerDate){
		int diffInDays = (int)( (newerDate.getTime() - olderDate.getTime()) 
                / (1000 * 60 * 60 * 24) );
		// TODO:also need to check if holiday specified by the user / insti 
		// shldn't lie on saturdays and sundays
		int startDateDay=UIHelper.getDayOfWeekFromDate(olderDate);
		int endDateDay=UIHelper.getDayOfWeekFromDate(newerDate);
		int numSat=0;
		int numSun=0;

		if(startDateDay<=endDateDay){
			numSat= (int)Math.floor(diffInDays/7.0);
			numSun= (int)Math.floor(diffInDays/7.0);

		}
		else{
			numSat= (int)Math.ceil(diffInDays/7.0);
			numSun= (int)Math.ceil(diffInDays/7.0);

		}
		int workingWeekends=0;
		if(checkWeekend(enableSaturday)){
			workingWeekends+=numSat;
		}
		if(checkWeekend(enableSunday)){
			workingWeekends+=numSun;
		}
		int totalWorkingDays=diffInDays-numHolidays-numSat - numSun + workingWeekends;
		Log.v(TAG, "total working days" + totalWorkingDays);
		return totalWorkingDays;
	}
	public boolean checkWeekend(CheckBox cb){
		return UIHelper.getCBChecked(this, cb.getId());
	}
	
	
	
	
}
