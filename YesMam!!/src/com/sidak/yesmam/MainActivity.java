package com.sidak.yesmam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.Holiday;

public class MainActivity extends Activity {

	public final String TAG = MainActivity.class.getSimpleName();
	private SharedPreferences prefs;
	private int numWorkingDays;
	private Calendar c;
	private List<Holiday> holidays;
	private List<Course> todayCourses;
	private HolidaysDataSource holiDatasource;
	private CoursesDataSource coursesDataSource;
	private String holidayType;
	private String holidayDesc;
	private TextView todayDate;
	private Button attend;
	private Button bunk;
	private Button proxy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getApplicationContext().getSharedPreferences(
				getString(R.string.semPrefs), 0);
		numWorkingDays = prefs.getInt(getString(R.string.numWorkingDays), 0);
		Log.v(TAG, "" + numWorkingDays);

		holiDatasource = new HolidaysDataSource(this);
		holiDatasource.open();
		Log.i(TAG, "after opening holiday databasse");
		coursesDataSource = new CoursesDataSource(this);
		coursesDataSource.open();
		Log.v(TAG, "after opnening courses database");
		todayDate = (TextView) findViewById(R.id.todayDate);
		attend = (Button) findViewById(R.id.attendButton);
		bunk = (Button) findViewById(R.id.bunkButton);
		proxy = (Button) findViewById(R.id.proxyButton);
		attend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				markAttendance(1);
			}
		});
		bunk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				markAttendance(0);
			}
		});
		proxy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getHelp();
			}
		});
		// check if it is a holiday
		// also cache the value for a particular day by checking the
		// current date and the variable
		// UIHelper.checkIfWeekend(s)
		// check for extra class
		// then if none: check for todays' classes

	}

	protected void getHelp() {
		// TODO Auto-generated method stub

	}

	protected void markAttendance(int i) {
		// mark the attendance of a particular course which is selected

	}

	private boolean checkHoliday() {
		int date[] = UIHelper.getDateFromText(getCurrentDate(), this);
		Log.v(TAG, " " + date[0] + " " + date[1] + " " + date[2]);
		holidays = holiDatasource.findWhereDatesMatch(new String[] {
				"" + date[0], "" + date[1], "" + date[2] });
		if (holidays == null) {
			// not a holiday
			return false;
		} else if (holidays.size() > 0) {
			Holiday holiday = holidays.get(0);
			holidayDesc = holiday.getDescription();
			holidayType = holiday.getType();
			return true;
		} else {
			return false;
		}
	}

	private String getCurrentDate() {
		c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = df.format(c.getTime());
		todayDate.setText(formattedDate);
		Log.v(TAG, "current date " + formattedDate);
		return formattedDate;
	}

	@Override
	protected void onResume() {
		super.onResume();
		holiDatasource.open();
		if (checkHoliday()) {
			// change in ui to show today's classes
			// cache the result

		} else {
			// cache today's courses
			todayCourses = coursesDataSource.getTodaysCourses(UIHelper
					.getDayOfWeekFromDate(UIHelper
							.getDateObjectFromText(todayDate.getText()
									.toString())), getString(R.string.enterDate));
			
			
			// display them according to current time , with the course next or
			// in course time selectd or focussed
			// show that particular course's attendance
			// put today's classes in the log database
			// cache todays' classes
			// show notifications for classes whose attendance has not been done
			// upto 3 classses, then just link to app
			// make the notification sticky
			// longpress to edit a particular class details
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		holiDatasource.close();
	}

}
