package com.sidak.yesmam;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sidak.yesmam.model.Course;

public class CourseListAdapter extends ArrayAdapter<Course> {
	Context context;
	List<Course> courses;
	private TextView viewCode;
	private TextView viewVenue;
	private TextView viewName;
	private TextView viewDesiredAtten;
	private TextView viewMinAtten;
	private TextView monTime;
	private TextView thuTime;
	private TextView tueTime;
	private TextView wedTime;
	private TextView friTime;

	private Course course;

	public CourseListAdapter(Context context, List<Course> courses) {
		super(context, android.R.id.content, courses);
		this.context = context;
		this.courses = courses;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item_course, null);
		}
		course = courses.get(position);

		viewCode = (TextView) convertView.findViewById(R.id.viewCode);
		viewVenue = (TextView) convertView.findViewById(R.id.viewVenue);
		viewName = (TextView) convertView.findViewById(R.id.viewName);
		viewDesiredAtten = (TextView) convertView
				.findViewById(R.id.viewDesiredAtten);
		viewMinAtten = (TextView) convertView.findViewById(R.id.viewMinAtten);

		monTime = (TextView) convertView.findViewById(R.id.monTime);
		tueTime = (TextView) convertView.findViewById(R.id.tueTime);
		wedTime = (TextView) convertView.findViewById(R.id.wedTime);
		thuTime = (TextView) convertView.findViewById(R.id.thuTime);
		friTime = (TextView) convertView.findViewById(R.id.friTime);

		viewCode.setText(course.getCourseCode());
		viewName.setText(course.getCourseName());
		viewVenue.setText(course.getCourseVenue());
		viewDesiredAtten.setText(course.getCourseDesiredAttendance());
		viewMinAtten.setText(course.getCourseReqAttendance());
		
		// in display it is "na" for a course's timing
		// if there's no class on that date
		// but in database it is "enter here"
		
		if (!(course.getMonTimings().equals(context
				.getString(R.string.enterDate)))) {
			monTime.setText(course.getMonTimings());
		} else {
			monTime.setText(R.string.na);
		}

		if (!(course.getTuesTimings().equals(context
				.getString(R.string.enterDate)))) {
			tueTime.setText(course.getTuesTimings());
		} else {
			tueTime.setText(R.string.na);
		}
		if (!(course.getWedTimings().equals(context
				.getString(R.string.enterDate)))) {
			wedTime.setText(course.getWedTimings());
		} else {
			wedTime.setText(R.string.na);
		}
		if (!(course.getThursTimings().equals(context
				.getString(R.string.enterDate)))) {
			thuTime.setText(course.getThursTimings());
		} else {
			thuTime.setText(R.string.na);
		}
		if (!(course.getFriTimings().equals(context
				.getString(R.string.enterDate)))) {
			friTime.setText(course.getFriTimings());
		} else {
			friTime.setText(R.string.na);
		}

		return convertView;
	}

}
