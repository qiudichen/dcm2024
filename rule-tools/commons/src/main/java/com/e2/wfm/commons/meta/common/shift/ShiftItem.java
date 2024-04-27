package com.e2.wfm.commons.meta.common.shift;

import com.e2.wfm.commons.meta.common.Countable;
import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.rule.Entity;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

/**
 * represent shift candidates
 */
public class ShiftItem implements Countable {

	private final Entity entity;
	
	private final List<LocalTime> startTimes;
	
	private final List<EventMeta> breakEvents;

	private final EventMeta lunchEvent;
	
	private final Duration shfitLength;

	public ShiftItem(Entity entity, List<LocalTime> startTimes, List<EventMeta> breakEvents, EventMeta lunchEvent, Duration shfitLength) {
		super();
		this.entity = entity;
		this.startTimes = startTimes;
		this.breakEvents = breakEvents;
		this.lunchEvent = lunchEvent;
		if(shfitLength == null) {
			this.shfitLength = Duration.ZERO;
		} else {
			this.shfitLength = shfitLength;
		}
	}

	public Entity getEntity() {
		return entity;
	}

	public List<LocalTime> getStartTimes() {
		return startTimes;
	}

	public List<EventMeta> getBreakEvents() {
		return breakEvents;
	}

	public Duration getShfitLength() {
		return shfitLength;
	}

	public EventMeta getLunchEvent() {
		return lunchEvent;
	}

	public LocalTime getEarliestStartTime() {
		return startTimes.get(0);
	}
	
	protected LocalTime getLatestStartTime() {
		return startTimes.get(startTimes.size() - 1);
	}

	public LocalTime getLatestEndTime() {
		return getLatestStartTime().plus(getShfitLength());
	}

	@Override
	public long totalNumber() {
		long count = startTimes.size();
		for(EventMeta eventMeta : this.breakEvents) {
			count = count * eventMeta.totalNumber();
		}
		
		if(lunchEvent != null) {
			count = count * lunchEvent.totalNumber();
		}
		
		return count;
	}
	
	protected boolean isZero(Duration duration) {
		return (duration == null || duration.isZero());
	}
}
