package com.sidak.yesmam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sidak.yesmam.db.HolidaysDataSource;
import com.sidak.yesmam.model.Holiday;

public class MainActivity extends Activity {

	public final String TAG = MainActivity.class.getSimpleName();
	private SharedPreferences prefs;
	private int numWorkingDays;
	private Calendar c;
	private List<Holiday> holidays;
	private HolidaysDataSource holiDatasource;
	private String holidayType;
	private String holidayDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getApplicationContext().getSharedPreferences(
				getString(R.string.semPrefs), 0);
		numWorkingDays = prefs.getInt(getString(R.string.numWorkingDays), 0);
		Log.v(TAG, "" + numWorkingDays);
		TextView t = (TextView) findViewById(R.id.trial);
		t.setText("" + numWorkingDays);
		holiDatasource = new HolidaysDataSource(this);
		holiDatasource.open();
		Log.i(TAG, "after opening holiday databasse");

		final Button t1 = (Button) findViewById(R.id.status1);
		t1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkHoliday()) {
					t1.setText(holidayDesc + " " + holidayType);
				} else {

					t1.setText("there is no holiday");
				}

			}
		});

		// check if it is a holiday
		// also cache the value for a particular day by checking the
		// current date and the variable
		// UIHelper.checkIfWeekend(s)
		// check for extra class
		// then if none: check for todays' classes

	}

	private boolean checkHoliday() {
		int date[] = UIHelper.getDateFromText(getCurrentDate(), this);
		Log.v(TAG, " " + date[0] + " " + date[1] + " " + date[2]);
		holidays = holiDatasource.findWhereDatesMatch(new String[] {
				"" + date[0], "" + date[1], "" + date[2] });
		if (holidays == null) {
			// not a holiday
			return false;
		} else if (holidays.size() > 0) {
			Holiday holiday = holidays.get(0);
			holidayDesc = holiday.getDescription();
			holidayType = holiday.getType();
			return true;
		} else {
			return false;
		}
	}

	private String getCurrentDate() {
		c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = df.format(c.getTime());
		Log.v(TAG, "current date " + formattedDate);
		return formattedDate;
	}

}
