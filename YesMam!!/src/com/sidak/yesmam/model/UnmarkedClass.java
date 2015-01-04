package com.sidak.yesmam.model;

public class UnmarkedClass {
	private String date;
	private Course course;
	private int attenVal;
	public UnmarkedClass(String d, Course c, int v){
		date =d;
		course=c;
		attenVal=v;
	}
	public void  setDate(String s){
		date=s;
	}
	public String getDate(){
		return date;
	}
	public void  setAttenVal(int v){
		attenVal=v;
	}
	public int getAttenVal(){
		return attenVal;
	}
	public void setCourse(Course c){
		course=c;
		
	}
	public Course getCourse(){
		return course;
	}
}
