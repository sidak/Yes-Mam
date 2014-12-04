package com.sidak.yesmam.db;

import java.util.ArrayList;
import java.util.List;

import com.sidak.yesmam.model.Course;
import com.sidak.yesmam.model.Holiday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CoursesDataSource {

	public static final String TAG = CoursesDataSource.class.getSimpleName();

	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	private static final String[] allColumns = { DBOpenHelper.COURSE_ID,
			DBOpenHelper.COURSE_NAME, DBOpenHelper.COURSE__CODE,
			DBOpenHelper.COURSE_VENUE, DBOpenHelper.COURSE_REQ_ATTEND,
			DBOpenHelper.COURSE_DES_ATTEND, DBOpenHelper.MON_TIMINGS,
			DBOpenHelper.TUES_TIMINGS, DBOpenHelper.WED_TIMINGS,
			DBOpenHelper.THURS_TIMINGS, DBOpenHelper.FRI_TIMINGS

	};

	public CoursesDataSource(Context context) {
		dbhelper = new DBOpenHelper(context);
		Log.v(TAG, "constructor course data src");
	}

	public void open() {
		Log.i(TAG, "Course Database opened");
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		Log.i(TAG, "Database closed");
		dbhelper.close();
	}

	public void create(Course course) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COURSE_NAME, course.getCourseName());
		values.put(DBOpenHelper.COURSE__CODE, course.getCourseCode());
		values.put(DBOpenHelper.COURSE_VENUE, course.getCourseVenue());
		values.put(DBOpenHelper.COURSE_REQ_ATTEND,
				course.getCourseReqAttendance());
		values.put(DBOpenHelper.COURSE_DES_ATTEND,
				course.getCourseDesiredAttendance());
		values.put(DBOpenHelper.MON_TIMINGS, course.getMonTimings());
		values.put(DBOpenHelper.TUES_TIMINGS, course.getTuesTimings());
		values.put(DBOpenHelper.WED_TIMINGS, course.getWedTimings());
		values.put(DBOpenHelper.THURS_TIMINGS, course.getThursTimings());
		values.put(DBOpenHelper.FRI_TIMINGS, course.getFriTimings());

		long insertid = database.insert(DBOpenHelper.TABLE_COURSES, null,
				values);
		Log.i(TAG, "in create in datasrc " + insertid);
	}
	public List<Course> findAll() {
		
		Cursor cursor = database.query(DBOpenHelper.TABLE_COURSES, allColumns, 
				null, null, null, null, null);
				
		Log.i(TAG, "Returned " + cursor.getCount() + " rows");
		List<Course> courses  = cursorToList(cursor);
		return courses;
	}

	private List<Course> cursorToList(Cursor cursor) {
		List<Course> courses = new ArrayList<Course>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Course course = new Course();
				course.setCourseName(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME)));
				course.setCourseVenue(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_VENUE)));
				course.setCourseCode(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE__CODE)));
				course.setCourseReqAttendance(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_REQ_ATTEND)));
				course.setCourseDesiredAttendance(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_DES_ATTEND)));
				course.setMonTimings(cursor.getString(cursor.getColumnIndex(DBOpenHelper.MON_TIMINGS)));
				course.setTuesTimings(cursor.getString(cursor.getColumnIndex(DBOpenHelper.TUES_TIMINGS)));
				course.setWedTimings(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WED_TIMINGS)));
				course.setThursTimings(cursor.getString(cursor.getColumnIndex(DBOpenHelper.THURS_TIMINGS)));
				course.setFriTimings(cursor.getString(cursor.getColumnIndex(DBOpenHelper.FRI_TIMINGS)));

				courses.add(course);
				Log.v(TAG, course.toString());

			}
		}
		return courses;
	}

}
