package com.sidak.yesmam;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.Holiday;

public class CourseView extends ListActivity {
	public static final String TAG = CourseView.class.getSimpleName();
	private CoursesDataSource datasource;
	private List<Holiday> holidays;
	private HolidaysDataSource holiDatasource;
	private Button addCourse;
	private Button finish;
	private List<Course> courses;
	private ListView lv;
	ArrayAdapter<Course> adapter;
	private Course courseAdded;
	private SharedPreferences pref;
	private int[] holidayCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_view);

		lv = (ListView) findViewById(android.R.id.list);
		// initialise add course button
		addCourse = (Button) findViewById(R.id.addCourseText);
		finish = (Button) findViewById(R.id.finishText);
		
		holiDatasource = new HolidaysDataSource(this);
		holiDatasource.open();
		holidays=holiDatasource.findAll();
		countHolidays();
		Log.i(TAG, "after opening holiday databasse");
		
		Log.v(TAG, "in courseview constructor entry pt");
		datasource = new CoursesDataSource(this);
		Log.v(TAG, "after initialsing datasource and before open");
		datasource.open();
		Log.v(TAG, "after open datscr");
		courses = datasource.findAll();
		Log.v(TAG, "in courseview constructor after find all");

		addCourse.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addCourseInDatabase();
			}

		});
		finish.setOnClickListener(new View.OnClickListener() {
				

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(CourseView.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}

		});
		refreshDisplayAndUpdate();
		Log.v(TAG, "after rdau in const");
	}

	private void countHolidays() {
		holidayCount= new int[8];
		for(int i=0; i<holidays.size(); i++){
			holidayCount[i]=0;
		}
		for(int i=0; i<holidays.size(); i++){
			Holiday holiday=holidays.get(i);
			holidayCount[UIHelper.getDayOfWeekFromDate(holiday.toDateObject())]+=1;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) { // for adding courses
			if (resultCode == RESULT_OK) {
				courseAdded = new Course();
				courseAdded.setCourseName(data.getExtras().getString(
						"courseName"));
				courseAdded.setCourseCode(data.getExtras().getString(
						"courseCode"));
				courseAdded.setCourseVenue(data.getExtras().getString(
						"courseVenue"));
				courseAdded.setCourseDesiredAttendance(data.getExtras()
						.getString("reqAttendance"));
				courseAdded.setCourseReqAttendance(data.getExtras().getString(
						"desAttendance"));

				courseAdded.setMonTimings(data.getExtras().getString(
						"monTimings"));
				courseAdded.setTuesTimings(data.getExtras().getString(
						"tuesTimings"));
				courseAdded.setWedTimings(data.getExtras().getString(
						"wedTimings"));
				courseAdded.setThursTimings(data.getExtras().getString(
						"thursTimings"));
				courseAdded.setFriTimings(data.getExtras().getString(
						"friTimings"));
				courseAdded.setAttendClasses(0);
				courseAdded.setBunkClasses(0);
				courseAdded.setTotalClasses(0);
				int totalClasses = calcTotalClasses(courseAdded);
				courseAdded.setTotalClasses(totalClasses);
				Log.v(TAG, "total classes "+totalClasses);
				Log.v(TAG, "in onactivity result" + courseAdded.toString());
				datasource.open();// do it again since onstop was c/d
				datasource.create(courseAdded);

			}
		}
	}

	private int calcTotalClasses(Course course) {
		int sum=0;
		pref = getApplicationContext()
				.getSharedPreferences(getString(R.string.semPrefs), 0);
		String s1 = pref.getString(getString(R.string.dateStart), "1/1/2000");
		String s2 = pref.getString(getString(R.string.dateEnd), "1/1/2000");
		Date olderDate=UIHelper.getDateObjectFromText(s1);
		Date newerDate=UIHelper.getDateObjectFromText(s2);
		if(!course.getMonTimings().equals(getString(R.string.enterDate))){
			sum+=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.MONDAY);
			sum-=holidayCount[Calendar.MONDAY];
		}
		if(!course.getTuesTimings().equals(getString(R.string.enterDate))){
			sum+=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.TUESDAY);
			sum-=holidayCount[Calendar.TUESDAY];
		}
		if(!course.getWedTimings().equals(getString(R.string.enterDate))){
			sum+=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.WEDNESDAY);
			sum-=holidayCount[Calendar.WEDNESDAY];
		}
		if(!course.getThursTimings().equals(getString(R.string.enterDate))){
			sum+=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.THURSDAY);
			sum-=holidayCount[Calendar.THURSDAY];
		}
		if(!course.getFriTimings().equals(getString(R.string.enterDate))){
			sum+=UIHelper.calculateSpecificDay(olderDate, newerDate, Calendar.FRIDAY);
			sum-=holidayCount[Calendar.FRIDAY];
		}
		return sum;
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		holiDatasource.open();
		datasource.open();
		courses = datasource.findAll();
		refreshDisplayAndUpdate();
		Log.v(TAG, "courseview on resume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		holiDatasource.open();
		datasource.open();
		courses = datasource.findAll();
		refreshDisplayAndUpdate();
		Log.v(TAG, "courseview onstart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
		holiDatasource.close();
	}

	private void addCourseInDatabase() {
		Intent intent = new Intent(CourseView.this, AddCourses.class);
		startActivityForResult(intent, 1);
		Log.v(TAG, "adding course in adtabase");
	}

	/*
	 * private void createData() { Holiday holiday1 = new Holiday();
	 * holiday1.setDay(20); holiday1.setMonth(6); holiday1.setYear(2014);
	 * holiday1.setDescription("wej"); holiday1.setType(Holiday.TYPE_INSTI);
	 * datasource.create(holiday1); Log.i(TAG, "after holiday1");
	 * 
	 * Holiday holiday2 = new Holiday(21, 6, 2014, "bday", Holiday.TYPE_INSTI);
	 * datasource.create(holiday2); Log.i(TAG, "after holiday2");
	 * 
	 * Holiday holiday3 = new Holiday(); holiday3.setDay(22);
	 * holiday3.setMonth(6); holiday3.setYear(2014);
	 * holiday3.setDescription("wegrej");
	 * holiday3.setType(Holiday.TYPE_PLANNED); datasource.create(holiday3);
	 * Log.i(TAG, "after holiday3");
	 * 
	 * }
	 */
	
	public void refreshDisplayAndUpdate() {
		Log.v(TAG, "in rdau");
		adapter = new CourseListAdapter(this, courses);
		setListAdapter(adapter);
		Log.v(TAG, "exit rdau");
	}

}
