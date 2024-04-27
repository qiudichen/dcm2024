package com.e2.wfm.commons.meta.common.shift;

import com.e2.wfm.commons.meta.common.Countable;
import com.e2.wfm.rule.Entity;
import com.e2.wfm.rule.RuleConstants;

import java.time.LocalTime;

public class ShiftMeta implements Countable {
	/* day off shift */
	private static ShiftMeta dayOffShift = new ShiftMeta(true);

	public static ShiftMeta getDayOffShift() {
		return dayOffShift;
	}

	private final Entity entity;
	
	/* indicate if it is DayOff Shift */
	private boolean dayOff;	
	
	private final ShiftItem[] shiftItems;

	private ShiftMeta(boolean dayOff) {
		
		this.dayOff = dayOff;
		this.shiftItems = new ShiftItem[0];
		this.entity = null;
	}

	public ShiftMeta(Entity entity, ShiftItem[] shiftItems) {
		super();
		this.entity = entity;
		this.shiftItems = shiftItems;
	}
	
	public boolean isDayOff() {
		return dayOff;
	}
	
	public Entity getEntity() {
		return entity;
	}

	public String getOid() {
		if(entity == null) {
			return RuleConstants.OFF_OID;
		}
		return entity.getOid();
	}

	public ShiftItem[] getShiftItems() {
		return shiftItems;
	}

	public LocalTime getEarliestStartTime() {
		LocalTime startTime = null;
		for(ShiftItem shiftItem : this.shiftItems) {
			LocalTime start = shiftItem.getEarliestStartTime();
			if(startTime == null || startTime.isAfter(start)) {
				startTime = start;
			}
		}
		return startTime;
	}
	
	public LocalTime getLatestEndTime() {
		LocalTime endTime = null;
		for(ShiftItem shiftItem : this.shiftItems) {
			LocalTime end = shiftItem.getLatestEndTime();
			if(endTime == null || endTime.isBefore(end)) {
				endTime = end;
			}
		}
		return endTime;
	}
	
	@Override
	public long totalNumber() {
		if(this.isDayOff()) {
			return 1l;
		}
	
		long count = 0;
		for(ShiftItem shiftItem : this.shiftItems) {
			count =  count + shiftItem.totalNumber();
		}
		return count;
	}
}
