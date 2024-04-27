package com.e2.wfm.commons.meta.common.schedule;

/**
 * Consistent start/break/lunch constraint. Must be applied to the same shift
 */
public class ConsistentConstraint implements ScheduleConstraint {

	private final boolean shiftStart;
	private final boolean breakStart;
	private final boolean lunchStart;

	public ConsistentConstraint(boolean shiftStart, boolean breakStart, boolean lunchStart) {
		super();
		this.shiftStart = shiftStart;
		this.breakStart = breakStart;
		this.lunchStart = lunchStart;
	}

	public boolean isShiftStart() {
		return shiftStart;
	}

	public boolean isBreakStart() {
		return breakStart;
	}

	public boolean isLunchStart() {
		return lunchStart;
	}
}
