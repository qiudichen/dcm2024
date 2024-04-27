package com.e2.wfm.commons.meta.common.event;

import com.e2.wfm.commons.meta.common.Countable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * represent event candidates
 */
public class EventMeta implements Countable {
	
	private final String oid;

	private final Duration length;
	
	/*
	 * all possible start time of the activity code
	 */
	private List<Duration> startTimes;

	public EventMeta(String oid, Duration length, List<Duration> startTimes) {
		super();
		this.oid = oid;
		this.length = length;
		this.startTimes = startTimes;
	}

	public String getOid() {
		return oid;
	}

	public Duration getLength() {
		return length;
	}

	public List<Duration> getStartTimes() {
		return startTimes;
	}

	@Override
	public long totalNumber() {
		return startTimes.size();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(length, oid, startTimes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventMeta other = (EventMeta) obj;
		return Objects.equals(length, other.length) && Objects.equals(oid, other.oid)
				&& Objects.equals(startTimes, other.startTimes);
	}

	public EventMeta copy() {
		return new EventMeta(oid, length, new ArrayList<>(this.startTimes));
	} 
}
