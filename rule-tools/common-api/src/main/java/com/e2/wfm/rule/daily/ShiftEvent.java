package com.e2.wfm.rule.daily;

import java.time.Duration;

/**
 * Event that the shift contains
 */
public interface ShiftEvent {
	String getActivityCodeOid();

	Duration getRelativeStart();

	Duration getLength();

	Duration getSlack();

	Duration getSlackIncrement();

}
