package com.e2.wfm.rule.weekly;

import java.time.Duration;
import java.util.List;

import com.e2.wfm.rule.Entity;
import com.e2.wfm.rule.common.IntegerRange;

/**
 * WeeklyRule API
 */
public interface ScheduleRule extends Entity {
	IntegerRange getUsage();

	boolean isConsistentStartTimes();

	boolean isConsistentLunchTimes();

	boolean isConsistentBreakTimes();

	IntegerRange getConsecutiveDaysOff();

	IntegerRange getConsecutiveDaysOn();

	Duration getMinHoursBetweenShifts();

	List<ShiftMember> getDailyRuleMembers();
}
