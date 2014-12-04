package com.sidak.yesmam;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.model.Course;

public class CourseView extends ListActivity {
	public static final String TAG = CourseView.class.getSimpleName();
	private CoursesDataSource datasource;
	private Button addCourse;
	private List<Course> courses;
	private ListView lv;
	ArrayAdapter<Course> adapter;
	private Course courseAdded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_view);
		
		lv =(ListView)findViewById(android.R.id.list);
		// initialise add course button
		addCourse = (Button)findViewById(R.id.addCourseText);
		Log.v(TAG, "in courseview constructor entry pt");
		datasource=new CoursesDataSource(this);
		Log.v(TAG, "after initialsing datasource and before open");
		datasource.open();
		Log.v(TAG, "after open datscr");
		courses=datasource.findAll();
		Log.v(TAG, "in courseview constructor after find all");
		
		addCourse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addCourseInDatabase();
			}

			
		});
		refreshDisplayAndUpdate();
		Log.v(TAG, "after rdau in const");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) { // for adding courses
			if (resultCode == RESULT_OK) {
				courseAdded = new Course();
				courseAdded.setCourseName(data.getExtras().getString("courseName"));
				courseAdded.setCourseCode(data.getExtras().getString("courseCode"));
				courseAdded.setCourseVenue(data.getExtras().getString("courseVenue"));
				courseAdded.setCourseDesiredAttendance(data.getExtras().getString("reqAttendance"));
				courseAdded.setCourseReqAttendance(data.getExtras().getString("desAttendance"));
				
				courseAdded.setMonTimings(data.getExtras().getString("monTimings"));
				courseAdded.setTuesTimings(data.getExtras().getString("tuesTimings"));
				courseAdded.setWedTimings(data.getExtras().getString("wedTimings"));
				courseAdded.setThursTimings(data.getExtras().getString("thursTimings"));
				courseAdded.setFriTimings(data.getExtras().getString("friTimings"));
				Log.v(TAG, "in onactivity result"+courseAdded.toString());
				datasource.open();// do it again since onstop was c/d
				datasource.create(courseAdded);

			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
		courses=datasource.findAll();
		refreshDisplayAndUpdate();
		Log.v(TAG, "courseview on resume");
	}
	@Override
	protected void onStart() {
		super.onStart();
		datasource.open();
		courses=datasource.findAll();
		refreshDisplayAndUpdate();
		Log.v(TAG, "courseview onstart");
	}
	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}

	private void addCourseInDatabase() {
		Intent intent = new Intent(CourseView.this, AddCourses.class);
		startActivityForResult(intent, 1);
		Log.v(TAG, "adding course in adtabase");
	}
	
	/*private void createData() {
		Holiday holiday1 = new Holiday();
		holiday1.setDay(20);
		holiday1.setMonth(6);
		holiday1.setYear(2014);
		holiday1.setDescription("wej");
		holiday1.setType(Holiday.TYPE_INSTI);
		datasource.create(holiday1);
		Log.i(TAG, "after holiday1");

		Holiday holiday2 = new Holiday(21, 6, 2014, "bday", Holiday.TYPE_INSTI);
		datasource.create(holiday2);
		Log.i(TAG, "after holiday2");

		Holiday holiday3 = new Holiday();
		holiday3.setDay(22);
		holiday3.setMonth(6);
		holiday3.setYear(2014);
		holiday3.setDescription("wegrej");
		holiday3.setType(Holiday.TYPE_PLANNED);
		datasource.create(holiday3);
		Log.i(TAG, "after holiday3");

	}*/

	public void refreshDisplayAndUpdate() {
		Log.v(TAG, "in rdau");
		adapter = new CourseListAdapter(this, courses);
		setListAdapter(adapter);
		Log.v(TAG, "exit rdau");
	}


}
