package com.e2.wfm.commons.meta.common;

import com.e2.wfm.commons.meta.common.event.EventMeta;
import com.e2.wfm.rule.RuleFixtures;
import com.e2.wfm.rule.daily.ShiftEvent;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventMetaFactoryTest {
	@Test
	void createTest() {
		Duration duration = Duration.ofHours(2);
		//start PT2H, slack PT30M, slack increment PT15M, length PT15M
		// PT2H, PT2H15M, PT2H30M
		ShiftEvent shiftEvent = RuleFixtures.ShiftFixtures.activities.get(0);
		EventMeta eventMeta = EventMetaFactory.createEventMeta(shiftEvent);
		Assertions.assertEquals(shiftEvent.getActivityCodeOid(), eventMeta.getOid());
		Assertions.assertEquals(shiftEvent.getLength(), eventMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT2H"), Duration.parse("PT2H15M"), Duration.parse("PT2H30M")), eventMeta.getStartTimes());
						
		eventMeta = EventMetaFactory.createEventMeta(shiftEvent, duration);
		Assertions.assertEquals(shiftEvent.getActivityCodeOid(), eventMeta.getOid());
		Assertions.assertEquals(shiftEvent.getLength(), eventMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H"), Duration.parse("PT4H15M"), Duration.parse("PT4H30M")), eventMeta.getStartTimes());

		shiftEvent = RuleFixtures.ShiftFixtures.fixedActivities.get(0);
		eventMeta = EventMetaFactory.createEventMeta(shiftEvent);
		Assertions.assertEquals(shiftEvent.getActivityCodeOid(), eventMeta.getOid());
		Assertions.assertEquals(shiftEvent.getLength(), eventMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT2H")), eventMeta.getStartTimes());
			
		eventMeta = EventMetaFactory.createEventMeta(shiftEvent, duration);
		Assertions.assertEquals(shiftEvent.getActivityCodeOid(), eventMeta.getOid());
		Assertions.assertEquals(shiftEvent.getLength(), eventMeta.getLength());
		Assertions.assertEquals(List.of(Duration.parse("PT4H")), eventMeta.getStartTimes());
	}
}
