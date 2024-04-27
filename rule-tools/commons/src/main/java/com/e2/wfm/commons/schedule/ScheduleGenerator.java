package com.e2.wfm.commons.schedule;

import com.e2.wfm.commons.meta.common.ShiftMetaFactory;
import com.e2.wfm.commons.meta.common.schedule.ScheduleMeta;
import com.e2.wfm.commons.meta.common.shift.ShiftMeta;
import com.e2.wfm.rule.activity.Event;
import com.e2.wfm.rule.daily.ShiftRule;
import com.e2.wfm.rule.weekly.ScheduleRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScheduleGenerator {
	
	private final List<Event> events;

	private final List<ShiftRule> shiftRules;
	
	private final List<ScheduleRule> scheduleRules;

	private Map<String, Event> eventMap;
	
	private Map<String, ShiftRule> shiftRuleMap;

	private Map<String, ShiftMeta> shiftMetaMap = new HashMap<>();

	public ScheduleGenerator(List<Event> events, List<ShiftRule> shiftRules, List<ScheduleRule> scheduleRules) {
		super();
		this.events = events;
		this.shiftRules = shiftRules;
		this.scheduleRules = scheduleRules;
	}
	
	public Map<String, Event> getEventMap() {
		if(eventMap == null) {
			eventMap = events.stream().collect(Collectors.toMap(e -> e.getOid(), Function.identity()));
		} 
		return eventMap;
	}
	
	public ShiftRule getShiftRule(String shiftOid) {
		if(shiftRuleMap == null) {
			shiftRuleMap = this.shiftRules.stream().collect(Collectors.toMap(e -> e.getOid(), Function.identity()));
		}
		return shiftRuleMap.get(shiftOid);
	}
	
	public ShiftMeta getShiftMeta(String shiftOid) {
		if(!shiftMetaMap.containsKey(shiftOid)) {
			ShiftRule shiftRule = getShiftRule(shiftOid);
			ShiftMeta shiftMeta = ShiftMetaFactory.createShiftMeta(shiftRule, getEventMap());
			shiftMetaMap.put(shiftOid, shiftMeta);
		}
		return shiftMetaMap.get(shiftOid);
	}
	
	protected List<ScheduleMeta> toScheduleMetas(ScheduleRule scheduleRule) {
		//PatternDataBus dataBus = new PatternDataBus(scheduleRule);
		//getGenerator().generate(dataBus);
		//TODO
		//List<String[]> list = dataBus.getOidResults();
		
		return null;
	}
}
