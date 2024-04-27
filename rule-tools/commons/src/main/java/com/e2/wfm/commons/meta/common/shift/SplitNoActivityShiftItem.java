package com.e2.wfm.commons.meta.common.shift;

import com.e2.wfm.commons.meta.common.InvalidShiftMetaException;
import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.rule.Entity;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/*
 * In case the split shift doesn't has activities, Meta data can have a list of off segement length
 * all activities and lunch already are aligned 
 * to shift start time with duration.
 */
public class SplitNoActivityShiftItem extends ShiftItem {
	
	private List<Duration> offSegementLengths;
	
	private Duration splitShfitLength;	
	
	public SplitNoActivityShiftItem(Entity entity, List<LocalTime> startTimes, List<EventMeta> breakEvents,
			EventMeta lunchEvent, Duration shfitLength, List<Duration> offSegementLengths, Duration splitShfitLength) {
		super(entity, startTimes, breakEvents, lunchEvent, shfitLength);
		
		if(isZero(splitShfitLength) || offSegementLengths.isEmpty()) {
			throw new InvalidShiftMetaException("off segement or split shift length is zero. Use ShiftMeta class.");
		}
		
		this.offSegementLengths = offSegementLengths;
		this.splitShfitLength = splitShfitLength;
	} 
		
	public List<Duration> getOffSegementLengths() {
		return offSegementLengths;
	}

	public Duration getSplitShfitLength() {
		return splitShfitLength;
	}

	public Duration getTotalShfitLength() {
		return super.getShfitLength().plus(splitShfitLength);
	}
	
	@Override
	public LocalTime getLatestEndTime() {
		return getLatestStartTime().plus(getTotalShfitLength()).plus(offSegementLengths.get(offSegementLengths.size() - 1));
	}
	
	@Override
	public long totalNumber() {
		long count = super.totalNumber();
		if(offSegementLengths != null) {
			count = count * offSegementLengths.size();
		}
		return count;
	}
}
