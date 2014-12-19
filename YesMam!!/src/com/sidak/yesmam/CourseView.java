package com.sidak.yesmam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.Holiday;
import com.sidak.yesmam.model.WorkingDay;

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
	private SharedPreferences pref,coursePref;
	private int[] holidayCount;
	private String s1,s2;
	private Date olderDate,newerDate;
	private int NUM_COURSES;
	private static Map<Integer, String> days= new HashMap<Integer, String>();
	static{
		 days.put(Calendar.MONDAY, "Monday");
		 days.put(Calendar.TUESDAY, "Tuesday");
		 days.put(Calendar.WEDNESDAY, "Wednesday");
		 days.put(Calendar.THURSDAY, "Thursday");
		 days.put(Calendar.FRIDAY, "Friday");
	};

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
		
		pref = getApplicationContext()
				.getSharedPreferences(getString(R.string.semPrefs), 0);
		coursePref=getApplicationContext()
				.getSharedPreferences("course prefs", 0);
		s1 = pref.getString(getString(R.string.dateStart), "1/1/2000");
		s2 = pref.getString(getString(R.string.dateEnd), "1/1/2000");
		olderDate=UIHelper.getDateObjectFromText(s1);
		newerDate=UIHelper.getDateObjectFromText(s2);
		
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
					//DBOpenHelper.setNumCourses(courses.size());
					// populate working days database 
					// do it in a different thread
					// create a spinner till work is done
					NUM_COURSES=courses.size();
					SharedPreferences.Editor e =coursePref.edit();
					e.putInt("num courses", NUM_COURSES);
					e.commit();
					
					WorkingDaysDataSource wdDataSource = new WorkingDaysDataSource(CourseView.this);
					wdDataSource.open();
					
					Calendar c1 = Calendar.getInstance();  
			        c1.setTime(olderDate);  
			   
			        Calendar c2 = Calendar.getInstance();  
			        c2.setTime(newerDate);  
			        //int num = 0;  
			        List<Course> todayCourses;
			        while(c2.after(c1)) {  
			        	int day = c1.get(Calendar.DAY_OF_WEEK);
			            if(!checkIfHoliday(c1) && (day!=1) && ( day!=7)){
			            	WorkingDay wd = new WorkingDay();
			            	wd.setDayString(""+days.get(day));
			            	Date temp=c1.getTime();
			            	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			            	String date = sdf.format(temp); 
			            	wd.setDateString(date);
			            	todayCourses = datasource.getTodaysCourses(day,
			    					getString(R.string.enterDate));
			            	int NUM_CLASSES=todayCourses.size();
			            	if(NUM_CLASSES!=0){
				            	// this list contains codes of the courses which have  a class on that day
				            	ArrayList<String> codes = new ArrayList<String>(NUM_CLASSES);
				            	for(int i=0; i<NUM_CLASSES; i++){
				            		codes.add(todayCourses.get(i).getCourseCode());
				            	}
				            	// wd.codes has codes of all the courses
				            	wd.codes = new ArrayList<String>(NUM_COURSES);
				            	wd.attendance= new ArrayList<Integer>(NUM_COURSES);
				            	for(int i=0; i<NUM_COURSES; i++){
				            		wd.codes.add(courses.get(i).getCourseCode());
				            		wd.attendance.add(3);
				            	}
				            	for(int i=0; i<NUM_COURSES; i++){
				            		// setting the attendance value to be 2 for courses which have classes 
				            		// and 3 for which don't have classes
				            		if(codes.contains(wd.codes.get(i))){
				            			wd.attendance.set(i, 2);
				            		}
				            	}
				            	wdDataSource.create(wd);
			            	}
			            }
			            c1.add(Calendar.DATE,1);  
			        }  
					// try the broadcast thing over here too
					Intent intent = new Intent(CourseView.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					
					wdDataSource.close();
				}

		});
		refreshDisplayAndUpdate();
		Log.v(TAG, "after rdau in const");
		
		
		
	}
	private boolean checkIfHoliday(Calendar c){
		Holiday holiday;
		String s;
		Date d;
		Calendar cal=Calendar.getInstance();
		for(int i=0; i<holidays.size(); i++){
			holiday= holidays.get(i);
			s=holiday.toString();
			d= UIHelper.getDateObjectFromText(s);
			cal.setTime(d);
			if(cal.equals(c)){
				return true;
			}
		}
		return false;
		
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
						.getString("desAttendance"));
				courseAdded.setCourseReqAttendance(data.getExtras().getString(
						"reqAttendance"));

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
