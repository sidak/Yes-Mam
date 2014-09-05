package com.sidak.yesmam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	public static final String TAG = "YES MAM DB";
	
	private static final String DATABASE_NAME = "semester.db";
	private static final int DATABASE_VERSION = 2;
	
	public static final String TABLE_COURSES="courses";
	public static final String COURSE_ID = "courseId";
	public static final String COURSE_NAME = "courseName";
	public static final String COURSE_VENUE = "courseVenue";
	public static final String COURSE__CODE = "courseCode";
	public static final String COURSE_DES_ATTEND = "desAttendance";
	public static final String COURSE_REQ_ATTEND = "reqAtendance";
	public static final String MON_TIMINGS="monday timings";
	public static final String TUES_TIMINGS="tuesday timings";
	public static final String WED_TIMINGS="wednesday timings";
	public static final String THURS_TIMINGS="thursday timings";
	public static final String FRI_TIMINGS="friday timings";
	
	private static final String TABLE_CREATE_COURSES=
			"CREATE TABLE " + TABLE_COURSES + " (" +
			 COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 COURSE_NAME + " TEXT, "+
			 COURSE__CODE + " TEXT, "+
			 COURSE_VENUE+ " TEXT, "+
			 COURSE_REQ_ATTEND + " REAL, "+
			 COURSE_DES_ATTEND+ " REAL, "+
			 MON_TIMINGS+ " TEXT, " +
			 TUES_TIMINGS+ " TEXT, " +
			 WED_TIMINGS+ " TEXT, " +
			 THURS_TIMINGS+ " TEXT, " +
			 FRI_TIMINGS+ " TEXT, " +
			 ")";

	public static final String TABLE_HOLIDAYS="holidays";
	public static final String COLUMN_ID = "holidayId";
	public static final String COLUMN_DAY = "day";
	public static final String COLUMN_MONTH = "month";
	public static final String COLUMN_YEAR = "year";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_TYPE = "type";


	private static final String TABLE_CREATE_HOLIDAYS=
			"CREATE TABLE " + TABLE_HOLIDAYS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 COLUMN_DAY+ " INTEGER, " +
			COLUMN_MONTH + " INTEGER, " +
			COLUMN_YEAR + " INTEGER, " +
			COLUMN_DESC + " TEXT, " +
			COLUMN_TYPE + " TEXT " +
			")";
			

	

	
	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(TAG, "dbopen helper constr");

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL(TABLE_CO);
		Log.i(TAG, "1GVHKJKJNKJKJN");
		Log.i(TAG, TABLE_CREATE_HOLIDAYS);
		
		db.execSQL(TABLE_CREATE_HOLIDAYS);
		db.execSQL(TABLE_CREATE_COURSES);
		Log.i(TAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAYS);

		onCreate(db);
	}

}
