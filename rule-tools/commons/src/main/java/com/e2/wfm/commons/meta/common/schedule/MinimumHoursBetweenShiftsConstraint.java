package com.e2.wfm.commons.meta.common.schedule;

import java.time.Duration;


/**
 * Minimum Hours Between Shift constraint
 */
public class MinimumHoursBetweenShiftsConstraint implements ScheduleConstraint {
	private final Duration minHoursBetweenShifts;
	
	public MinimumHoursBetweenShiftsConstraint(Duration minHoursBetweenShifts) {
		this.minHoursBetweenShifts = minHoursBetweenShifts;
	}
}
