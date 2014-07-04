package com.sidak.yesmam;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

public class SemesterActivity extends Activity  {
	private TextView semesterText;
	private TextView startDate;
	private TextView endDate;
	private TextView showStartDate;
	private TextView showEndDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semester);

		startDate = (TextView) findViewById(R.id.startDate);
		// endDate = (TextView)findViewById(R.id.endDate);
		showStartDate = (TextView) findViewById(R.id.showStartDate);
		showStartDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				DialogFragment newFragment = new DatePickerFragment(showStartDate);
				newFragment.show(ft, "datePicker");
			}
		});
	}
}
