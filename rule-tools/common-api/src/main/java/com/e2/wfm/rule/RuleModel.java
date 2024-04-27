package com.e2.wfm.rule;

import com.e2.wfm.rule.activity.Event;
import com.e2.wfm.rule.daily.ShiftRule;
import com.e2.wfm.rule.weekly.ScheduleRule;

import java.util.List;

public interface RuleModel {
	//activity codes
	List<Event> getEvents();
	
	//daily rule
	List<ShiftRule> getShiftRules();
	
	//weekly rule
	List<ScheduleRule> getScheduleRules();
}
