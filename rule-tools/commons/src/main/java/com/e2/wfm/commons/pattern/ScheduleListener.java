package com.e2.wfm.commons.pattern;

public interface ScheduleListener {
	
	public void addPattern(String[] oids);
	
	public String[] getNextOids();
	
	public void setCompleted();
	
	public boolean isCompleted();
	
	public boolean isEOF();
}
