package com.e2.wfm.commons.meta.common;

import com.e2.wfm.commons.meta.common.event.EventMeta;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventMetaTest {
	@Test 
	void cloneTest() {
		 EventMeta eventMeta = new  EventMeta("oid", Duration.ofHours(1), List.of(Duration.ofMinutes(15), Duration.ofMinutes(30), Duration.ofMinutes(45)));
		 
		 EventMeta clone = eventMeta.copy();
		 Assertions.assertNotSame(eventMeta, clone);
		 Assertions.assertSame(eventMeta.getOid(), clone.getOid());
		 Assertions.assertSame(eventMeta.getLength(), clone.getLength());
		 Assertions.assertNotSame(eventMeta.getStartTimes(), clone.getStartTimes());
		 
		 Assertions.assertEquals(eventMeta, clone);
		 Assertions.assertEquals(eventMeta.getStartTimes(), clone.getStartTimes());		 
	}
}
