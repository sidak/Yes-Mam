package com.sidak.yesmam;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sidak.yesmam.model.Course;

public class ClassListAdapter extends ArrayAdapter<Course> {
	Context context;
	List<Course> courses;
	private TextView viewCode1;
	private TextView viewVenue1;
	private TextView viewName1;
	private TextView viewDesiredAtten;
	private TextView viewMinAtten;
	private TextView viewClassTime;
	private TextView viewStatus;
	private Course course;

	public ClassListAdapter(Context context, List<Course> courses) {
		super(context, android.R.id.content, courses);
		this.context = context;
		this.courses = courses;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item_class, null);
		}
		course = courses.get(position);
		viewClassTime = (TextView) convertView.findViewById(R.id.viewClassTime);
		viewStatus = (TextView) convertView.findViewById(R.id.viewStatus);
		viewCode1 = (TextView) convertView.findViewById(R.id.viewCode1);
		viewVenue1 = (TextView) convertView.findViewById(R.id.viewVenue1);
		viewName1 = (TextView) convertView.findViewById(R.id.viewName1);
		//viewDesiredAtten = (TextView) convertView
			//	.findViewById(R.id.viewDesiredAtten);
		//viewMinAtten = (TextView) convertView.findViewById(R.id.viewMinAtten);

		viewCode1.setText(course.getCourseCode());
		viewName1.setText(course.getCourseName());
		viewVenue1.setText(course.getCourseVenue());
		//viewDesiredAtten.setText(course.getCourseDesiredAttendance());
		//viewMinAtten.setText(course.getCourseReqAttendance());
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.MONDAY) {
			viewClassTime.setText(course.getMonTimings());
		} else if (day == Calendar.TUESDAY) {
			viewClassTime.setText(course.getTuesTimings());
		} else if (day == Calendar.WEDNESDAY) {
			viewClassTime.setText(course.getWedTimings());
		} else if (day == Calendar.THURSDAY) {
			viewClassTime.setText(course.getThursTimings());
		} else if (day == Calendar.FRIDAY) {
			viewClassTime.setText(course.getFriTimings());
		}

		return convertView;
	}
	
}
