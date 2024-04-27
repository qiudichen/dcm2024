package com.e2.wfm.commons.meta.common.shift;

import com.e2.wfm.commons.meta.common.InvalidShiftMetaException;
import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.rule.Entity;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/*
 * In case the split shift has activities, Meta data has only one off segment so that all activities and lunch can ba aligned 
 * to shift start time with duration.
 */
public class SplitShiftItem extends ShiftItem {
	
	private Duration offSegementLength;
	
	private Duration splitShfitLength;	

	public SplitShiftItem(Entity entity, List<LocalTime> startTimes, List<EventMeta> breakEvents,
			EventMeta lunchEvent, Duration shfitLength, Duration offSegementLength, Duration splitShfitLength) {
		super(entity, startTimes, breakEvents, lunchEvent, shfitLength);
		if(isZero(offSegementLength) || isZero(splitShfitLength)) {
			throw new InvalidShiftMetaException("off segement or split shift length is zero. Use ShiftMeta class.");
		}
		this.offSegementLength = offSegementLength;
		this.splitShfitLength = splitShfitLength;
	} 

	public Duration getTotalShfitLength() {
		return super.getShfitLength().plus(splitShfitLength);
	}

	public Duration getActualLength() {
		return this.getTotalShfitLength().plus(offSegementLength);
	}
	
	public Duration getOffSegementLength() {
		return offSegementLength;
	}

	public Duration getSplitShfitLength() {
		return splitShfitLength;
	}
	
	public List<LocalTime> getEndTimes() {
		return this.getStartTimes().stream().map(s -> s.plus(getActualLength())).toList();
	}
	
	@Override
	public LocalTime getLatestEndTime() {
		return getLatestStartTime().plus(getActualLength());
	}
}
