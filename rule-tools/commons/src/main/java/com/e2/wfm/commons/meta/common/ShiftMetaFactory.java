package com.e2.wfm.commons.meta.common;

import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.commons.meta.common.shift.ShiftItem;
import com.e2.wfm.commons.meta.common.shift.ShiftMeta;
import com.e2.wfm.commons.meta.common.shift.SplitNoActivityShiftItem;
import com.e2.wfm.commons.meta.common.shift.SplitShiftItem;
import com.e2.wfm.rule.activity.Event;
import com.e2.wfm.rule.activity.EventType;
import com.e2.wfm.rule.daily.ShiftEvent;
import com.e2.wfm.rule.daily.ShiftRule;
import com.e2.wfm.rule.daily.SplitShiftRule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ShiftMetaFactory {
	
	private ShiftMetaFactory() {
		
	}
	
	public static ShiftMeta createShiftMeta(ShiftRule shiftRule, Map<String, Event> eventMap) {
		ShiftItem[] shiftItems = createShiftItems(shiftRule, eventMap);
		return new ShiftMeta(shiftRule, shiftItems);
	}
	
	protected static ShiftItem[] createShiftItems(ShiftRule shiftRule, Map<String, Event> eventMap) {
		//get shift start times
		List<LocalTime> startTimes = toStartTimes(shiftRule.getEarliestStartTime(), shiftRule.getLatestStartTime(), shiftRule.getStartTimeIncrement());
		
		//get lunch from first shift
		EventMeta lunchEvent = getLunchEventMeta(shiftRule.getActivities(), eventMap);
		
		//get activities from first shift
		List<EventMeta> breakEvents = getBreakEventMetas(shiftRule.getActivities(), eventMap);
		
		if(shiftRule.getSplitShift() == null) {
			return new ShiftItem[] {
					new ShiftItem(shiftRule, startTimes, breakEvents, lunchEvent, shiftRule.getShiftLength())
			};
		} else if(shiftRule.getSplitShift().getActivities() == null 
				|| shiftRule.getSplitShift().getActivities().isEmpty()) {
			
			SplitShiftRule splitShift = shiftRule.getSplitShift();
			List<Duration> offSegementLengths = toOffSegements(splitShift.getOffSegmentMinUsage(), 
					splitShift.getOffSegmentMaxUsage(), splitShift.getOffSegmentIncrement());

			return new ShiftItem[] { new SplitNoActivityShiftItem(shiftRule, startTimes, breakEvents, lunchEvent, 
						shiftRule.getShiftLength(), offSegementLengths, splitShift.getShiftLength())};
		} else {
			SplitShiftRule splitShift = shiftRule.getSplitShift();
			
			List<Duration> offSegementLengths = toOffSegements(splitShift.getOffSegmentMinUsage(), 
					splitShift.getOffSegmentMaxUsage(), splitShift.getOffSegmentIncrement());
			
			ShiftItem[] shiftMetas = new ShiftItem[offSegementLengths.size()];
			
			for(int i = 0; i < offSegementLengths.size(); i++) {
				Duration offSegementLength = offSegementLengths.get(i);
				Duration firstShiftLength = offSegementLength.plus(shiftRule.getShiftLength());
				
				List<ShiftEvent> shiftEvents = splitShift.getActivities();
				List<EventMeta> tempBreakEvents = new ArrayList<>(breakEvents);
				tempBreakEvents.addAll(getBreakEventMetas(shiftEvents, firstShiftLength, eventMap));
				
				if(lunchEvent == null) {
					lunchEvent = getLunchEventMeta(shiftEvents, firstShiftLength, eventMap);
				}
				
				shiftMetas[i] = new SplitShiftItem(shiftRule, startTimes, tempBreakEvents,
						lunchEvent, shiftRule.getShiftLength(), offSegementLength, splitShift.getShiftLength());
			}
			return shiftMetas;
		}
	}

	protected static List<EventMeta> getBreakEventMetas(List<ShiftEvent> shiftEvents, 
			Map<String, Event> eventMap) {
		if(shiftEvents == null) {
			return Collections.emptyList();
		}
		
		return shiftEvents.stream()
			.filter(s -> eventMap.containsKey(s.getActivityCodeOid()) 
					&& eventMap.get(s.getActivityCodeOid()).getType() == EventType.BREAK)
			.map(EventMetaFactory::createEventMeta)
			.toList();
	}
	
	protected static List<EventMeta> getBreakEventMetas(List<ShiftEvent> shiftEvents, Duration firstShiftLength,
			Map<String, Event> eventMap) {
		if(shiftEvents == null) {
			return Collections.emptyList();
		}
		
		return shiftEvents.stream()
				.filter(s -> eventMap.containsKey(s.getActivityCodeOid()) 
						&& eventMap.get(s.getActivityCodeOid()).getType() == EventType.BREAK)
				.map(s -> EventMetaFactory.createEventMeta(s, firstShiftLength))
				.toList();
	}
	
	protected static EventMeta getLunchEventMeta(List<ShiftEvent> shiftEvents, 
			Map<String, Event> eventMap) {
		
		if(shiftEvents == null) {
			return null;
		}
		
		return shiftEvents.stream()
			.filter(s -> eventMap.containsKey(s.getActivityCodeOid()) 
					&& eventMap.get(s.getActivityCodeOid()).getType() == EventType.LUNCH)
			.map(EventMetaFactory::createEventMeta)
			.collect(toSingleton());
	}
	
	protected static EventMeta getLunchEventMeta(List<ShiftEvent> shiftEvents,  Duration firstShiftLength,
			Map<String, Event> eventMap) {
		
		if(shiftEvents == null) {
			return null;
		}
		
		return shiftEvents.stream()
				.filter(s -> eventMap.containsKey(s.getActivityCodeOid()) 
						&& eventMap.get(s.getActivityCodeOid()).getType() == EventType.LUNCH)
				.map(s -> EventMetaFactory.createEventMeta(s, firstShiftLength))
				.collect(toSingleton());
	}
	
	private static <T> Collector<T, ?, T> toSingleton() {
	    return Collectors.collectingAndThen(
	            Collectors.toList(),
	            list -> {
	                if (list.size() > 1) {
	                    throw new IllegalStateException("Duplicated items in the list. size: " + list.size());
	                } else if(list.isEmpty()) {
	                	return null;
	                }
	                return list.get(0);
	            }
	    );
	}
	
	/**
	 * create a list of start times
	 * @param earliestStartTime
	 * @param latestStart
	 * @param increment
	 * @return
	 */
	protected static List<LocalTime> toStartTimes(LocalTime earliestStartTime, LocalTime latestStart, Duration increment) {
		increment = increment != null ? increment : Duration.ZERO;

		LocalTime start = earliestStartTime;
		
		List<LocalTime> startTimesTemp = new ArrayList<>();
		do {
			startTimesTemp.add(start);
			if(increment.toMinutes() > 0) {
				start = start.plus(increment);
			} else {
				break;
			}
		} while(!start.isAfter(latestStart));
		
		return Collections.unmodifiableList(startTimesTemp);		
	}

	protected static List<Duration> toOffSegements(Duration offSegmentMinUsage, 
			Duration offSegmentMaxUsage, Duration increment) {
		
		increment = increment != null ? increment : Duration.ZERO;
		long longestSeconds = offSegmentMaxUsage.toSeconds();
		Duration start = offSegmentMinUsage;
		List<Duration> durations = new ArrayList<>();
		do {
			durations.add(start);
			
			if(increment.toMinutes() > 0) {
				start = start.plus(increment);
			} else {
				break;
			}
		} while(start.toSeconds() <= longestSeconds);
		
		return Collections.unmodifiableList(durations);		
	}
}
