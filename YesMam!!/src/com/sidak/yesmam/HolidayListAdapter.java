package com.sidak.yesmam;

import java.util.List;

import com.sidak.yesmam.model.Holiday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HolidayListAdapter extends ArrayAdapter<Holiday> {
	Context context;
	List<Holiday> holidays;
	
	public HolidayListAdapter(Context context, List<Holiday> holidays) {
		super(context, android.R.id.content, holidays);
		this.context = context;
		this.holidays = holidays;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.list_item_holiday, null);
	
        Holiday holiday = holidays.get(position);
        
        TextView viewType = (TextView) view.findViewById(R.id.viewType);
        TextView viewDate = (TextView) view.findViewById(R.id.viewDate);
        TextView viewDesc = (TextView) view.findViewById(R.id.viewDesc);

        viewType.setText(holiday.getType());
        viewDate.setText(holiday.getDay()+"/"+holiday.getMonth()+"/"+holiday.getYear());
        viewDesc.setText(holiday.getDescription());


		return view;
	}
}
