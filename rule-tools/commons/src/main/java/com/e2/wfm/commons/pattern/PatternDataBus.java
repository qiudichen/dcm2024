package com.e2.wfm.commons.pattern;

import static com.e2.wfm.rule.RuleConstants.OFF_OID;
import static com.e2.wfm.rule.RuleConstants.OFF_VAL;

import com.e2.wfm.commons.pattern.rule.ShiftUsageRule;
import com.e2.wfm.commons.pattern.rule.ShiftUsageRuleImpl;
import com.e2.wfm.rule.common.IntegerRange;
import com.e2.wfm.rule.weekly.ScheduleRule;
import com.e2.wfm.rule.weekly.ShiftMember;

import java.time.DayOfWeek;
import java.util.*;

public class PatternDataBus {
	private static final DayOfWeek[] dayOfWeeks = new DayOfWeek[]{ DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
	
	/**
	 * A two dimensional represents from start layer to end layer that 
	 * selectable item Id in each layer.
	 * For each example:
	 * layer 1: dayOff shift
	 * layer 2: shift1, shift2, and dayOff shift
	 *  ...
	 * layer 7: shift2, shift3, and dayOff shift
	 * { 
	 * {OFF_VAL},
	 * {OFF_VAL, 1, 2},
	 * ...
	 * {OFF_VAL, 2, 3},
	 * }
	 */
	private final int[][] selectableIds;
	
	/*
	 * Usage of each shift in the schedule
	 * key -> rule Id, value -> MaxMinRule
	 */
	private final ShiftUsageRule[] shiftUsageRules;
	
	/*
	 * max/min of consecutive day off
	 */
	private final IntegerRange consecutiveDaysOff;
	
	/*
	 * max/min of consecutive day on
	 */
	private final IntegerRange consecutiveDaysOn;

	private final SelectedValue selectedValue;
	
	private final Map<Integer, String> idToOidMap;
	
	private final int queueSize;
	
	public PatternDataBus(ScheduleRule scheduleRule) {
		this.consecutiveDaysOff = scheduleRule.getConsecutiveDaysOff();
		this.consecutiveDaysOn = scheduleRule.getConsecutiveDaysOn();
		selectedValue  = new SelectedValue(7);

		int numOfShifts = scheduleRule.getDailyRuleMembers().size();

		//each rule's usage plus day off usage
		shiftUsageRules = new ShiftUsageRule[numOfShifts + 1];
		idToOidMap = new HashMap<>(numOfShifts);
		
		//assign day off usage
		IntegerRange usage = scheduleRule.getUsage();		
		shiftUsageRules[OFF_VAL] = ShiftUsageRuleImpl.builder()
				.shiftId(OFF_VAL)
				.maxUsage(7 - usage.getMin())
				.minUsage(7 - usage.getMax())
				.build(); 
				
		idToOidMap.put(OFF_VAL, OFF_OID);

		Map<DayOfWeek, Set<Integer>> selectableShiftsMap = new EnumMap<>(DayOfWeek.class);
		//add day off to each day
		for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
			selectableShiftsMap.computeIfAbsent(dayOfWeek, k -> new HashSet<>()).add(OFF_VAL);
		}
		
		for(int i = 0; i < numOfShifts; i++) {
			ShiftMember shiftMember = scheduleRule.getDailyRuleMembers().get(i);		
			int index = i + 1;
			
			String oid = shiftMember.getDailyRuleOid();
			
			idToOidMap.put(index, oid);
			shiftUsageRules[index] = ShiftUsageRuleImpl.builder()
					.shiftId(index)
					.maxUsage(shiftMember.getUsage().getMax())
					.minUsage(shiftMember.getUsage().getMin())
					.build();  

			Set<DayOfWeek> allowedDays = shiftMember.getAllowableDays().getJavaDaysOfWeek();
			for(DayOfWeek dayOfWeek : allowedDays) {
				selectableShiftsMap.get(dayOfWeek).add(index);
			}
		}

		long count = 1;
		selectableIds = new int[dayOfWeeks.length][];
		for(int i = 0; i < dayOfWeeks.length; i++) {
			Set<Integer> dailyRules = selectableShiftsMap.get(dayOfWeeks[i]);
			selectableIds[i] = new int[dailyRules.size()];
			
			count = count * dailyRules.size();
			if(count > Integer.MAX_VALUE) {
				throw new RuntimeException("The number of shifts is " + numOfShifts 
						+ ". And the total pattern may be more than " + Integer.MAX_VALUE);
			}
			
			int j = 0;
			for(int id : dailyRules) {
				selectableIds[i][j++] = id;
			}
		}
		
		queueSize = (int)count;
	}
	
	public int getQueueSize() {
		return queueSize;
	}

	public SelectedValue getSelectedValue() {
		return selectedValue;
	}
	
	public int[][] getSelectableIds() {
		return selectableIds;
	}

//	/**
//	 * return oids result
//	 * @return
//	 */
//	public List<String[]> getOidResults() {
//		return this.results.stream().map(this::toOids).toList();
//	}
	
	public ShiftUsageRule getShiftUsageRule(int value) {
		if(this.shiftUsageRules == null) {
			return null;
		}
		return this.shiftUsageRules[value];
	}
	
	public int getShiftUsageRuleSize() {
		if(this.shiftUsageRules == null) {
			return 0;
		}
		
		return this.shiftUsageRules.length;
	}
	
	public boolean processResult(ScheduleListener listener) {
		if(this.isSelectedValid()) {
			int[] copiedValues = this.selectedValue.cloneValues();
			listener.addPattern(this.toOids(copiedValues));
			return true;
		}
		return false;
	}
	
	public boolean isLastLayer(int layer) {
		return layer == this.getSelectableIds().length;
	}
	
	private boolean isSelectedValid() {
		return isValidRuleUsage() && validConsecutive();
	}
	
	private String[] toOids(int[] ids) 
	{
		String[] oids = new String[ids.length];
		for(int i = 0; i < ids.length; i++) {
			oids[i] = getOidByIndex(ids[i]);
		}
		return oids;
	}
	
	private String getOidByIndex(int index) {
		return this.idToOidMap.get(index);
	}
	
	private boolean validConsecutive() {
		if(this.consecutiveDaysOff != null) {
			String[] dayOffs = split(this.selectedValue.getConsectivePattern(), "1", "0");
			if(!validLength(consecutiveDaysOff, dayOffs)) {
				return false;
			}
		}
		if(this.consecutiveDaysOn != null) {
			String[] dayOns = split(this.selectedValue.getConsectivePattern(), "0", "1");
			if(!validLength(consecutiveDaysOn, dayOns)) {
				return false;
			}
		}
		return true;
	}
	
	private static String[] split(String pattern, String splitBy, String value) {
		String[] values = pattern.split(splitBy);
		//if start and end are the same, need to merge them together
		if(pattern.startsWith(value) && pattern.endsWith(value) && values.length > 1) {
			String[] cycleValues = new String[values.length - 1];
			cycleValues[0] = values[0] + values[values.length -1];
			System.arraycopy(values, 1, cycleValues, 1, cycleValues.length - 1);
			return cycleValues;
		} 
		return values;
	}
	
	/*
	 * check max/min length of each value of values is between range
	 */
	private boolean validLength(IntegerRange range, String[] values) {
		int minLength = Integer.MAX_VALUE;
		int maxLength = 0;
		for(String v : values) {
			int len = v.length();
			if(len == 0) {
				continue;
			}
			
			if(len > maxLength) {
				maxLength = len;
			}
			if(len < minLength) {
				minLength = len;
			}
		}
		
		if(minLength == Integer.MAX_VALUE) {
			minLength = 0;
		}
		return range.contains(maxLength) && range.contains(minLength);
	}
	
	/**
	 * valid the selected values against usage rule
	 * @return
	 */
	private boolean isValidRuleUsage() {
		if(this.shiftUsageRules == null) {
			return true;
		}
		
		for(int i = 0; i < this.getShiftUsageRuleSize(); i++) {
			ShiftUsageRule usageRule = this.getShiftUsageRule(i);
			if(usageRule == null) {
				continue;
			}
			
			int usage = this.selectedValue.getUsage(i);
			if(usageRule.isInvalidUsage(usage)) {
				return false;
			}
		}
		return true;
	}

	/* protected methods for unit test */
	protected ShiftUsageRule[] getShiftUsageRules() {
		return shiftUsageRules;
	}

	protected IntegerRange getConsecutiveDaysOff() {
		return consecutiveDaysOff;
	}

	protected IntegerRange getConsecutiveDaysOn() {
		return consecutiveDaysOn;
	}

	protected Map<Integer, String> getIdToOidMap() {
		return idToOidMap;
	}
}
