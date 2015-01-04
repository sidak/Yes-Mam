package com.sidak.yesmam;

import java.util.ArrayList;
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
import android.widget.Toast;

import com.sidak.yesmam.db.CoursesDataSource;
import com.sidak.yesmam.db.WorkingDaysDataSource;
import com.sidak.yesmam.model.UnmarkedClass;
import com.sidak.yesmam.model.WorkingDay;

public class UnmarkedClassListActivity extends ListActivity {

	public final String TAG = UnmarkedClassListActivity.class.getSimpleName();
	private List<UnmarkedClass> uClasses;
	private CoursesDataSource coursesDataSource;
	private WorkingDaysDataSource wDataSource;
	private Button attend;
	private Button bunk;
	private Button cancel;
	private Button update;
	private ListView lv;
	private UnmarkedClass selectedUClass;
	ArrayAdapter<UnmarkedClass> adapter;

	private List<Integer> flags;
	private ImageButton log;
	private int NUM_UNMARKED_CLASSES;
	private List<String> uClassCodes;
	private int uClassIndex;
	private int selAttVal;
	private int selCodeIdx;
	private WorkingDay currentWday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_unmarked_class_list);

		Log.i(TAG, "after opening holiday databasse");
		coursesDataSource = new CoursesDataSource(this);
		coursesDataSource.open();
		Log.v(TAG, "after opnening courses database");
		wDataSource = new WorkingDaysDataSource(this);
		wDataSource.open();
		Log.v(TAG, "after opnening working days database");
		currentWday= wDataSource.findCurrent(UIHelper.getCurrentDate()).get(0);
		
		lv = (ListView) findViewById(android.R.id.list);// impt to initialise
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				selectedUClass = uClasses.get(position);
				uClassIndex = position;
				// index of the course among the list of courses tracked by the working days database
				selCodeIdx=currentWday.codes.indexOf(selectedUClass.getCourse().getCourseCode());
				selAttVal = flags.get(uClassIndex);
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

			}

		});

		log = (ImageButton) findViewById(R.id.log);
		// open the list of previous working days
		log.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UnmarkedClassListActivity.this,
						WorkingDayList.class);
				startActivity(i);
			}
		});

		attend = (Button) findViewById(R.id.attendButton);
		bunk = (Button) findViewById(R.id.bunkButton);
		cancel = (Button) findViewById(R.id.cancelButton);
		update = (Button) findViewById(R.id.updateButton);
		
		// the index to be used in markAttendance() method of wDataSource should 
		// be the index of that course in the list of courses that are put into
		// the working days database 
		attend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if no course has been selected
				if (selectedUClass == null) {
					Toast.makeText(UnmarkedClassListActivity.this,
							R.string.selectUClass, Toast.LENGTH_LONG).show();
				} else {
					// if the attendance of that class has not been marked
					// earlier
					if (selAttVal == 2) {
						// increase the NUM_ATTENDED_CLASSES field in course db
						// mark the class with attendance val of 1 , indicating
						// it has been attended
						coursesDataSource.markPresent(selectedUClass
								.getCourse().getAttendedClasses() + 1,
								selectedUClass.getCourse().getCourseCode());

						wDataSource.markAttendance(1, selCodeIdx, selectedUClass.getDate());

						flags.set(uClassIndex, 1);
						selAttVal = 1;

						// update wday db containing record of today's date and
						// attendance[i]=1 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						attend.setEnabled(false);

						// if the class selected was marked with bunk earlier
					} else if (selAttVal == 0) {
						coursesDataSource.markPresent(selectedUClass
								.getCourse().getAttendedClasses() + 1,
								selectedUClass.getCourse().getCourseCode());
						coursesDataSource.markAbsent(selectedUClass.getCourse()
								.getBunkedClasses() - 1, selectedUClass
								.getCourse().getCourseCode());
						// update wday db containing record of today's date and
						// attendance[i]=1 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=1
						// update selAttVal=1;
						wDataSource.markAttendance(1, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 1);
						selAttVal = 1;
						attend.setEnabled(false);
						bunk.setEnabled(true);
					} else if (selAttVal == 4) {
						// inc the num of classes by one
						// inc the num of attended classes by one
						coursesDataSource.changeNumClasses(selectedUClass
								.getCourse().getTotalClasses() + 1,
								selectedUClass.getCourse().getCourseCode());
						coursesDataSource.markPresent(selectedUClass
								.getCourse().getAttendedClasses() + 1,
								selectedUClass.getCourse().getCourseCode());

						wDataSource.markAttendance(1, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 1);
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
				if (selectedUClass == null) {
					Toast.makeText(UnmarkedClassListActivity.this,
							R.string.selectUClass, Toast.LENGTH_LONG).show();
				} else {
					if (selAttVal == 2) {
						coursesDataSource.markAbsent(selectedUClass.getCourse()
								.getBunkedClasses() + 1, selectedUClass
								.getCourse().getCourseCode());
						wDataSource.markAttendance(0, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 0);
						selAttVal = 0;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						bunk.setEnabled(false);
					} else if (selAttVal == 1) {
						coursesDataSource.markAbsent(selectedUClass.getCourse()
								.getBunkedClasses() + 1, selectedUClass
								.getCourse().getCourseCode());

						coursesDataSource.markPresent(selectedUClass
								.getCourse().getAttendedClasses() - 1,
								selectedUClass.getCourse().getCourseCode());

						wDataSource.markAttendance(0, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 0);
						selAttVal = 0;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						attend.setEnabled(true);
						bunk.setEnabled(false);
					} else if (selAttVal == 4) {
						// inc the num of classes by one
						// inc the num of bunk classes by one
						coursesDataSource.changeNumClasses(selectedUClass
								.getCourse().getTotalClasses() + 1,
								selectedUClass.getCourse().getCourseCode());
						coursesDataSource.markAbsent(selectedUClass.getCourse()
								.getBunkedClasses() + 1, selectedUClass
								.getCourse().getCourseCode());

						wDataSource.markAttendance(0, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 0);
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
				if (selectedUClass == null) {
					Toast.makeText(UnmarkedClassListActivity.this,
							R.string.selectUClass, Toast.LENGTH_LONG).show();
				} else {
					String code = selectedUClass.getCourse().getCourseCode();
					if (selAttVal == 2) {
						// dec the num of classes by 1
						coursesDataSource.changeNumClasses(selectedUClass
								.getCourse().getTotalClasses() - 1, code);

						wDataSource.markAttendance(4, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 4);
						selAttVal = 4;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						cancel.setEnabled(false);
					} else if (selAttVal == 0) {
						coursesDataSource.changeNumClasses(selectedUClass
								.getCourse().getTotalClasses() - 1, code);

						coursesDataSource.markAbsent(selectedUClass.getCourse()
								.getBunkedClasses() - 1, selectedUClass
								.getCourse().getCourseCode());

						wDataSource.markAttendance(4, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 4);
						selAttVal = 4;
						// update wday db containing record of today's date and
						// attendance[i]=0 where i is index of selected course
						// code
						// update local copy i.e. flags(i)=0
						// update selAttVal=0;
						bunk.setEnabled(true);
						cancel.setEnabled(false);
					} else if (selAttVal == 1) {
						coursesDataSource.changeNumClasses(selectedUClass
								.getCourse().getTotalClasses() - 1, code);

						coursesDataSource.markPresent(selectedUClass
								.getCourse().getAttendedClasses() - 1,
								selectedUClass.getCourse().getCourseCode());

						wDataSource.markAttendance(4, selCodeIdx, selectedUClass.getDate());
						flags.set(uClassIndex, 4);
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
		update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UnmarkedClassListActivity.this, WdayTemplateActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		coursesDataSource.open();
		wDataSource.open();

		// cache today's courses
		// currentDay = UIHelper.getDayOfWeekFromDate(current);
		uClasses = Globals.uClasses;

		uClassCodes = new ArrayList<String>();
		flags = new ArrayList<Integer>();

		NUM_UNMARKED_CLASSES = uClasses.size();
		for (int i = 0; i < NUM_UNMARKED_CLASSES; i++) {

			uClassCodes.add(uClasses.get(i).getCourse().getCourseCode());
			flags.add(uClasses.get(i).getAttenVal());

		}
		/*
		 * if (currentDay == Calendar.MONDAY) { Collections.sort(uClasses, new
		 * Comparator<UnmarkedClass>() { public int compare(UnmarkedClass a,
		 * UnmarkedClass b) { Date d1, d2;
		 * 
		 * d1 = UIHelper.getDateObjectFromTextTime(a.getCourse()
		 * .getMonTimings()); d2 =
		 * UIHelper.getDateObjectFromTextTime(b.getCourse() .getMonTimings());
		 * return d1.compareTo(d2); } }); } else if (currentDay ==
		 * Calendar.TUESDAY) { Collections.sort(uClasses, new
		 * Comparator<UnmarkedClass>() { public int compare(UnmarkedClass a,
		 * UnmarkedClass b) { Date d1, d2; d1 =
		 * UIHelper.getDateObjectFromTextTime(a.getCourse() .getTuesTimings());
		 * d2 = UIHelper.getDateObjectFromTextTime(b.getCourse()
		 * .getTuesTimings()); return d1.compareTo(d2); } }); } else if
		 * (currentDay == Calendar.WEDNESDAY) { Collections.sort(uClasses, new
		 * Comparator<UnmarkedClass>() { public int compare(UnmarkedClass a,
		 * UnmarkedClass b) { Date d1, d2; d1 =
		 * UIHelper.getDateObjectFromTextTime(a.getCourse() .getWedTimings());
		 * d2 = UIHelper.getDateObjectFromTextTime(b.getCourse()
		 * .getWedTimings()); return d1.compareTo(d2); } }); } else if
		 * (currentDay == Calendar.THURSDAY) { Collections.sort(uClasses, new
		 * Comparator<UnmarkedClass>() { public int compare(UnmarkedClass a,
		 * UnmarkedClass b) { Date d1, d2; d1 =
		 * UIHelper.getDateObjectFromTextTime(a.getCourse() .getThursTimings());
		 * d2 = UIHelper.getDateObjectFromTextTime(b.getCourse()
		 * .getThursTimings()); return d1.compareTo(d2); } }); } else if
		 * (currentDay == Calendar.FRIDAY) { Collections.sort(uClasses, new
		 * Comparator<UnmarkedClass>() { public int compare(UnmarkedClass a,
		 * UnmarkedClass b) { Date d1, d2; d1 =
		 * UIHelper.getDateObjectFromTextTime(a.getCourse() .getFriTimings());
		 * d2 = UIHelper.getDateObjectFromTextTime(b.getCourse()
		 * .getFriTimings()); return d1.compareTo(d2); } }); }
		 */
		Log.v(TAG, "in onresume " + uClasses.size());
		adapter = new UnmarkedClassAdapter(this, uClasses);
		setListAdapter(adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		wDataSource.close();
		coursesDataSource.close();
	}

	private void updateCourses() {
		uClasses = Globals.uClasses;
		adapter = new UnmarkedClassAdapter(this, uClasses);
		setListAdapter(adapter);
	}
}
