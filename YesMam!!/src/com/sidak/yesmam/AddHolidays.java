package com.sidak.yesmam;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sidak.yesmam.model.Holiday;

public class AddHolidays extends Activity {
	private RadioButton typeInsti;
	private RadioButton typePlanned;
	private TextView holidayDate;
	private EditText holidayDesc;
	private Button saveHoliday;
	private String dateHoliday;
	private SharedPreferences prefs;
	private String startDate;
	private String endDate;

	public final String TAG = AddHolidays.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_holidays);
		typeInsti = (RadioButton) findViewById(R.id.typeInsti);
		typePlanned = (RadioButton) findViewById(R.id.typePlanned);
		holidayDate = (TextView) findViewById(R.id.showHolidayDate);
		holidayDesc = (EditText) findViewById(R.id.enterDesc);
		saveHoliday = (Button) findViewById(R.id.saveNewHoliday);
		prefs = getApplicationContext().getSharedPreferences(
				getString(R.string.semPrefs), 0);
		holidayDate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog((TextView) v);
			}
		});
		saveHoliday.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendData();
			}
		});
	}

	private void showDatePickerDialog(TextView tv) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new DatePickerFragment(tv);
		newFragment.show(ft, "datePicker");
	}

	private void sendData() {
		String type;
		if (typeInsti.isChecked()) {
			type = Holiday.TYPE_INSTI;
		} else {
			type = Holiday.TYPE_PLANNED;
		}
		String desc = holidayDesc.getText().toString();
		dateHoliday = holidayDate.getText().toString();
		if (desc == null || dateHoliday == null || type == null) {
			Toast.makeText(this, "Pls check the data inputted",
					Toast.LENGTH_LONG).show();
			return;
		}

		// Intent intent =getIntent();
		// startDate=intent.getStringExtra("dateStart");
		// endDate=intent.getStringExtra("dateEnd");
		startDate = prefs.getString(getString(R.string.dateStart), "1/1/2000");
		endDate = prefs.getString(getString(R.string.dateEnd), "" + "1/1/2000");
		Log.v(TAG, startDate + " " + endDate + " " + dateHoliday + " ");
		if (UIHelper.checkIfBefore(startDate, dateHoliday)
				&& UIHelper.checkIfBefore(dateHoliday, endDate)) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("type", type);
			returnIntent.putExtra("desc", desc);
			returnIntent.putExtra("date", dateHoliday);
			setResult(RESULT_OK, returnIntent);
			finish();
		} else {
			Toast.makeText(this,
					"Pls check the holiday dates with respect to the semester",
					Toast.LENGTH_LONG).show();
		}

	}

}
