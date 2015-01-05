package com.sidak.yesmam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
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
import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.WorkingDay;

public class PastWdayActivity extends ListActivity {

	public final String TAG = PastWdayActivity.class.getSimpleName();
	private List<Course> pastCourses;
	private CoursesDataSource coursesDataSource;
	private WorkingDaysDataSource wDataSource;
	private TextView todayDate;
	private Button attend;
	private Button bunk;
	private Button cancel;
	private ListView lv;
	private Course selectedCourse;
	ArrayAdapter<Course> adapter;

	private int currentDay;
	private String currentDate;
	private TextView viewReqAtten;
	private TextView viewDesAtten;
	private List<Integer> flags;
	private ImageButton log;
	private WorkingDay currentWday;
	private int NUM_COURSES;
	private List<String> todayCodes;
	private int selCodeIdx;
	private int selListIdx;
	private int selAttVal;
	private int prevDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_past_wday);

		// get the current working day from global class
		// if I am opening for the second Time
		Log.i(TAG, "after opening holiday databasse");
		coursesDataSource = new CoursesDataSource(this);
		coursesDataSource.open();
		Log.v(TAG, "after opnening courses database");
		wDataSource = new WorkingDaysDataSource(this);
		wDataSource.open();
		Log.v(TAG, "after opnening working days database");

		lv = (ListView) findViewById(android.R.id.list);// impt to initialise
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// if(view.i)
				// view.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
				
				selectedCourse = pastCourses.get(position);
				selCodeIdx = currentWday.codes.indexOf(selectedCourse.getCourseCode());
				selListIdx=todayCodes.indexOf(selectedCourse.getCourseCode());
				selAttVal = flags.get(selListIdx);
				// if bunk is marked
				if (selAttVal == 0) {
					attend.setEnabled(true);
					bunk.setEnabled(false);
					cancel.setEnabled(true);
				}
				// if attend is marked
				else if (selAttVal == 1) {
					attend.setEnabled(false);
					bunk.setEnabled(true);
					cancel.setEnabled(true);
				}
				// if nothing has been marked
				else if (selAttVal == 2) {
					attend.setEnabled(true);
					bunk.setEnabled(true);
					cancel.setEnabled(true);
				}
				// if cancel is marked
				else if (selAttVal == 4) {
					attend.setEnabled(true);
					bunk.setEnabled(true);
					cancel.setEnabled(false);
				}
				viewDesAtten.setText("Des Atten :"
						+ selectedCourse.getCourseDesiredAttendance() + "%");
				viewReqAtten.setText("Req Atten :"
						+ selectedCourse.getCourseReqAttendance() + "%");

			}

		});

		// lv.setOnFocusChangeListener(l)
		todayDate = (TextView) findViewById(R.id.todayDate);

		log = (ImageButton) findViewById(R.id.log);
		// open the list of previous working days
		log.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PastWdayActivity.this,
						WorkingDayList.class);
				startActivity(i);
			}
		});

		viewDesAtten = (TextView) findViewById(R.id.viewDesAtten);
		viewReqAtten = (TextView) findViewById(R.id.viewReqAtten);

		attend = (Button) findViewById(R.id.attendButton);
		bunk = (Button) findViewById(R.id.bunkButton);
		cancel = (Button) findViewById(R.id.cancelButton);
		attend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if no course has been selected
				if (selectedCourse == null) {
					Toast.makeText(PastWdayActivity.this,
							R.string.selectCourse, Toast.LENGTH_LONG).show();
				} else {
					// if the attendance of that class has not been marked
					// earlier
					if (selAttVal == 2) {
						// increase the NUM_ATTENDED_CLASSES field in course db
						// mark the class with attendance val of 1 , indicating
						// it has been attended
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses() + 1,
								selectedCourse.getCourseCode());

						wDataSource.markAttendance(1, selCodeIdx, currentDate);

						flags.set(selListIdx, 1);
						selAttVal = 1;

						// update wday db containing record of today's date and
						// attendance[i]=1 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						attend.setEnabled(false);

						// if the class selected was marked with bunk earlier
					} else if (selAttVal == 0) {
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses() + 1,
								selectedCourse.getCourseCode());
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses()-1,
								selectedCourse.getCourseCode());
						// update wday db containing record of today's date and
						// attendance[i]=1 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						wDataSource.markAttendance(1, selCodeIdx, currentDate);
						flags.set(selListIdx, 1);
						selAttVal = 1;
						attend.setEnabled(false);
						bunk.setEnabled(true);
					} else if (selAttVal == 4) {
						// inc the num of classes by one
						// inc the num of attended classes by one
						coursesDataSource.changeNumClasses(selectedCourse.getTotalClasses()+1, selectedCourse.getCourseCode());
						coursesDataSource.markPresent(selectedCourse.getAttendedClasses()+1, selectedCourse.getCourseCode());
						
						wDataSource.markAttendance(1, selCodeIdx, currentDate);
						flags.set(selListIdx, 1);
						selAttVal = 1;
						attend.setEnabled(false);
						cancel.setEnabled(true);
					}
					updateCourses();
				}
			}
		});
		bunk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selectedCourse == null) {
					Toast.makeText(PastWdayActivity.this,
							R.string.selectCourse, Toast.LENGTH_LONG).show();
				} else {
					if (selAttVal == 2) {
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses() + 1,
								selectedCourse.getCourseCode());
						wDataSource.markAttendance(0, selCodeIdx, currentDate);
						flags.set(selListIdx, 0);
						selAttVal = 0;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						bunk.setEnabled(false);
					} else if (selAttVal == 1) {
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses() + 1,
								selectedCourse.getCourseCode());

						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses()-1,
								selectedCourse.getCourseCode());

						wDataSource.markAttendance(0, selCodeIdx, currentDate);
						flags.set(selListIdx, 0);
						selAttVal = 0;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						attend.setEnabled(true);
						bunk.setEnabled(false);
					}
				 else if (selAttVal == 4) {
					// inc the num of classes by one
					// inc the num of bunk classes by one
					coursesDataSource.changeNumClasses(selectedCourse.getTotalClasses()+1, selectedCourse.getCourseCode());
					coursesDataSource.markAbsent(selectedCourse.getBunkedClasses()+1, selectedCourse.getCourseCode());
					
					wDataSource.markAttendance(0, selCodeIdx, currentDate);
					flags.set(selListIdx, 0);
					selAttVal = 0;
					bunk.setEnabled(false);
					cancel.setEnabled(true);
				}
					updateCourses();
				}
			}
			
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (selectedCourse == null) {
					Toast.makeText(PastWdayActivity.this,
							R.string.selectCourse, Toast.LENGTH_LONG).show();
				} else {
					String code = selectedCourse.getCourseCode();
					if (selAttVal == 2) {
						// dec the num of classes  by 1 
						coursesDataSource.changeNumClasses(selectedCourse.getTotalClasses()-1, code);
						
						
						wDataSource.markAttendance(4, selCodeIdx, currentDate);
						flags.set(selListIdx, 4);
						selAttVal = 4;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						cancel.setEnabled(false);
					} else if (selAttVal == 0) {
						coursesDataSource.changeNumClasses(selectedCourse.getTotalClasses()-1, code);
						
						coursesDataSource.markAbsent(
								selectedCourse.getBunkedClasses()- 1,
								selectedCourse.getCourseCode());

					

						wDataSource.markAttendance(4, selCodeIdx, currentDate);
						flags.set(selListIdx, 4);
						selAttVal = 4;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						bunk.setEnabled(true);
						cancel.setEnabled(false);
					}
					else if (selAttVal == 1) {
						coursesDataSource.changeNumClasses(selectedCourse.getTotalClasses()-1, code);
						
						coursesDataSource.markPresent(
								selectedCourse.getAttendedClasses()- 1,
								selectedCourse.getCourseCode());

					

						wDataSource.markAttendance(4, selCodeIdx, currentDate);
						flags.set(selListIdx, 4);
						selAttVal = 4;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						attend.setEnabled(true);
						cancel.setEnabled(false);
					}
					updateCourses();
				}
			}
			
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		coursesDataSource.open();
		wDataSource.open();

		// just refer the wday , referred by the workingListActivity
		currentWday = Globals.workday;
		
		// sets the date of this activity as the past working day's date
		setDateWday();

		Date current = UIHelper.getDateObjectFromText(currentDate);
		Calendar c = Calendar.getInstance();
		c.setTime(current);
		prevDay=c.get(Calendar.DAY_OF_WEEK);
		// cache today's courses
		currentDay = UIHelper.getDayOfWeekFromDate(current);
		// load the data only after a day
		pastCourses = coursesDataSource.getTodaysCourses(currentDay,
				getString(R.string.enterDate));

		todayCodes = new ArrayList<String>();
		flags = new ArrayList<Integer>();
		NUM_COURSES = currentWday.codes.size();
		for (int i = 0; i < NUM_COURSES; i++) {
			// if the courses's attendance is marked by 3, it means there is
			// no class for that course
			// today , so don't add it
			if (currentWday.attendance.get(i) != 3) {
				todayCodes.add(currentWday.codes.get(i));
				flags.add(currentWday.attendance.get(i));
			}
		}

		if (currentDay == Calendar.MONDAY) {
			Collections.sort(pastCourses, new Comparator<Course>() {
				public int compare(Course a, Course b) {
					Date d1, d2;

					d1 = UIHelper.getDateObjectFromTextTime(a.getMonTimings());
					d2 = UIHelper.getDateObjectFromTextTime(b.getMonTimings());
					return d1.compareTo(d2);
				}
			});
		} else if (currentDay == Calendar.TUESDAY) {
			Collections.sort(pastCourses, new Comparator<Course>() {
				public int compare(Course a, Course b) {
					Date d1, d2;
					d1 = UIHelper.getDateObjectFromTextTime(a.getTuesTimings());
					d2 = UIHelper.getDateObjectFromTextTime(b.getTuesTimings());
					return d1.compareTo(d2);
				}
			});
		} else if (currentDay == Calendar.WEDNESDAY) {
			Collections.sort(pastCourses, new Comparator<Course>() {
				public int compare(Course a, Course b) {
					Date d1, d2;
					d1 = UIHelper.getDateObjectFromTextTime(a.getWedTimings());
					d2 = UIHelper.getDateObjectFromTextTime(b.getWedTimings());
					return d1.compareTo(d2);
				}
			});
		} else if (currentDay == Calendar.THURSDAY) {
			Collections.sort(pastCourses, new Comparator<Course>() {
				public int compare(Course a, Course b) {
					Date d1, d2;
					d1 = UIHelper.getDateObjectFromTextTime(a.getThursTimings());
					d2 = UIHelper.getDateObjectFromTextTime(b.getThursTimings());
					return d1.compareTo(d2);
				}
			});
		} else if (currentDay == Calendar.FRIDAY) {
			Collections.sort(pastCourses, new Comparator<Course>() {
				public int compare(Course a, Course b) {
					Date d1, d2;
					d1 = UIHelper.getDateObjectFromTextTime(a.getFriTimings());
					d2 = UIHelper.getDateObjectFromTextTime(b.getFriTimings());
					return d1.compareTo(d2);
				}
			});
		}

		Log.v(TAG, "in onresume " + pastCourses.size());
		adapter = new ClassListAdapter(this, pastCourses,prevDay);
		setListAdapter(adapter);
	}

	private void setDateWday() {
		currentDate = currentWday.getDateString();
		todayDate.setText(currentDate);

	}

	@Override
	protected void onPause() {
		super.onPause();
		wDataSource.close();
		coursesDataSource.close();
	}
	private void updateCourses(){
		pastCourses = coursesDataSource.getTodaysCourses(currentDay,
				getString(R.string.enterDate));
		adapter = new ClassListAdapter(this, pastCourses,prevDay);
		setListAdapter(adapter);
	}
}
