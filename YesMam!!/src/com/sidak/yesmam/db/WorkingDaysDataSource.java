package com.sidak.yesmam.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.WorkingDay;

public class WorkingDaysDataSource {
	public static int NUM_COURSES;
	public static final String TAG = WorkingDaysDataSource.class.getSimpleName();
	private SharedPreferences coursePref;
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	private static  String[] allColumns; 
	public WorkingDaysDataSource(Context context) {
		coursePref=context
				.getSharedPreferences("course prefs", 0);
		NUM_COURSES=coursePref.getInt("num courses", 0);
		//NUM_COURSES=n;
		WDBOpenHelper.setNumCourses(NUM_COURSES);
		dbhelper = new WDBOpenHelper(context);
		
		allColumns= new String[3+2*NUM_COURSES];
		allColumns[0]=WDBOpenHelper.WDAY_ID;
		allColumns[1]=WDBOpenHelper.WDAY_DATE;
		allColumns[2]=WDBOpenHelper.WDAY_DAY;
		Log.v(TAG, "constructor course data src");
	}

	public void open() {
		Log.i(TAG, "Course Database opened");
		database = dbhelper.getWritableDatabase();
			
		for(int i=0; i<NUM_COURSES;i++){
			allColumns[3+i]=WDBOpenHelper.CODES[i];
		}
		for(int i=0; i<NUM_COURSES;i++){
			allColumns[3+NUM_COURSES+i]=WDBOpenHelper.ATTENDANCE[i];
		}
	}

	public void close() {
		Log.i(TAG, "Database closed");
		dbhelper.close();
	}

	public void create(WorkingDay w) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(WDBOpenHelper.WDAY_DATE, w.getDateString());
		values.put(WDBOpenHelper.WDAY_DAY, w.getDayString());
		for(int i=0; i<NUM_COURSES;  i++){
			values.put(WDBOpenHelper.CODES[i],w.codes.get(i));
		}
		for(int i=0; i<NUM_COURSES;  i++){
			values.put(WDBOpenHelper.ATTENDANCE[i],w.attendance.get(i));
		}
		long insertid = database.insert(WDBOpenHelper.TABLE_WDAYS, null,
				values);
		Log.i(TAG, "in create in datasrc " + insertid);
	}

	public List<WorkingDay> findAll() {

		Cursor cursor = database.query(WDBOpenHelper.TABLE_WDAYS, allColumns,
				null, null, null, null, null);

		Log.i(TAG, "Returned " + cursor.getCount() + " rows");
		List<WorkingDay> w = cursorToList(cursor);
		return w;
	}

	private List<WorkingDay> cursorToList(Cursor cursor) {
		List<WorkingDay> wdays = new ArrayList<WorkingDay>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				WorkingDay wday = new WorkingDay();
				wday.setDateString(cursor.getString(cursor
						.getColumnIndex(WDBOpenHelper.WDAY_DATE)));
				wday.setDayString(cursor.getString(cursor
						.getColumnIndex(WDBOpenHelper.WDAY_DAY)));
				wday.codes = new ArrayList<String>(NUM_COURSES);
				for(int i=0; i<NUM_COURSES;  i++){
					wday.codes.add(cursor.getString(cursor.getColumnIndex(WDBOpenHelper.CODES[i])));
				}
				wday.attendance=new ArrayList<Integer>(NUM_COURSES);
				for(int i=0; i<NUM_COURSES;  i++){
					wday.attendance.add(cursor.getInt(cursor.getColumnIndex(WDBOpenHelper.ATTENDANCE[i])));
				}
				
				wdays.add(wday);
				Log.v(TAG, wday.toString());

			}
		}
		return wdays;
	}
	
	public void markAttendance(int att, int i,String date) {

		ContentValues values = new ContentValues();
		values.put(WDBOpenHelper.ATTENDANCE[i],att );
		// updating row
		database.update(WDBOpenHelper.TABLE_WDAYS, values,WDBOpenHelper.WDAY_DATE  + " = ?",
				new String[] {date});
		Log.v(TAG, date+ " " +att);
	}

	public List<WorkingDay> findCurrent(String date) {
		Cursor cursor = database.query(WDBOpenHelper.TABLE_WDAYS, allColumns,
				WDBOpenHelper.WDAY_DATE + "=?", new String[]{date}, null, null, null);

		Log.i(TAG, "Returned " + cursor.getCount() + " rows");
		List<WorkingDay> w = cursorToList(cursor);
		return w;
	}



}