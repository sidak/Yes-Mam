package com.sidak.yesmam;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddCourses extends Activity {
	public static final String TAG = AddCourses.class.getSimpleName();
	private EditText courseName;
	private EditText courseCode;
	private EditText courseVenue;
	private EditText reqAttendance;
	private EditText desAttendance;
	private CheckBox monCB;
	private CheckBox tuesCB;
	private CheckBox wedCB;
	private CheckBox thursCB;
	private CheckBox friCB;
	private TextView monTV;
	private TextView tuesTV;
	private TextView wedTV;
	private TextView thursTV;
	private TextView friTV;
	private Button submitCourseButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_courses);
		courseName = (EditText) findViewById(R.id.courseName);
		courseCode = (EditText) findViewById(R.id.courseCode);
		courseVenue = (EditText) findViewById(R.id.courseVenue);
		reqAttendance = (EditText) findViewById(R.id.reqAttendanceValue);
		desAttendance = (EditText) findViewById(R.id.desiredAttendanceValue);

		monCB = (CheckBox) findViewById(R.id.mondayCTV);
		tuesCB = (CheckBox) findViewById(R.id.tuesdayCTV);
		wedCB = (CheckBox) findViewById(R.id.wednesdayCTV);
		thursCB = (CheckBox) findViewById(R.id.thursdayCTV);
		friCB = (CheckBox) findViewById(R.id.fridayCTV);

		monTV = (TextView) findViewById(R.id.monTimings);
		tuesTV = (TextView) findViewById(R.id.tuesdayTimings);
		wedTV = (TextView) findViewById(R.id.wednesdayTimings);
		thursTV = (TextView) findViewById(R.id.thursdayTimings);
		friTV = (TextView) findViewById(R.id.fridayTimings);
		
		monTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(monTV);
			}
		});
		tuesTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(tuesTV);
			}
		});
		wedTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(wedTV);
			}
		});
		thursTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(thursTV);
			}
		});
		friTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimePickerDialog(friTV);
			}
		});
		monCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					showTimePickerDialog(monTV);
				}
			}
		});
		tuesCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					showTimePickerDialog(tuesTV);
				}
			}
		});
		wedCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					showTimePickerDialog(wedTV);
				}
			}
		});
		thursCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					showTimePickerDialog(thursTV);
				}
			}
		});
		friCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					showTimePickerDialog(friTV);
				}
			}
		});

		submitCourseButton = (Button) findViewById(R.id.submitCourse);

		submitCourseButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validateData()) {
					sendCourseData();
				}
			}

		});

	}

	/*
	 * @Override private void OnCheckedChangeListener(View v) { // TODO
	 * Auto-generated method stub switch (v.getId()) { case R.id.mondayCTV: if
	 * (UIHelper.getCBChecked(this, v.getId())) { showTimePickerDialog(monTV); }
	 * case R.id.tuesdayCTV: if (UIHelper.getCBChecked(this, v.getId())) {
	 * showTimePickerDialog(tuesTV); } case R.id.wednesdayCTV: if
	 * (UIHelper.getCBChecked(this, v.getId())) { showTimePickerDialog(wedTV); }
	 * case R.id.thursdayCTV: if (UIHelper.getCBChecked(this, v.getId())) {
	 * showTimePickerDialog(monTV); } case R.id.fridayCTV: if
	 * (UIHelper.getCBChecked(this, v.getId())) { showTimePickerDialog(friTV); }
	 * } }
	 */

	private void sendCourseData() {
		Intent sendIntent = new Intent();

		// if any of the timings are not set , they will be equal to
		// "enter here"
		sendIntent.putExtra("courseName",
				UIHelper.getTextFromEditext(this, courseName.getId()));
		sendIntent.putExtra("courseCode",
				UIHelper.getTextFromEditext(this, courseCode.getId()));
		sendIntent.putExtra("courseVenue",
				UIHelper.getTextFromEditext(this, courseVenue.getId()));
		sendIntent.putExtra("reqAttendance",
				UIHelper.getTextFromEditext(this, reqAttendance.getId()));
		sendIntent.putExtra("desAttendance",
				UIHelper.getTextFromEditext(this, desAttendance.getId()));
		//
		String monText,tuesText,wedText,thursText,friText;
		if(monCB.isChecked()){
			 monText = (String) monTV.getText();
		}
		else{
			 monText = getString(R.string.enterDate);
		}
		if(tuesCB.isChecked()){
			 tuesText = (String) tuesTV.getText();
		}
		else{
			 tuesText = getString(R.string.enterDate);
		}
		if(wedCB.isChecked()){
			 wedText = (String) wedTV.getText();
		}
		else{
			 wedText = getString(R.string.enterDate);
		}
		if(thursCB.isChecked()){
			 thursText = (String) thursTV.getText();
		}
		else{
			 thursText = getString(R.string.enterDate);
		}
		if(friCB.isChecked()){
			 friText = (String) friTV.getText();
		}
		else{
			 friText = getString(R.string.enterDate);
		}
		sendIntent.putExtra("monTimings", monText);
		sendIntent.putExtra("tuesTimings", tuesText);
		sendIntent.putExtra("wedTimings", wedText);
		sendIntent.putExtra("thursTimings", thursText);
		sendIntent.putExtra("friTimings", friText);
		setResult(RESULT_OK, sendIntent);
		finish();

	}

	private boolean validateData() {
		Log.v(TAG, "in validate data ");
		if (ifEmptyEdittext(courseName) || ifEmptyEdittext(courseCode)
				|| ifEmptyEdittext(courseVenue)
				|| ifEmptyEdittext(reqAttendance)
				|| ifEmptyEdittext(desAttendance)) {
			Log.v(TAG,
					"in the if cond checking if fields are mpty in course add ");
			Toast.makeText(this, R.string.fillAllFields, Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if(!(reqAttendance.getText().toString()).matches("[0-9]+")){
			Toast.makeText(this, "Required Attendance should be a numeric field", Toast.LENGTH_LONG)
			.show();
			return false;
		}
		if(!(desAttendance.getText().toString()).matches("[0-9]+")){
			Toast.makeText(this, "Desired Attendance should be a numeric field", Toast.LENGTH_LONG)
			.show();
			return false;
		}
		Log.v(TAG, ""+Integer.parseInt(desAttendance.getText().toString())+"<"+
				Integer.parseInt(reqAttendance.getText().toString()));
		if(Integer.parseInt(desAttendance.getText().toString()) <
				Integer.parseInt(reqAttendance.getText().toString())){
			Toast.makeText(this, "Desired Attendance should be greater than equal to Required Attendance value", Toast.LENGTH_LONG)
			.show();
			return false;
		}
		if (monCB.isChecked() || tuesCB.isChecked() || wedCB.isChecked()
				|| thursCB.isChecked() || friCB.isChecked()) {
			// check if those checkboxes which are checked have corresponding
			// dates along with it.
			return true;

		} else {
			Toast.makeText(this, R.string.fillTimings, Toast.LENGTH_LONG)
					.show();
		}
		return false;
	}

	private boolean ifEmptyEdittext(EditText ed) {

		return (ed.getText().toString()).equals("");
		// because the "enter here" appearing in the edittext is the label and
		// not the text

	}

	private void showTimePickerDialog(TextView tv) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		DialogFragment newFragment = new TimePickerFragment(tv);
		newFragment.show(ft, "timePicker");
	}

}
