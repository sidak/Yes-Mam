package com.sidak.yesmam;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sidak.yesmam.model.UnmarkedClass;

public class UnmarkedClassAdapter extends ArrayAdapter<UnmarkedClass> {
	Context context;
	private List<UnmarkedClass> uClasses;
	private TextView dateString;
	private TextView courseCode;
	private TextView courseName;
	private TextView courseTimings;
	

	private UnmarkedClass uClass;

	public UnmarkedClassAdapter(Context context, List<UnmarkedClass> uClasses) {
		super(context, android.R.id.content, uClasses);
		this.context = context;
		this.uClasses = uClasses;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item_unmarked_class, null);
		}
		uClass = uClasses.get(position);

		dateString = (TextView) convertView.findViewById(R.id.viewDateString);
		courseTimings = (TextView) convertView.findViewById(R.id.viewTimings);
		courseCode = (TextView) convertView.findViewById(R.id.viewCode1);
		courseName = (TextView) convertView.findViewById(R.id.viewName1);
		
		dateString.setText(uClass.getDate());
		courseCode.setText(uClass.getCourse().getCourseCode());
		courseName.setText(uClass.getCourse().getCourseName());
		String time = "", day = "";
		Calendar c = Calendar.getInstance();
		c.setTime(UIHelper.getDateObjectFromText(uClass.getDate()));
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			time = uClass.getCourse().getMonTimings();
			day = "Monday";
		} else if (c.get(Calendar.DAY_OF_WEEK)== Calendar.TUESDAY) {
			time = uClass.getCourse().getTuesTimings();
			day = "Tuesday";
		} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			time = uClass.getCourse().getWedTimings();
			day = "Wednesday";
		} else if (c.get(Calendar.DAY_OF_WEEK)== Calendar.THURSDAY) {
			time = uClass.getCourse().getThursTimings();
			day = "Thursday";
		} else if (c.get(Calendar.DAY_OF_WEEK)== Calendar.FRIDAY) {
			time = uClass.getCourse().getFriTimings();
			day = "Friday";
		}
		courseTimings.setText(time + ", " + day);

		

		return convertView;
	}

}
