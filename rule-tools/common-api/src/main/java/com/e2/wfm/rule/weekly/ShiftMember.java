package com.e2.wfm.rule.weekly;

import com.e2.wfm.rule.common.IntegerRange;
import com.e2.wfm.rule.common.time.DayPattern;

/**
 * daily rule that the weekly rule contains
 */
public interface ShiftMember {
	String getDailyRuleOid();

	DayPattern getAllowableDays();

	IntegerRange getUsage();
}
