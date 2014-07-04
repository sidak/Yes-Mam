package com.sidak.yesmam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CoursesDataSource {

	public static final String TAG=CoursesDataSource.class.getSimpleName();
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	public CoursesDataSource(Context context) {
		dbhelper = new DBOpenHelper(context);
	}
	
	public void open() {
		Log.i(TAG, "Database opened");
		database = dbhelper.getWritableDatabase();
	}

	public void close() {
		Log.i(TAG, "Database closed");		
		dbhelper.close();
	}
	
}
