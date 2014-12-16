package com.sidak.yesmam.model;

import java.util.List;

public class WorkingDay {
	private String dateString;
	private String dayString;
	public List<Course> courses;
	// 0 for bunk, 1 for attend , 2 for not calc
	public List<Integer> attendance;
	public String getDayString(){
		return dayString;
	}
	public String getDateString(){
		return dateString;
	}
	
	public void setDayString(String s){
		dayString=s;
	}
	public void setDateString(String s){
		dateString=s;
	}
	public String toString(){
		String temp =dateString +" "+dayString;
		for(int i=0;i<courses.size(); i++){
			temp+= courses.get(i).getCourseCode() + " " + attendance.get(i);
		}
		return temp;
	}
}
