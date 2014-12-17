package com.sidak.yesmam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WDBOpenHelper extends SQLiteOpenHelper {

	public static final String TAG = "YES MAM  W DB";
	private static int NUM_COURSES=0;
	private static final String DATABASE_NAME = "wdays.db";
	private static final int DATABASE_VERSION = 2;

	public static void setNumCourses(int n) {
		NUM_COURSES = n;
	}

	public static final String TABLE_WDAYS = "workingDays";
	public static String[] CODES;
	public static String[] ATTENDANCE;
	public static final String WDAY_DATE = "date";
	public static final String WDAY_DAY = "day";
	public static final String WDAY_ID = "wdayId";

	private static String TABLE_CREATE_WDAYS = "CREATE TABLE " + TABLE_WDAYS
			+ " (" + WDAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ WDAY_DATE + " TEXT, " + WDAY_DAY + " TEXT, ";

	public WDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(TAG, "dbopen helper constr");

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL(TABLE_CO);
		Log.i(TAG, "ewcvkewnfcknk");
		Log.i(TAG, TABLE_CREATE_WDAYS);
		CODES= new String[NUM_COURSES];
		ATTENDANCE=new String[NUM_COURSES];
		for (int i = 0; i < NUM_COURSES; i++) {
			CODES[i] = "course" + i;
		}
		for (int i = 0; i < NUM_COURSES; i++) {
			ATTENDANCE[i] = "attendance" + i;
		}
		for (int i = 0; i < NUM_COURSES; i++) {
			TABLE_CREATE_WDAYS += (CODES[i] + " TEXT, ");
		}
		for (int i = 0; i < NUM_COURSES-1; i++) {
			TABLE_CREATE_WDAYS += (ATTENDANCE[i] + " INTEGER, ");
		}
		TABLE_CREATE_WDAYS += (ATTENDANCE[NUM_COURSES-1] + " INTEGER ");
		TABLE_CREATE_WDAYS += ")";
		db.execSQL(TABLE_CREATE_WDAYS);

		Log.i(TAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WDAYS);
		onCreate(db);
	}

}
