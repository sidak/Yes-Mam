package com.sidak.yesmam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	public static final String TAG = "YES MAM DB";
	
	private static final String DATABASE_NAME = "semester.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_COURSES=null;

	private static final String TABLE_CREATE_COURSES=null;

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

		Log.i(TAG, "Table has been created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLIDAYS);

		onCreate(db);
	}

}
