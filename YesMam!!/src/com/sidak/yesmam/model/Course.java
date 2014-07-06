package com.sidak.yesmam.model;

public class Course {
	private String courseName;
	private String courseCode;
	private String courseVenue;
	// implement attendance var's as double and not int , since they wld be represented
	// as percentage 
	private double courseReqAttendance;
	private double courseDesiredAttendance;
	private boolean[] isWeekDayClass;
	private String[] weekDayClassTimings;
	public String getCourseName(){
		return courseName;
	}
	public String getCourseCode(){
		return courseCode;
	}
	public String getCourseVenue(){
		return courseVenue;
	}
	public double getCourseReqAttendance(){
		return courseReqAttendance;
	}
	public double getCourseDesiredAttendance(){
		return courseDesiredAttendance;
	}
	
	public void setCourseName(String name){
		courseName= name;
	}
	public void setCourseCode(String code){
		courseCode= code;
	}
	public void setCourseVenue(String venue){
		courseVenue= venue;
	}
	public void setCourseReqAttendance(double reqAttendance){
		courseReqAttendance=reqAttendance;
	}
	public void setCourseDesiredAttendance(double desiredAttendance){
		courseDesiredAttendance= desiredAttendance;
	}
	
}
