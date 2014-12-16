package com.sidak.yesmam;

import java.util.Date;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class SemesterActivity extends Activity implements OnClickListener {
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
	private final int PLAN_HOLIDAYS_CODE = 1;
	private int numHolidays;
	private int workingDays;
	private String dateStart;
	private String dateEnd;
	private SharedPreferences prefs;
	/*private int mondays;
	private int tuesdays;
	private int wednesdays;
	private int thursdays;
	private int fridays;
	*/

	public final String TAG = SemesterActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);
		// labels for dates
		startDate = (TextView) findViewById(R.id.startDate);
		endDate = (TextView) findViewById(R.id.endDate);
		// hold values for dates
		showStartDate = (TextView) findViewById(R.id.sDateValue);
		showEndDate = (TextView) findViewById(R.id.showEndDate);

		enableSaturday = (CheckBox) findViewById(R.id.enableSaturday);
		enableSunday = (CheckBox) findViewById(R.id.enableSunday);
		reset = (Button) findViewById(R.id.reset);
		addCourses = (Button) findViewById(R.id.addCourses);
		planHolidays = (Button) findViewById(R.id.planHolidays);
		// attach on click listeners
		showStartDate.setOnClickListener(this);
		showEndDate.setOnClickListener(this);
		reset.setOnClickListener(this);
		planHolidays.setOnClickListener(this);
		addCourses.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sDateValue:
			showDatePickerDialog((TextView) v);
			break;
		case R.id.showEndDate:
			showDatePickerDialog((TextView) v);
			break;
		case R.id.reset:
			resetFields();
			break;
		case R.id.planHolidays:
			Intent intent = new Intent(SemesterActivity.this, HolidayList.class);
			if (validateCalculateSemester()) {
				saveSemesterInfo();
			}
			//intent.putExtra("dateEnd", dateEnd);
			//intent.putExtra("dateStart", dateStart);
			startActivityForResult(intent, PLAN_HOLIDAYS_CODE);
			break;
		case R.id.addCourses:
			if (validateCalculateSemester()) {
				saveSemesterInfo();
				getCourseView();
			}
			break;
		default:
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLAN_HOLIDAYS_CODE && resultCode == RESULT_OK) {
			numHolidays = data.getExtras().getInt("holidayNum");
			Log.i(TAG, "" + numHolidays + ": number of holidays ");
		}
	}

	// showStartDate and showEndDate contain the date entered
	protected boolean validateCalculateSemester() {
		if (ifEmptyTextview(showStartDate) || ifEmptyTextview(showEndDate)) {
			Toast.makeText(this, "Please enter the required dates",
					Toast.LENGTH_LONG).show();
			return false;
		}
		dateStart = UIHelper.getTextFromTextview(this, showStartDate.getId());
		dateEnd = UIHelper.getTextFromTextview(this, showEndDate.getId());
		start = UIHelper.getDateObjectFromText(dateStart);
		end = UIHelper.getDateObjectFromText(dateEnd);
		if (!validateDates(start, end)) {
			Toast.makeText(this, "Please check the dates entered",
					Toast.LENGTH_LONG).show();
			return false;
		}
		Log.v(TAG, start.toString());
		Log.v(TAG, end.toString());

		// --------------- dates validated------------------
		workingDays = calculateWorkingDays(start, end);
		return true;
	}

	private void getCourseView() {
		Intent intent = new Intent(this, CourseView.class);
		startActivity(intent);
	}

	private void saveSemesterInfo() {
		// TODO Auto-generated method stub
		prefs = getApplicationContext()
				.getSharedPreferences(getString(R.string.semPrefs), 0);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt(getString(R.string.numWorkingDays), workingDays);
		
		/*edit.putInt(getString(R.string.numMon), mondays);
		edit.putInt(getString(R.string.numTue), tuesdays);
		edit.putInt(getString(R.string.numWed), wednesdays);
		edit.putInt(getString(R.string.numThu), thursdays);
		edit.putInt(getString(R.string.numFri), fridays);
		*/
		edit.putString(getString(R.string.dateStart), dateStart);
		edit.putString(getString(R.string.dateEnd), dateEnd);
		edit.commit();

		Log.i(TAG, "data saved");
	}

	private boolean ifEmptyTextview(TextView tv) {
		return (UIHelper.getTextFromTextview(this, tv.getId()) == getString(R.string.enterDate));
	}

	private void showDatePickerDialog(TextView tv) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new DatePickerFragment(tv);
		newFragment.show(ft, "datePicker");
	}

	public void resetFields() {
		if (enableSaturday.isChecked()) {
			enableSaturday.setChecked(false);
		}
		if (enableSunday.isChecked()) {
			enableSunday.setChecked(false);
		}
		showEndDate.setText(getString(R.string.enterDate));
		showStartDate.setText(getString(R.string.enterDate));

	}

	public boolean validateDates(Date before, Date after) {
		if (after.compareTo(before) <= 0) {
			return false;
		}
		return true;
	}

	public int calculateWorkingDays(Date olderDate, Date newerDate) {
		int diffInDays = (int) ((newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24));
		diffInDays++;
		// TODO: also need to check if holiday specified by the user / insti
		// shldn't lie on saturdays and sundays
		int startDateDay = UIHelper.getDayOfWeekFromDate(olderDate);
		int endDateDay = UIHelper.getDayOfWeekFromDate(newerDate);
		int numSat = 0;
		int numSun = 0;
		// wrong
		if (startDateDay < endDateDay) {
			numSat = (int) Math.floor(diffInDays / 7.0);
			numSun = (int) Math.floor(diffInDays / 7.0);
			
		} else {
			numSat = (int) Math.ceil(diffInDays / 7.0);
			numSun = (int) Math.ceil(diffInDays / 7.0);
			
		}
		// assuming sat and sun are off
		/*mondays=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.MONDAY);
		tuesdays=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.TUESDAY);
		wednesdays=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.WEDNESDAY);
		thursdays=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.THURSDAY);
		fridays=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.FRIDAY);
		int total = mondays+thursdays+tuesdays+wednesdays+fridays;
		*/
		int workingWeekends = 0;
		if (checkWeekend(enableSaturday)) {
			workingWeekends += numSat;
		}
		if (checkWeekend(enableSunday)) {
			workingWeekends += numSun;
		}
		
		
		
		int totalWorkingDays = diffInDays - numHolidays - numSat - numSun
				+ workingWeekends;
		Log.v(TAG, "total working days" + totalWorkingDays + "diffin days "
				+ diffInDays + "num holidays " + numHolidays + "num sat "
				+ numSat + "num sun " + numSun + " working weekens "
				+ workingWeekends);
		//Log.v(TAG, totalWorkingDays + " twd  : indv "+ total );
		return totalWorkingDays;
	}

	public boolean checkWeekend(CheckBox cb) {
		return UIHelper.getCBChecked(this, cb.getId());
	}

}
