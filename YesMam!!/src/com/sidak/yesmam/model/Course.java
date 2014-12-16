package com.sidak.yesmam.model;

public class Course {
	private String courseName="";
	private String courseCode="";
	private String courseVenue="";
	// implement attendance var's as double and not int , since they wld be represented
	// as percentage 
	private String courseReqAttendance="";
	private String courseDesiredAttendance="";
	//private boolean[] isWeekDayClass;
	private String monTimings="";
	private String tuesTimings="";
	private String wedTimings="";
	private String thursTimings="";
	private String friTimings="";
	private int totalClasses=0;
	private int attendClasses=0;
	private int bunkClasses=0;
	public Course(String code){
		courseCode=code;
	}
	public Course(){
		
	}
	public String getCourseName(){
		return courseName;
	}
	public String getCourseCode(){
		return courseCode;
	}
	public String getCourseVenue(){
		return courseVenue;
	}
	public String getCourseReqAttendance(){
		return courseReqAttendance;
	}
	public String getCourseDesiredAttendance(){
		return courseDesiredAttendance;
	}
	public int getTotalClasses(){
		return totalClasses;
	}
	
	public int getAttendedClasses(){
		return attendClasses;
	}
	public int getBunkedClasses(){
		return bunkClasses;
	}
	
	public void  setTotalClasses(int n){
		totalClasses=n;
	}
	
	public void  setAttendClasses(int n){
		attendClasses=n;
	}
	
	public void  setBunkClasses(int n){
		bunkClasses=n;
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
	public void setCourseReqAttendance(String reqAttendance){
		courseReqAttendance=reqAttendance;
	}
	public void setCourseDesiredAttendance(String desiredAttendance){
		courseDesiredAttendance= desiredAttendance;
	}
	public String getMonTimings(){
		return monTimings;
	}
	public void setMonTimings(String tmg){
		monTimings=tmg;
	}
	public String getTuesTimings(){
		return tuesTimings;
	}
	public void setTuesTimings(String tmg){
		tuesTimings=tmg;
	}
	public String getWedTimings(){
		return wedTimings;
	}
	public void setWedTimings(String tmg){
		wedTimings=tmg;
	}
	public String getThursTimings(){
		return thursTimings;
	}
	public void setThursTimings(String tmg){
		thursTimings=tmg;
	}
	public String getFriTimings(){
		return friTimings;
	}
	public void setFriTimings(String tmg){
		friTimings=tmg;
	}
	public String toString(){
		return courseCode +" "+courseVenue+" "+courseName+" "+
				courseDesiredAttendance+ " "+ courseReqAttendance+
				" "+monTimings+" "+ tuesTimings+ " "+ wedTimings+ " "+
				thursTimings+" "+friTimings + " "+totalClasses+ " "+attendClasses+" "+bunkClasses;
	
	}
}
