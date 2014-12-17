package com.sidak.yesmam;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sidak.yesmam.model.WorkingDay;

public class WorkingDayAdapter extends ArrayAdapter<WorkingDay> {
	Context context;
	private List<WorkingDay> wdays;
	private TextView wdDate;
	private TextView wdDay;
	private WorkingDay wday;
	

	public WorkingDayAdapter(Context context, List<WorkingDay> wdays) {
		super(context, android.R.id.content, wdays);
		this.context = context;
		this.wdays = wdays;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.list_item_working_day, null);
		}
		wday = wdays.get(position);

		wdDate = (TextView) convertView.findViewById(R.id.wdDate);
		wdDay = (TextView) convertView.findViewById(R.id.wdDay);
			
		wdDate.setText(wday.getDateString());
		wdDay.setText(wday.getDayString());
		
		return convertView;
	}
	
}
