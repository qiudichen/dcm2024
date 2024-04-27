package com.e2.wfm.rule.daily;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import com.e2.wfm.rule.Entity;

/**
 * DailyRule API
 */
public interface ShiftRule extends Entity {
	String getShiftType();

	LocalTime getEarliestStartTime();

	LocalTime getLatestStartTime();

	Duration getStartTimeIncrement();

	Duration getShiftLength();

	List<ShiftEvent> getActivities();
	
	SplitShiftRule getSplitShift();
}
