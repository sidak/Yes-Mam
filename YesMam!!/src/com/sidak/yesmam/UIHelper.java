package com.sidak.yesmam;

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
	
	public static String getText(Activity activity, int id) {
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
	public static int[] getDateFromText(String date, Context context){
		
		if(date==context.getString(R.string.enterDate)){
			return null;
		}
		String[] dateElements=date.split("\\/");
		Log.v("ui", dateElements[0]+":"+dateElements[1]+":"+dateElements[2]);
		// although date is shown in dd/mm/yyyy
		// is actually set as mm/dd/yyyy
		int day =Integer.parseInt(dateElements[1]);
		int month =Integer.parseInt(dateElements[0]);
		int year =Integer.parseInt(dateElements[2]);
		int[] dateEle={day,month,year};
		
//		Date d = null;
//		Calendar cal = GregorianCalendar.getInstance();
//		cal.set(1900 + year, month-1, day);
//		d = cal.getTime();
		return dateEle;

	}

}
