package com.sidak.yesmam;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
	private Button selectHolidays;
	
	public final String TAG =SemesterActivity.class.getSimpleName();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);

		startDate = (TextView) findViewById(R.id.startDate);
		endDate = (TextView)findViewById(R.id.endDate);
		showStartDate = (TextView) findViewById(R.id.showStartDate);
		showEndDate = (TextView) findViewById(R.id.showEndDate);
		enableSaturday = (CheckBox) findViewById(R.id.enableSaturday);
		enableSunday = (CheckBox) findViewById(R.id.enableSunday);
		reset =(Button)findViewById(R.id.reset);
		addCourses =(Button)findViewById(R.id.addCourses);
		planHolidays =(Button)findViewById(R.id.planHolidays);
		selectHolidays =(Button)findViewById(R.id.selectHolidays);

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
	}
	/*public void onClick(View v){
		switch(v.getId()){
		case R.id.showEndDate:
			showDatePickerDialog((TextView) v);
		case R.id.showStartDate:
			showDatePickerDialog((TextView) v);
		case R.id.reset:
			resetFields();
		}
	}*/
	
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
	
}
