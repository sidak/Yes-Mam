package com.sidak.yesmam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
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
	public static Date getDateObjectFromTextTime(String dateString) {
		// it is assumed that the dateString is validated before sending
		Date d = null;
		try {
			d = new SimpleDateFormat("HH:mm").parse(dateString);
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
	public static int getDayOfWeekFromDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	public static boolean checkIfBefore(String s1, String s2){
		Date d1,d2;
		d1=getDateObjectFromText(s1);
		d2=getDateObjectFromText(s2);
		if(d1.compareTo(d2)<=0){
			return true;
		}
		return false;
	}
	public static boolean checkIfWeekend(String s ){
		Date d = getDateObjectFromText(s);
		int day = getDayOfWeekFromDate(d);
		if(day==Calendar.SATURDAY || day == Calendar.SUNDAY){
			return true;
		}
		else return false;
	}
	public static int calculateSpecificDay(Date d1, Date d2, int day) {
		Calendar c1 = Calendar.getInstance();  
        c1.setTime(d1);  
   
        Calendar c2 = Calendar.getInstance();  
        c2.setTime(d2);  
   
        int num = 0;  
   
        while(c2.after(c1)) {  
            if(c1.get(Calendar.DAY_OF_WEEK)==day)  
                num++;  
            c1.add(Calendar.DATE,1);  
        }  
        if(c2.get(Calendar.DAY_OF_WEEK)==day)num++;
        return num;
	}
}