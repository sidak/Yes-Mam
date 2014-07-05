package com.sidak.yesmam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class UIHelper {

	public static void displayText(Activity activity, int id, String text) {
		TextView tv = (TextView) activity.findViewById(id);
		tv.setText(text);
	}
	
	public static String getTextFromTextview(Activity activity, int id) {
		TextView tv = (TextView) activity.findViewById(id);
		return tv.getText().toString();
	}
	public static String getTextFromEditext(Activity activity, int id) {
		EditText et = (EditText) activity.findViewById(id);
		return et.getText().toString();
	}

	public static boolean getCBChecked(Activity activity, int id) {
		CheckBox cb = (CheckBox) activity.findViewById(id);
		return cb.isChecked();
	}

	public static void setCBChecked(Activity activity, int id, boolean value) {
		CheckBox cb = (CheckBox) activity.findViewById(id);
		cb.setChecked(value);
	}

	public static int[] getDateFromText(String date, Context context) {

		if (date == context.getString(R.string.enterDate)) {
			return null;
		}
		String[] dateElements = date.split("\\/");

		int day = Integer.parseInt(dateElements[0]);
		int month = Integer.parseInt(dateElements[1]);
		int year = Integer.parseInt(dateElements[2]);
		int[] dateEle = { day, month, year };
		return dateEle;

	}

	public static Date getDateObjectFromText(String dateString) {
		// it is assumed that the dateString is validated before sending
		Date d = null;
		try {
			d = new SimpleDateFormat("dd/MM/yy").parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	//
	public static String getTextFromDateObject(Date date) {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(date);
	    int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day+"/"+(month+1)+"/"+year;
	}

}
