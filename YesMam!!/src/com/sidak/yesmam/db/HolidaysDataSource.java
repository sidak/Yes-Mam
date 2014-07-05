package com.sidak.yesmam.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sidak.yesmam.HolidayList;
import com.sidak.yesmam.model.Holiday;

public class HolidaysDataSource {
	
public static final String TAG=HolidaysDataSource.class.getSimpleName();
	
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	private static final String[] allColumns = {
		DBOpenHelper.COLUMN_ID,
		DBOpenHelper.COLUMN_DAY,
		DBOpenHelper.COLUMN_MONTH,
		DBOpenHelper.COLUMN_YEAR,
		DBOpenHelper.COLUMN_DESC,
		DBOpenHelper.COLUMN_TYPE

	};
	
	public HolidaysDataSource(Context context) {
		dbhelper = new DBOpenHelper(context);
	}
	
	public void open() {
		Log.i(TAG, "Database opened");
		database = dbhelper.getWritableDatabase();
		Log.i(TAG, "after getwrirable");

	}

	public void close() {
		Log.i(TAG, "Database closed");		
		dbhelper.close();
	}
	public Holiday create(Holiday holiday) {
		Log.v(TAG, holiday.toString());

		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.COLUMN_DAY, holiday.getDay());
		values.put(DBOpenHelper.COLUMN_MONTH, holiday.getMonth());
		values.put(DBOpenHelper.COLUMN_YEAR, holiday.getYear());
		values.put(DBOpenHelper.COLUMN_DESC, holiday.getDescription());
		values.put(DBOpenHelper.COLUMN_TYPE, holiday.getType());

		long insertid = database.insert(DBOpenHelper.TABLE_HOLIDAYS, null, values);
		Log.i(TAG, "in create in datasrc " +insertid);

		holiday.setId(insertid);
		return holiday;
	}
	public List<Holiday> findAll() {
		
		Cursor cursor = database.query(DBOpenHelper.TABLE_HOLIDAYS, allColumns, 
				null, null, null, null, null);
				
		Log.i(TAG, "Returned " + cursor.getCount() + " rows");
		List<Holiday> holidays  = cursorToList(cursor);
		return holidays;
	}

	public List<Holiday> findFiltered(String selection, String orderBy) {
		
		Cursor cursor = database.query(DBOpenHelper.TABLE_HOLIDAYS, allColumns, 
				selection, null, null, null, orderBy);
		
		Log.i(TAG, "Returned " + cursor.getCount() + " rows");
		List<Holiday> holidays = cursorToList(cursor);
		return holidays;
	}
	
	private List<Holiday> cursorToList(Cursor cursor) {
		List<Holiday> holidays = new ArrayList<Holiday>();
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Holiday holiday = new Holiday();
				holiday.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_ID)));
				holiday.setDay(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COLUMN_DAY)));
				holiday.setMonth(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COLUMN_MONTH)));
				holiday.setYear(cursor.getInt(cursor.getColumnIndex(DBOpenHelper.COLUMN_YEAR)));
				holiday.setDescription(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_DESC)));
				holiday.setType(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TYPE)));

				holidays.add(holiday);
				Log.v(TAG, holiday.toString());

			}
		}
		return holidays;
	}

	public boolean remove(Holiday removeHoliday) {
		String desc= removeHoliday.getDescription();
		String type= removeHoliday.getType();
		int day = removeHoliday.getDay();
		int month = removeHoliday.getMonth();
		int year = removeHoliday.getYear();
		
		String where=allColumns[1] +"="+removeHoliday.getDay() +" AND "+ 
				allColumns[2] +"="+removeHoliday.getMonth() +" AND "+
				allColumns[3] +"="+removeHoliday.getYear() +" AND "+ 
				allColumns[4] +"= '"+removeHoliday.getDescription() +"' AND "+ 
				allColumns[5] +"= '"+removeHoliday.getType() +"'"; 
		int result = database.delete(DBOpenHelper.TABLE_HOLIDAYS, where, null);
		return result==1;
}
	
}

