package com.sidak.yesmam.model;

public class Holiday {
	public final static String TYPE_PLANNED="Planned";
	public final static String TYPE_INSTI="Insti";

	private int day;
	private int month ;
	private int year;
	private String description;
	private String type;
	private long id;
	public long getId(){
		return id;
	}
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
	public String getType(){
		return type;
	}
	public void setDay(int day){
		this.day= day;
	}
	public void setMonth(int month){
		 this.month=month;
	}
	public void setYear(int year){
		this.year = year;
	}
	public void setDescription(String description){
		this.description= description;
	}
	public void setType(String type){
		this.type= type;
	}
	public void setId(long id){
		this.id= id;
	}
	@Override
	public String toString() {
		return type+"\t"+ description+ "\n"+ day +"/"+ month+"/"+ year;
	}
}
