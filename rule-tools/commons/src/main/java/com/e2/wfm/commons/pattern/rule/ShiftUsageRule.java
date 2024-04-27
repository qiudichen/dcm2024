package com.e2.wfm.commons.pattern.rule;
/**
 * Usage rule of shift member in weekly rule, including DayOff rule
 */
public interface ShiftUsageRule {
	/*
	 * internal id
	 */
	int getShiftId();
	/*
	 * maximum usage of the shift
	 */
	public int getMaxUsage();
	/*
	 * minimum usage of the shift
	 */
	public int getMinUsage();
	
	/*
	 * true if val > max usage
	 */
	public boolean isGreaterMax(int val);
	
	/*
	 * true if val < min usage
	 */
	public boolean isLessMin(int val);
	
	/*
	 * true if val < min or val > max
	 */
	public boolean isInvalidUsage(int val);
}
