package com.sidak.yesmam;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	private TextView textView;
	public TimePickerFragment(TextView tv){
		textView=tv;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		String min,hr;
		if(minute<10){
			min="0"+minute;
		}
		else{
			min=""+minute;
		}
		if(hourOfDay<10){
			hr="0"+hourOfDay;
		}else{
			hr=""+hourOfDay;
		}
		textView.setText(hr+":"+min);
	}
}
