package com.e2.wfm.commons.meta.common;

import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.rule.daily.ShiftEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventMetaFactory {
	
	private EventMetaFactory() {
		
	}
	
	public static EventMeta createEventMeta(ShiftEvent shiftEvent) {
		long slackIncrement = shiftEvent.getSlackIncrement() == null ? 0 : shiftEvent.getSlackIncrement().toMinutes();
		List<Duration> startTimes;
		if(slackIncrement == 0) {
			startTimes = List.of(shiftEvent.getRelativeStart());
		} else {
			int size =(int)(1 + shiftEvent.getSlack().toMinutes() / slackIncrement);
			List<Duration> startTimeTemp = new ArrayList<>(size);
			Duration start = shiftEvent.getRelativeStart();
			for(int i = 0; i < size; i++) {
				startTimeTemp.add(start);
				start = start.plus(shiftEvent.getSlackIncrement());
			}
			startTimes = Collections.unmodifiableList(startTimeTemp);
		}
		return new EventMeta(shiftEvent.getActivityCodeOid(), shiftEvent.getLength(), startTimes);
	}
	
	public static EventMeta createEventMeta(ShiftEvent shiftEvent, Duration firstShiftLength) {
		long slackIncrement = shiftEvent.getSlackIncrement() == null ? 0 : shiftEvent.getSlackIncrement().toMinutes();
		List<Duration> startTimes;
		if(slackIncrement == 0) {
			startTimes = List.of(shiftEvent.getRelativeStart().plus(firstShiftLength));
		} else {
			int size =(int)(1 + shiftEvent.getSlack().toMinutes() / slackIncrement);
			List<Duration> startTimeTemp = new ArrayList<>(size);
			Duration start = shiftEvent.getRelativeStart();
			for(int i = 0; i < size; i++) {
				startTimeTemp.add(start.plus(firstShiftLength));
				start = start.plus(shiftEvent.getSlackIncrement());
			}
			startTimes = Collections.unmodifiableList(startTimeTemp);
		}
		return new EventMeta(shiftEvent.getActivityCodeOid(), shiftEvent.getLength(), startTimes);
	}
}
