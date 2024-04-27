package com.e2.wfm.rule.daily;

import java.time.Duration;
import java.util.List;

public interface SplitShiftRule {
	Duration getShiftLength();

	Duration getOffSegmentMaxUsage();

	Duration getOffSegmentMinUsage();

	Duration getOffSegmentIncrement();

	List<ShiftEvent> getActivities();
}
