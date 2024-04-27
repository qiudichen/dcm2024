package com.e2.wfm.commons.meta.common.schedule;

import com.e2.wfm.commons.meta.common.shift.ShiftMeta;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ScheduleMeta {
	//shift in each day, including day off shift
	private final ShiftMeta[] shiftMetas;
	
	//key -> oid, value -> shift
	private final Map<String, ShiftMeta> shiftMap;
	
	//key -> oid, usage of each shift. Used by consistent constraint
	private final Map<String, Integer> shiftUsages;
	
	private final MinimumHoursBetweenShiftsConstraint minConstraint;
	
	private final ConsistentConstraint consistentConstraint;
	
	public ScheduleMeta(ShiftMeta[] shiftMetas, Duration minHoursBetweenShifts,
			boolean shiftStart, boolean breakStart, boolean lunchStart) {
		super();
		this.shiftMetas = shiftMetas;
		
		if(shiftStart || breakStart || lunchStart) {
			this.consistentConstraint = new ConsistentConstraint(shiftStart, breakStart, lunchStart);
		} else {
			this.consistentConstraint =  null;
		}
		
		if(minHoursBetweenShifts == null || minHoursBetweenShifts == Duration.ZERO) {
			this.minConstraint = null;
		} else {
			this.minConstraint = new MinimumHoursBetweenShiftsConstraint(minHoursBetweenShifts);
		}
		
		this.shiftMap = new HashMap<>();
		this.shiftUsages = new HashMap<>();
		
		for (ShiftMeta shiftMeta : this.shiftMetas) {
			if (!shiftMeta.isDayOff()) {
				this.shiftMap.put(shiftMeta.getOid(), shiftMeta);
				
				if (shiftUsages.containsKey(shiftMeta.getOid())) {
					int count = shiftUsages.get(shiftMeta.getOid()) + 1;
					shiftUsages.put(shiftMeta.getOid(), count);
				} else {
					shiftUsages.put(shiftMeta.getOid(), 1);
				}
			}
		}
	}
}
