package com.sidak.yesmam;

import java.util.List;

import com.sidak.yesmam.model.Holiday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HolidayListAdapter extends ArrayAdapter<Holiday> {
	Context context;
	List<Holiday> holidays;
	private TextView viewType;
	private TextView viewDate;
	private TextView viewDesc;
	private Holiday holiday;
	
	public HolidayListAdapter(Context context, List<Holiday> holidays) {
		super(context, android.R.id.content, holidays);
		this.context = context;
		this.holidays = holidays;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	        convertView = vi.inflate(R.layout.list_item_holiday, null);
		}
        holiday = holidays.get(position);
        
        viewType = (TextView) convertView.findViewById(R.id.viewType);
        viewDate = (TextView) convertView.findViewById(R.id.viewDateString);
        viewDesc = (TextView) convertView.findViewById(R.id.viewDesc);

        viewType.setText(holiday.getType());
        viewDate.setText(holiday.getDay()+"/"+holiday.getMonth()+"/"+holiday.getYear());
        viewDesc.setText(holiday.getDescription());

//        LinearLayout outer= (LinearLayout)convertView.findViewById(R.id.outerLayout);
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				Toast.makeText(context, "hey all"+holiday.getDescription(), Toast.LENGTH_LONG).show();
//				return false;
//			}
//		});
		return convertView;
	}
	
	
}
