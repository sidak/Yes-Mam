package com.sidak.yesmam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.Holiday;
import com.sidak.yesmam.model.WorkingDay;

public class WdayTemplateActivity extends ListActivity {

	public final String TAG = WdayTemplateActivity.class.getSimpleName();
	private SharedPreferences prefs;
	private int numWorkingDays;
	private Calendar c;
	private List<Holiday> holidays;
	private List<Course> todayCourses;
	private HolidaysDataSource holiDatasource;
	private CoursesDataSource coursesDataSource;
	private WorkingDaysDataSource wDataSource;
	private String holidayType;
	private String holidayDesc;
	private TextView todayDate;
	private Button attend;
	private Button bunk;
	private Button proxy;
	private ListView lv;
	private Course selectedCourse;
	ArrayAdapter<Course> adapter;
	private TextView tillNow;
	private TextView ifAttend;

	private TextView ifMiss;
	private boolean attenFlag = false;
	private boolean bunkFlag = false;
	private int currentDay;
	private boolean ifHoliday;
	private String currentDate;
	private TextView viewClassesLeft;
	private TextView viewReqAtten;
	private TextView viewDesAtten;
	private TextView viewMsg;
	private List<Integer> flags;
	private ImageButton log;
	private WorkingDay currentWday;
	private int NUM_CLASSES, NUM_COURSES;
	private List<String> todayCodes;
	private int selCodeIdx;
	private int selAttVal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		prefs = getApplicationContext().getSharedPreferences(
				getString(R.string.semPrefs), 0);
		numWorkingDays = prefs.getInt(getString(R.string.numWorkingDays), 0);
		Log.v(TAG, "" + numWorkingDays);
		*/
		// get the current working day from global class
		currentWday = Globals.workday;
		lv = (ListView) findViewById(android.R.id.list);// impt to initialise
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if(view.i)
				// view.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));

				selectedCourse = todayCourses.get(position);
				selCodeIdx= todayCodes.indexOf(selectedCourse.getCourseCode());
				selAttVal=flags.get(selCodeIdx);
				if(selAttVal==0){
					attend.setEnabled(true);
					bunk.setEnabled(false);
				}
				else if (selAttVal==1){
					attend.setEnabled(false);
					bunk.setEnabled(true);
				}
				else if (selAttVal==2){
					attend.setEnabled(true);
					bunk.setEnabled(true);
				}
				
				int total, attend, bunk, left;
				total = selectedCourse.getTotalClasses();
				attend = selectedCourse.getAttendedClasses();
				bunk = selectedCourse.getBunkedClasses();
				left = total - attend - bunk;
				double tillNowVal = (double) attend * 100.0 / (attend + bunk);

				double ifAttendVal = (double) (attend + 1) * 100.0
						/ (attend + 1 + bunk);
				double ifMissVal = (double) (attend) * 100.0
						/ (attend + 1 + bunk);
				if (Double.isNaN(tillNowVal)) {
					tillNow.setText(getString(R.string.defaultAttenVal));
				} else {

					tillNow.setText(String.format("%.2f", tillNowVal) + "%");

				}
				ifAttend.setText(String.format("%.2f", ifAttendVal) + "%");
				ifMiss.setText(String.format("%.2f", ifMissVal) + "%");

				viewDesAtten.setText("Des Atten :"
						+ selectedCourse.getCourseDesiredAttendance() + "%");
				viewReqAtten.setText("Req Atten :"
						+ selectedCourse.getCourseReqAttendance() + "%");
				viewClassesLeft.setText("Classes Left :" + (left));

				double goEvery = (double) (attend + left) * 100.0 / total;
				double goNone = (double) (attend * 100.0) / total;
				double des = Double.parseDouble(selectedCourse
						.getCourseDesiredAttendance());
				double req = Double.parseDouble(selectedCourse
						.getCourseReqAttendance());
				int classesReq = (int) Math.ceil((req * total) / 100.0);
				int classesDes = (int) Math.ceil((des * total) / 100.0);
				int moreReqClasses = classesReq - attend;
				int moreDesClasses = classesDes - attend;
				//TODO: display desired classes number
				if (goNone >= des) {
					viewMsg.setText("Congrats for achieving your goal, you may not go to class from now");
				} else if (goNone < des) {
					if (goNone >= req) {
						if (goEvery < des) {
							viewMsg.setText("In SAFE ZONE but you won't get the desired attendance");
						} else if (goEvery >= des) {
							viewMsg.setText("In SAFE ZONE and you can achieve your goal");
						}
					} else {
						if (goEvery < req) {
							viewMsg.setText("Sorry, but you will get an attendance back ");
						} else if (goEvery >= req) {
							if (left == moreReqClasses) {
								viewMsg.setText("In DANGER ZONE and don't skip even a single class");
							} else if ((left - moreReqClasses) <= 2) {
								viewMsg.setText("In DANGER ZONE , skip the class if very important");
							} else if (moreReqClasses > (left / 2)) {
								viewMsg.setText("In DANGER ZONE , think once more before skipping");
							} else {
								viewMsg.setText("In DANGER ZONE , but you can afford to skip a class");

							}
						}
					}
				}
			}

		});

		holiDatasource = new HolidaysDataSource(this);
		holiDatasource.open();
		Log.i(TAG, "after opening holiday databasse");
		coursesDataSource = new CoursesDataSource(this);
		coursesDataSource.open();
		Log.v(TAG, "after opnening courses database");
		wDataSource = new WorkingDaysDataSource(this);
		wDataSource.open();
		Log.v(TAG, "after opnening working days database");
		todayDate = (TextView) findViewById(R.id.todayDate);
		tillNow = (TextView) findViewById(R.id.tillNow);
		ifAttend = (TextView) findViewById(R.id.ifAttend);
		ifMiss = (TextView) findViewById(R.id.ifMiss);
		log = (ImageButton) findViewById(R.id.log);
		log.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(WdayTemplateActivity.this, WorkingDayList.class);
				startActivity(i);
			}
		});
		viewClassesLeft = (TextView) findViewById(R.id.viewClassesLeft);
		viewDesAtten = (TextView) findViewById(R.id.viewDesAtten);
		viewReqAtten = (TextView) findViewById(R.id.viewReqAtten);
		viewMsg = (TextView) findViewById(R.id.viewMsg);

		attend = (Button) findViewById(R.id.attendButton);
		bunk = (Button) findViewById(R.id.bunkButton);
		proxy = (Button) findViewById(R.id.proxyButton);
		attend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(selectedCourse==null){
					Toast.makeText(WdayTemplateActivity.this,
							R.string.selectCourse, Toast.LENGTH_LONG)
							.show();
				}
				else{
					if(selAttVal==2){
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses() + 1,
								selectedCourse.getCourseCode());
						
						wDataSource.markAttendance(1, selCodeIdx, currentDate);
						flags.set(selCodeIdx, 1);
						selAttVal=1;
						// update wday db containing record of today's date and attendance[i]=1 where i is index of selected course code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						attend.setEnabled(false);
					}
					else if (selAttVal==0){
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses() + 1,
								selectedCourse.getCourseCode());
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses(),
								selectedCourse.getCourseCode());
						// update wday db containing record of today's date and attendance[i]=1 where i is index of selected course code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						wDataSource.markAttendance(1, selCodeIdx, currentDate);
						flags.set(selCodeIdx, 1);
						selAttVal=1;
						attend.setEnabled(false);
						bunk.setEnabled(true);
					}
				}
			}
		});
		bunk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(selectedCourse==null){
					Toast.makeText(WdayTemplateActivity.this,
							R.string.selectCourse, Toast.LENGTH_LONG)
							.show();
				}
				else{
					if(selAttVal==2){
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses() + 1,
								selectedCourse.getCourseCode());
						wDataSource.markAttendance(0, selCodeIdx, currentDate);
						flags.set(selCodeIdx, 0);
						selAttVal=0;
						// update wday db containing record of today's date and attendance[i]=0 where i is index of selected course code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						bunk.setEnabled(false);
					}
					else if (selAttVal==1){
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses() + 1,
								selectedCourse.getCourseCode());
						
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses(),
								selectedCourse.getCourseCode());
						wDataSource.markAttendance(0, selCodeIdx, currentDate);
						flags.set(selCodeIdx, 0);
						selAttVal=0;
						// update wday db containing record of today's date and attendance[i]=0 where i is index of selected course code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						attend.setEnabled(true);
						bunk.setEnabled(false);
					}
				}
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
		currentDate=currentWday.getDateString();
		todayDate.setText(currentDate);
	}

	protected void getHelp() {
		// TODO Auto-generated method stub

	}

	private boolean checkHoliday() {
		int date[] = UIHelper.getDateFromText(todayDate.getText().toString(), this);
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

	@Override
	protected void onResume() {
		super.onResume();
		currentDate=currentWday.getDateString();
		todayDate.setText(currentDate);
		
		holiDatasource.open();
		coursesDataSource.open();
		wDataSource.open();
		Date current = UIHelper.getDateObjectFromText(todayDate.getText()
				.toString());
		if (checkHoliday()) {
			// change in ui to show today's classes
			// cache the result

		} else if (UIHelper.checkIfWeekend(todayDate.getText().toString())) {
		
		} else {
			// cache today's courses
			currentDay = UIHelper.getDayOfWeekFromDate(current);
			todayCourses = coursesDataSource.getTodaysCourses(currentDay,
					getString(R.string.enterDate));
			
			todayCodes=new ArrayList<String>();
			flags = new ArrayList<Integer>();
			
			NUM_COURSES=currentWday.codes.size();
			for(int i=0;i<NUM_COURSES; i++){
				// if the courses's attendance is marked by 3, it means there is no class for that course
				// today , so don't add it 
				if(currentWday.attendance.get(i)!=3){
					todayCodes.add(currentWday.codes.get(i));
					flags.add(currentWday.attendance.get(i)); 
				}
			}
			NUM_CLASSES=todayCourses.size();

			if (currentDay == Calendar.MONDAY) {
				Collections.sort(todayCourses, new Comparator<Course>() {
					public int compare(Course a, Course b) {
						Date d1, d2;

						d1 = UIHelper.getDateObjectFromTextTime(a
								.getMonTimings());
						d2 = UIHelper.getDateObjectFromTextTime(b
								.getMonTimings());
						return d1.compareTo(d2);
					}
				});
			} else if (currentDay == Calendar.TUESDAY) {
				Collections.sort(todayCourses, new Comparator<Course>() {
					public int compare(Course a, Course b) {
						Date d1, d2;
						d1 = UIHelper.getDateObjectFromTextTime(a
								.getTuesTimings());
						d2 = UIHelper.getDateObjectFromTextTime(b
								.getTuesTimings());
						return d1.compareTo(d2);
					}
				});
			} else if (currentDay == Calendar.WEDNESDAY) {
				Collections.sort(todayCourses, new Comparator<Course>() {
					public int compare(Course a, Course b) {
						Date d1, d2;
						d1 = UIHelper.getDateObjectFromTextTime(a
								.getWedTimings());
						d2 = UIHelper.getDateObjectFromTextTime(b
								.getWedTimings());
						return d1.compareTo(d2);
					}
				});
			} else if (currentDay == Calendar.THURSDAY) {
				Collections.sort(todayCourses, new Comparator<Course>() {
					public int compare(Course a, Course b) {
						Date d1, d2;
						d1 = UIHelper.getDateObjectFromTextTime(a
								.getThursTimings());
						d2 = UIHelper.getDateObjectFromTextTime(b
								.getThursTimings());
						return d1.compareTo(d2);
					}
				});
			} else if (currentDay == Calendar.FRIDAY) {
				Collections.sort(todayCourses, new Comparator<Course>() {
					public int compare(Course a, Course b) {
						Date d1, d2;
						d1 = UIHelper.getDateObjectFromTextTime(a
								.getFriTimings());
						d2 = UIHelper.getDateObjectFromTextTime(b
								.getFriTimings());
						return d1.compareTo(d2);
					}
				});
			}
			Log.v(TAG, "in onresume " + todayCourses.size());
			adapter = new ClassListAdapter(this, todayCourses);
			setListAdapter(adapter);
			// if a course is added again after pressing back
			// filter working days which have no classes
			// check that on a part day not more than two attendance possible
			// save attendance val for each course
			// display when no classes or holiday or sat
			// set selection for listview based on index closest to current time
			// set the status
			// cache todays' classes
			// show notifications for classes whose attendance has not been done
			// make the notification sticky
			// longpress to edit a particular class details
			// editing other details
			// in danger or safe zone
			// ui improvements
			// upload app on playstore
			// about

			// NEXT VERSION
			// proxy
			// tutorials, pracs
			// extra class
			// sat , sun enable
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		wDataSource.close();
		coursesDataSource.close();
		holiDatasource.close();
	}

}
