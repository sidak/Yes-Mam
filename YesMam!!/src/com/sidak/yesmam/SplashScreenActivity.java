package com.sidak.yesmam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.UnmarkedClass;
import com.sidak.yesmam.model.WorkingDay;

public class SplashScreenActivity extends Activity {

	private static final String TAG = SplashScreenActivity.class
			.getSimpleName();
	private static int SPLASH_TIME_OUT = 1000;
	private CoursesDataSource courseDataSource;
	private WorkingDaysDataSource wDataSource;
	private List<WorkingDay> wdays;
	private List<UnmarkedClass> unmarkedClasses=null;
	private WorkingDay wday;
	private SharedPreferences wdayPrefs;
	private Map<String, Integer> attend, bunk;
	private boolean isSet;
	private boolean noWday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		// LOAD A WORKING DAT OBJECT BASED ON THE CURRENT DATE FROM THE WORKING
		// DAYS DATABASE
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		boolean previouslyStarted = prefs.getBoolean(
				getString(R.string.prefPreviouslyStarted), false);
		// it should load wday itself
		// after changing working days list to open only previous classes
		// you may omit this
		/*
		 * SharedPreferences.Editor edit = prefs.edit();
		 * edit.putBoolean(getString(R.string.toLoadWday), Boolean.TRUE);
		 * edit.commit();
		 */
	}

	private void runDelayedNextActivity(final Class c) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// call main activity
				Intent intent = new Intent(SplashScreenActivity.this, c);
				// if u directly use 'this' for context, then this refers to the
				// runnable and not the splashscreen activity
				startActivity(intent);
				finish();

			}
		}, SPLASH_TIME_OUT);
	}

	/*
	 * @Override protected void onPause() { // TODO Auto-generated method stub
	 * super.onPause(); courseDataSource.close(); wDataSource.close(); }
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.v(TAG, "hey i am here");

		wdayPrefs = getApplicationContext().getSharedPreferences(
				getString(R.string.wday_prefs), 0);
		isSet = wdayPrefs.getBoolean(getString(R.string.wday_isset), false);

		if (!isSet) {
			/*
			 * edit = prefs.edit();
			 * edit.putBoolean(getString(R.string.prefPreviouslyStarted),
			 * Boolean.TRUE); edit.commit();
			 */
			runDelayedNextActivity(SemesterActivity.class);
		} else {
			courseDataSource = new CoursesDataSource(this);
			courseDataSource.open();
			wDataSource = new WorkingDaysDataSource(this);
			wDataSource.open();
			String lastDateString = wdayPrefs.getString(
					getString(R.string.last_date), "01/01/2000");
			String currentDateString = UIHelper.getCurrentDate();
			Date lastDate = UIHelper.getDateObjectFromText(lastDateString);
			Date currentDate = UIHelper
					.getDateObjectFromText(currentDateString);
			if (!(lastDateString.equals("01/01/2000"))) {
				if (lastDate.before(currentDate)) {
					// get the no of working days between the last updated date
					// and date before current date
					/*
					 * SimpleDateFormat formatter = new
					 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
					 * lastDateFormatted = formatter.format(lastDate); String
					 * currentDateFormatted = formatter.format(currentDate);
					 */
					wdays = wDataSource.findBeforeToday1(lastDateString,
							currentDateString);

					if (wdays.size() > 0) {
						noWday=false;
						// in each working day , find out the courses which have
						// classes on that day
						// then for that course check if attendance was marked
						// if no attendance marked , then add the
						// course-code,date of class to the list of
						// leftCourseCodes
						// if marked then in a local hashmap corresponiding to
						// the course code -

						// initialise hashmap
						attend = new HashMap<String, Integer>();
						bunk = new HashMap<String, Integer>();
						wday = wdays.get(0);
						for (int i = 0; i < wday.codes.size(); i++) {
							attend.put(wday.codes.get(i), 0);
							bunk.put(wday.codes.get(i), 0);
						}
						// initialise the codes and dates list which will be
						// used to store data for classes
						// whose attendance has not been marked
						unmarkedClasses = new ArrayList<UnmarkedClass>();

						for (WorkingDay wd : wdays) {
							int len = wd.codes.size();
							for (int i = 0; i < len; i++) {
								// if corresponding to that course ,there was a
								// class but attendance not marked
								String code = wd.codes.get(i);
								if (wd.attendance.get(i) == 2) {

									UnmarkedClass uc = new UnmarkedClass(
											wd.getDateString(),
											courseDataSource
													.getCourseWithCode(code), 2);
									unmarkedClasses.add(uc);
								}
								// if present was marked
								else if (wd.attendance.get(i) == 1) {
									attend.put(code, attend.get(code) + 1);
								} else if (wd.attendance.get(i) == 0) {
									bunk.put(code, bunk.get(code) + 1);
								}
							}
						}

						// store the value to be added in the fields of
						// num_attendance and num_bunk
						// for each of the course which have had atleat one
						// class,
						// update the value in the database - load initially and
						// then update for each of the course above

						for (Map.Entry<String, Integer> entry : attend
								.entrySet()) {
							// System.out.println(entry.getKey() + "/" +
							// entry.getValue());
							if (entry.getValue() > 0) {
								// load that course from course database
								// then update it's value of num_attend field
								Course course = courseDataSource
										.getCourseWithCode(entry.getKey());
								int numAttend = course.getAttendedClasses()
										+ entry.getValue();
								courseDataSource.markPresent(numAttend,
										entry.getKey());
							}
						}

						for (Map.Entry<String, Integer> entry : bunk.entrySet()) {
							// System.out.println(entry.getKey() + "/" +
							// entry.getValue());
							if (entry.getValue() > 0) {
								// load that course from course database
								// then update it's value of num_bunk field
								Course course = courseDataSource
										.getCourseWithCode(entry.getKey());
								int numBunk = course.getBunkedClasses()
										+ entry.getValue();
								courseDataSource.markAbsent(numBunk,
										entry.getKey());
							}
						}
					}
					else{
						noWday=true;
						Log.v(TAG, "wdays size =0");
					}
				}
			}
			// then after the above operation is done, check if list of
			// leftCourses is not empty
			if (!noWday && !unmarkedClasses.isEmpty()) {
				// make a model class for leftover classes and then
				// if not empty, then open an activity containing the left
				// Classes
				Globals.isUnmarkedClass = true;
				Globals.uClasses = unmarkedClasses;
				runDelayedNextActivity(UnmarkedClassListActivity.class);

			} else {
				runDelayedNextActivity(WdayTemplateActivity.class);

			}
			courseDataSource.close();
			wDataSource.close();
		}

		super.onResume();

	}
}
