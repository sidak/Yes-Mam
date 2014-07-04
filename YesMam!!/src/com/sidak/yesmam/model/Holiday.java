package com.sidak.yesmam.model;

public class Holiday {
	private int day;
	private int month ;
	private int year;
	private String description;
	public int getDay(){
		return day;
	}
	public int getMonth(){
		return month;
	}
	public int getYear(){
		return year;
	}
	public String getDescription(){
		return description;
	}
	public void setDay(int day){
		this.day= day;
	}
	public void setMonth(int month){
		 this.month=month;
	}
	public void setYear(){
		this.year = year;
	}
	public void setDescription(){
		this.description= description;
	}
	@Override
	public String toString() {
		return description+ "/t"+ day +"/"+ month+"/"+ year;
	}
}
