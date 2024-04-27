package com.e2.wfm.commons.meta.common;

import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.commons.meta.common.shift.ShiftItem;
import com.e2.wfm.commons.meta.common.shift.ShiftMeta;
import com.e2.wfm.commons.meta.common.shift.SplitNoActivityShiftItem;
import com.e2.wfm.commons.meta.common.shift.SplitShiftItem;
import com.e2.wfm.rule.RuleFixtures;
import com.e2.wfm.rule.RuleFixtures.EventFixtures;
import com.e2.wfm.rule.activity.Event;
import com.e2.wfm.rule.daily.ShiftEvent;
import com.e2.wfm.rule.daily.ShiftEventImpl;
import com.e2.wfm.rule.daily.ShiftRule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShiftMetaFactoryTest {

	@Test
	void toOffSegementsTest() {
		Duration offSegmentMinUsage = Duration.ofMinutes(30);
		Duration offSegmentMaxUsage = Duration.ofMinutes(120);
		Duration increment = Duration.ofMinutes(15);
		
		//30, 45, 1h, 1h15, 1h30, 1h45, 2h
		List<Duration> toOffSegements = ShiftMetaFactory.toOffSegements(offSegmentMinUsage, offSegmentMaxUsage, increment);
		Assertions.assertEquals(List.of(Duration.parse("PT30M"), Duration.parse("PT45M"), 
					Duration.parse("PT1H"), Duration.parse("PT1H15M"), Duration.parse("PT1H30M"),
					Duration.parse("PT1H45M"), Duration.parse("PT2H")), 
				toOffSegements);
	}	
	
	@Test
	void toStartTimesTest() {
		LocalTime earliestStartTime = LocalTime.of(8, 0);
		LocalTime latestStart = LocalTime.of(9, 0);
		Duration increment = Duration.ofMinutes(15);
		// 8, 8:15, 8:30, 8:45, 9:00
		List<LocalTime> startTimes = ShiftMetaFactory.toStartTimes(earliestStartTime, latestStart, increment);
		Assertions.assertEquals(List.of(LocalTime.of(8, 0), LocalTime.of(8, 15), LocalTime.of(8, 30), LocalTime.of(8, 45), LocalTime.of(9, 0)), 
				startTimes);
	}
	
	@Test
	void getBreakEventMetasTest() {
		Map<String, Event> eventMap  = RuleFixtures.EventFixtures.standardActivityCodes
					.stream().collect(Collectors.toMap(Event::getOid, Function.identity()));

		List<EventMeta> eventMetas = ShiftMetaFactory.getBreakEventMetas(Collections.emptyList(), eventMap);
		Assertions.assertNotNull(eventMetas);
		Assertions.assertTrue(eventMetas.isEmpty());
		
		List<ShiftEvent> activities  = RuleFixtures.ShiftFixtures.activities;
		eventMetas = ShiftMetaFactory.getBreakEventMetas(activities, eventMap);
		
		Assertions.assertEquals(2, eventMetas.size());
		
		Assertions.assertEquals(EventFixtures.breakOid, eventMetas.get(0).getOid());
		Assertions.assertEquals(Duration.parse("PT15M"), eventMetas.get(0).getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT2H"), Duration.parse("PT2H15M"), Duration.parse("PT2H30M")), eventMetas.get(0).getStartTimes());
		
		Assertions.assertEquals(EventFixtures.breakOid, eventMetas.get(1).getOid());
		Assertions.assertEquals(Duration.parse("PT15M"), eventMetas.get(1).getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT6H"), Duration.parse("PT6H30M")), eventMetas.get(1).getStartTimes());

		eventMetas = ShiftMetaFactory.getBreakEventMetas(activities, Duration.ofHours(2), eventMap);
		
		Assertions.assertEquals(EventFixtures.breakOid, eventMetas.get(0).getOid());
		Assertions.assertEquals(Duration.parse("PT15M"), eventMetas.get(0).getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H"), Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), eventMetas.get(0).getStartTimes());
		
		Assertions.assertEquals(EventFixtures.breakOid, eventMetas.get(1).getOid());
		Assertions.assertEquals(Duration.parse("PT15M"), eventMetas.get(1).getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT8H"), Duration.parse("PT8H30M")), eventMetas.get(1).getStartTimes());
		
		
		eventMetas = ShiftMetaFactory.getBreakEventMetas(activities, Collections.emptyMap());
		Assertions.assertNotNull(eventMetas);
		Assertions.assertTrue(eventMetas.isEmpty());
	}
	
	@Test
	void getLunchEventMetaTest() {
		Map<String, Event> eventMap  = RuleFixtures.EventFixtures.standardActivityCodes
				.stream().collect(Collectors.toMap(Event::getOid, Function.identity()));
		
		EventMeta lunchMeta = ShiftMetaFactory.getLunchEventMeta(Collections.emptyList(), eventMap);
		Assertions.assertNull(lunchMeta);
		
		List<ShiftEvent> activities  = RuleFixtures.ShiftFixtures.activities;
		lunchMeta = ShiftMetaFactory.getLunchEventMeta(activities, eventMap);
		Assertions.assertNotNull(lunchMeta);
	
		Assertions.assertEquals(EventFixtures.lunchOid, lunchMeta.getOid());
		Assertions.assertEquals(Duration.parse("PT1H"), lunchMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H"), Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), lunchMeta.getStartTimes());

		lunchMeta = ShiftMetaFactory.getLunchEventMeta(activities, Duration.ofHours(1), eventMap);
		Assertions.assertNotNull(lunchMeta);
		
		Assertions.assertEquals(EventFixtures.lunchOid, lunchMeta.getOid());
		Assertions.assertEquals(Duration.parse("PT1H"), lunchMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT5H"), Duration.parse("PT5H15M"), Duration.parse("PT5H30M")), lunchMeta.getStartTimes());

		activities = new ArrayList<>(RuleFixtures.ShiftFixtures.activities);
		activities.add(	ShiftEventImpl.builder().activityCodeOid(EventFixtures.lunchOid)
				.relativeStart(Duration.parse("PT5H"))
				.length(Duration.parse("PT1H")).slack(Duration.parse("PT30M"))
				.slackIncrement(Duration.parse("PT15M")).build()
				);
		
		try {
			lunchMeta = ShiftMetaFactory.getLunchEventMeta(activities, eventMap);
			Assertions.fail("Should throw IllegalStateException.");
		} catch(IllegalStateException e) {
			
		}
	}
	
	@Test
	void createShiftMetaTest() {
		Map<String, Event> eventMap  = RuleFixtures.EventFixtures.standardActivityCodes
				.stream().collect(Collectors.toMap(Event::getOid, Function.identity()));
		
		ShiftRule shiftRule = RuleFixtures.ShiftFixtures.shift1;
		ShiftMeta shiftMeta = ShiftMetaFactory.createShiftMeta(shiftRule, eventMap);
		
		Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftMeta.getEarliestStartTime());
		Assertions.assertEquals(LocalTime.parse("07:00:00").plus(Duration.ofHours(8)), shiftMeta.getLatestEndTime());

		ShiftItem[] shiftItems = shiftMeta.getShiftItems();
		
		Assertions.assertEquals(1, shiftItems.length);
		
		ShiftItem shiftItem = shiftItems[0];
		
		Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftItem.getEarliestStartTime());
		Assertions.assertEquals(LocalTime.parse("07:00:00").plus(Duration.ofHours(8)), shiftItem.getLatestEndTime());

		Assertions.assertSame(shiftRule, shiftMeta.getEntity());
		Assertions.assertEquals(ShiftItem.class.getName(), shiftItem.getClass().getName());
		Assertions.assertEquals(shiftRule.getShiftLength(), shiftItem.getShfitLength());
		// start: 6:00, 6:30, 7:00		
		Assertions.assertEquals(List.of(LocalTime.of(6, 0), LocalTime.of(6, 30), LocalTime.of(7, 0)), 
				shiftItem.getStartTimes());
		
		// breaks: { {2H, 2H15M, 2H30M}, {6H, 6H30M} }
		Assertions.assertEquals(2, shiftItem.getBreakEvents().size());
		EventMeta breakMeta = shiftItem.getBreakEvents().get(0);
		Assertions.assertEquals(List.of(Duration.parse("PT2H"), 
				Duration.parse("PT2H15M"), Duration.parse("PT2H30M")), breakMeta.getStartTimes());
		Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
		Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());
		
		breakMeta = shiftItem.getBreakEvents().get(1);
		Assertions.assertEquals(List.of(Duration.parse("PT6H"), 
				Duration.parse("PT6H30M")), breakMeta.getStartTimes());
		Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
		Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());

		// lunch: {4H, 4H15, 4H30M}
		EventMeta lunchEvent =  shiftItem.getLunchEvent();
		
		Assertions.assertEquals(EventFixtures.lunchOid, lunchEvent.getOid());
		Assertions.assertEquals(Duration.parse("PT1H"), lunchEvent.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H"), 
				Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), lunchEvent.getStartTimes());

	}

	@Test
	void createSplitNoActShiftMetaTest() {
		Map<String, Event> eventMap  = RuleFixtures.EventFixtures.standardActivityCodes
				.stream().collect(Collectors.toMap(Event::getOid, Function.identity()));
		
		ShiftRule shiftRule = RuleFixtures.ShiftFixtures.splitShiftNoActivity;

		ShiftMeta shiftMeta = ShiftMetaFactory.createShiftMeta(shiftRule, eventMap);
		
		Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftMeta.getEarliestStartTime());
		Assertions.assertEquals(LocalTime.parse("07:00:00").plus(Duration.ofHours(6)).plus(Duration.ofHours(4)).plus(Duration.ofHours(1)), 
				shiftMeta.getLatestEndTime());

		ShiftItem[] shiftItems = shiftMeta.getShiftItems();
		Assertions.assertEquals(1, shiftItems.length);		
		Assertions.assertEquals(SplitNoActivityShiftItem.class.getName(), shiftItems[0].getClass().getName());
		SplitNoActivityShiftItem shiftItem = (SplitNoActivityShiftItem)shiftItems[0];
		
		Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftItem.getEarliestStartTime());
		Assertions.assertEquals(LocalTime.parse("07:00:00").plus(Duration.ofHours(6)).plus(Duration.ofHours(4)).plus(Duration.ofHours(1)), 
				shiftItem.getLatestEndTime());
		
		Assertions.assertSame(shiftRule, shiftItem.getEntity());

		// start: 6:00, 6:30, 7:00		
		Assertions.assertEquals(List.of(LocalTime.of(6, 0), LocalTime.of(6, 30), LocalTime.of(7, 0)), 
				shiftItem.getStartTimes());
		
		Assertions.assertEquals(shiftRule.getShiftLength(), shiftItem.getShfitLength());
		Assertions.assertEquals(shiftRule.getSplitShift().getShiftLength(), shiftItem.getSplitShfitLength());

		// offsegment: 30, 45, 60
		Assertions.assertEquals(List.of(Duration.parse("PT30M"), 
				Duration.parse("PT45M"), Duration.parse("PT1H")), shiftItem.getOffSegementLengths());

		// breaks: { {2H, 2H15M, 2H30M}, {6H, 6H30M} }
		Assertions.assertEquals(2, shiftItem.getBreakEvents().size());
		EventMeta breakMeta = shiftItem.getBreakEvents().get(0);
		Assertions.assertEquals(List.of(Duration.parse("PT2H"), 
				Duration.parse("PT2H15M"), Duration.parse("PT2H30M")), breakMeta.getStartTimes());
		Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
		Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());
		
		breakMeta = shiftItem.getBreakEvents().get(1);
		Assertions.assertEquals(List.of(Duration.parse("PT6H"), 
				Duration.parse("PT6H30M")), breakMeta.getStartTimes());
		Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
		Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());

		// lunch: {4H, 4H15, 4H30M}
		EventMeta lunchEvent =  shiftItem.getLunchEvent();
		
		Assertions.assertEquals(EventFixtures.lunchOid, lunchEvent.getOid());
		Assertions.assertEquals(Duration.parse("PT1H"), lunchEvent.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H"), 
				Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), lunchEvent.getStartTimes());
	}
	
	@Test
	void createSplitShiftMetaTest() {
		Map<String, Event> eventMap  = RuleFixtures.EventFixtures.standardActivityCodes
				.stream().collect(Collectors.toMap(Event::getOid, Function.identity()));
		
		ShiftRule shiftRule = RuleFixtures.ShiftFixtures.splitShift;
		
		ShiftMeta shiftMeta = ShiftMetaFactory.createShiftMeta(shiftRule, eventMap);
		
		Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftMeta.getEarliestStartTime());
		Assertions.assertEquals(LocalTime.parse("07:00:00").plus(Duration.ofHours(6)).plus(Duration.ofHours(4)).plus(Duration.ofHours(1)), 
				shiftMeta.getLatestEndTime());
		
		ShiftItem[] shiftItems = shiftMeta.getShiftItems();
		
		Assertions.assertEquals(3, shiftItems.length);	
		// offsegment: 30, 45, 60
		for(int i = 0; i <shiftItems.length; i++) {
			Assertions.assertEquals(SplitShiftItem.class.getName(), shiftItems[i].getClass().getName());
			SplitShiftItem shiftItem = (SplitShiftItem)shiftItems[i];
			
			Assertions.assertSame(shiftRule, shiftItem.getEntity());
			Assertions.assertEquals(shiftRule.getShiftLength(), shiftItem.getShfitLength());
			Assertions.assertEquals(shiftRule.getShiftLength().plus(shiftRule.getSplitShift().getShiftLength()), 
					shiftItem.getTotalShfitLength());
			
			Assertions.assertEquals(List.of(LocalTime.of(6, 0), LocalTime.of(6, 30), LocalTime.of(7, 0)), 
					shiftItem.getStartTimes());
			
			Assertions.assertEquals(shiftRule.getSplitShift().getShiftLength(), shiftItem.getSplitShfitLength());
			
			// lunch: {4H, 4H15, 4H30M}
			EventMeta lunchEvent =  shiftItem.getLunchEvent();
			Assertions.assertEquals(EventFixtures.lunchOid, lunchEvent.getOid());
			Assertions.assertEquals(Duration.parse("PT1H"), lunchEvent.getLength());
			Assertions.assertEquals(List.of(Duration.parse("PT4H"), 
					Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), lunchEvent.getStartTimes());
			
			// breaks: { {2H, 2H15M, 2H30M}, {6H, 6H30M} }
			Assertions.assertEquals(2, shiftItem.getBreakEvents().size());
			EventMeta breakMeta = shiftItem.getBreakEvents().get(0);
			Assertions.assertEquals(List.of(Duration.parse("PT2H"), 
					Duration.parse("PT2H15M"), Duration.parse("PT2H30M")), breakMeta.getStartTimes());
			Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
			Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());
			
			breakMeta = shiftItem.getBreakEvents().get(1);
			Assertions.assertEquals(Duration.parse("PT15M"), breakMeta.getLength());
			Assertions.assertEquals(EventFixtures.breakOid, breakMeta.getOid());
			Assertions.assertEquals(LocalTime.parse("06:00:00"), shiftItem.getEarliestStartTime());

			if(i == 0) {
				Assertions.assertEquals(Duration.parse("PT30M"), shiftItem.getOffSegementLength());
				Assertions.assertEquals(List.of(LocalTime.of(16, 30), 
						LocalTime.of(17, 0), 
						LocalTime.of(17, 30)), 
						shiftItem.getEndTimes());

				Assertions.assertEquals(List.of(Duration.parse("PT7H30M"), 
						Duration.parse("PT7H45M"), Duration.parse("PT8H")), 
						breakMeta.getStartTimes());
				
				Assertions.assertEquals(LocalTime.parse("07:00:00")
						.plus(Duration.ofHours(6))
						.plus(Duration.ofHours(4))
						.plus(Duration.ofMinutes(30)), 
						shiftItem.getLatestEndTime());
				
			} else if(i ==1) {
				Assertions.assertEquals(Duration.parse("PT45M"), shiftItem.getOffSegementLength());
				Assertions.assertEquals(List.of(LocalTime.of(16, 45), 
						LocalTime.of(17, 15), 
						LocalTime.of(17, 45)), 
						shiftItem.getEndTimes());
				
				Assertions.assertEquals(List.of(Duration.parse("PT7H45M"), 
						Duration.parse("PT8H"), Duration.parse("PT8H15M")), 
						breakMeta.getStartTimes());
				
				Assertions.assertEquals(LocalTime.parse("07:00:00")
						.plus(Duration.ofHours(6))
						.plus(Duration.ofHours(4))
						.plus(Duration.ofMinutes(45)), 
						shiftItem.getLatestEndTime());				
			} else if(i == 2) {
				Assertions.assertEquals(Duration.parse("PT60M"), shiftItem.getOffSegementLength());
				Assertions.assertEquals(List.of(LocalTime.of(17, 0), 
						LocalTime.of(17, 30), 
						LocalTime.of(18, 00)), 
						shiftItem.getEndTimes());
				
				Assertions.assertEquals(List.of(Duration.parse("PT8H"), 
						Duration.parse("PT8H15M"), Duration.parse("PT8H30M")), 
						breakMeta.getStartTimes());		
				
				Assertions.assertEquals(LocalTime.parse("07:00:00")
						.plus(Duration.ofHours(6))
						.plus(Duration.ofHours(4))
						.plus(Duration.ofHours(1)), 
						shiftItem.getLatestEndTime());				
			}
		}
	}
}
