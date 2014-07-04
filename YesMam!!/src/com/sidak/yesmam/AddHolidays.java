package com.sidak.yesmam;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_holidays);
		typeInsti=(RadioButton)findViewById(R.id.typeInsti);
		typePlanned=(RadioButton)findViewById(R.id.typePlanned);
		holidayDate=(TextView)findViewById(R.id.showHolidayDate);
		holidayDesc=(EditText)findViewById(R.id.enterDesc);
		saveHoliday=(Button)findViewById(R.id.saveNewHoliday);
		holidayDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog((TextView)v);
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
	private void sendData(){
		String type;
		if (typeInsti.isChecked()){
			type=Holiday.TYPE_INSTI;
		}
		else{
			type=Holiday.TYPE_PLANNED;
		}
		String desc= holidayDesc.getText().toString();
		String date =holidayDate.getText().toString();
		if(desc==null || date==null || type==null){
			Toast.makeText(this, "Pls check the data inputted", Toast.LENGTH_LONG).show();
			return;
		}
		Intent returnIntent = new Intent();
		returnIntent.putExtra("type", type);
		returnIntent.putExtra("desc", desc);
		returnIntent.putExtra("date", date);
		setResult(RESULT_OK, returnIntent);
		finish();

	}
	
	
}
