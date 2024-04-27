package com.e2.wfm.commons.pattern;

import com.e2.wfm.rule.RuleConstants;
import com.e2.wfm.rule.RuleFixtures;
import com.e2.wfm.rule.RuleFixtures.ShiftFixtures;
import com.e2.wfm.rule.weekly.ScheduleRule;

import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PatternGeneratorTest {
	
	@Test
	void fixedRuleGeneratorTest() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.fixedScheduleRule;
		//PatternDataBus dataBus = new PatternDataBus(schedule);
		PatternGenerator generator = PatternGenerator.createPatternGenerator(schedule);
		generator.run();
		
		//List<String[]> results = dataBus.getOidResults();
		
		ScheduleListener listener = generator.getListener();
		
		Assertions.assertTrue(listener.isCompleted());
				
		String[] oids = listener.getNextOids();
		Assertions.assertEquals(RuleConstants.OFF_OID, oids[0]);
		Assertions.assertEquals(RuleConstants.OFF_OID, oids[6]);
		for(int i = 1; i < 6; i++) {
			Assertions.assertEquals(ShiftFixtures.fixedDailyOid, oids[i]);			
		}
		
		Assertions.assertTrue(listener.isEOF());
		
		oids = listener.getNextOids();
		Assertions.assertNull(oids);
	}
	
	@Test
	void twoShifts1RuleGeneratorTest() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.twoShiftScheduleRule1;
		
		PatternGenerator generator = PatternGenerator.createPatternGenerator(schedule);
		
		ScheduleListener listener = generator.getListener();
		
		Thread thread = new Thread(generator);
		thread.start();
		
		List<String[]> results = new ArrayList<>();
		
		String[] oids = null;
		while(!listener.isEOF()) {
			oids = listener.getNextOids();
			if(oids != null) {
				results.add(oids);
			}
		}

		Map<Integer, String> idToOidMap = new HashMap<>();
		idToOidMap.put(0, RuleConstants.OFF_OID);
		idToOidMap.put(1, ShiftFixtures.shiftOid1);
		idToOidMap.put(2, ShiftFixtures.shiftOid2);
		
		Assertions.assertEquals(25, results.size());
		
		int[][] expected = {{1, 0, 0, 0, 0, 2, 1},
				{1, 0, 0, 0, 2, 0, 1},
				{1, 0, 0, 0, 2, 2, 1},
				{1, 0, 0, 2, 0, 0, 1},
				{1, 0, 0, 2, 0, 2, 1},
				{1, 0, 0, 2, 2, 0, 1},
				{1, 0, 0, 2, 2, 2, 1},
				{1, 0, 2, 0, 0, 0, 1},
				{1, 0, 2, 0, 0, 2, 1},
				{1, 0, 2, 0, 2, 0, 1},
				{1, 0, 2, 0, 2, 2, 1},
				{1, 0, 2, 2, 0, 0, 1},
				{1, 0, 2, 2, 0, 2, 1},
				{1, 0, 2, 2, 2, 0, 1},
				{1, 2, 0, 0, 0, 0, 1},
				{1, 2, 0, 0, 0, 2, 1},
				{1, 2, 0, 0, 2, 0, 1},
				{1, 2, 0, 0, 2, 2, 1},
				{1, 2, 0, 2, 0, 0, 1},
				{1, 2, 0, 2, 0, 2, 1},
				{1, 2, 0, 2, 2, 0, 1},
				{1, 2, 2, 0, 0, 0, 1},
				{1, 2, 2, 0, 0, 2, 1},
				{1, 2, 2, 0, 2, 0, 1},
				{1, 2, 2, 2, 0, 0, 1}};

		for(int i = 0; i < results.size(); i++) {
			oids = results.get(i);
			for(int j = 0; j < oids.length; j++) {
				String expectedOid = idToOidMap.get(expected[i][j]);
				Assertions.assertEquals(expectedOid, oids[j], "(i, j)= (" + i + ", " + j + ")");
			}
		}
	}
	
	private List<String[]> sort(List<String[]> results) {
		results = new ArrayList<>(results);
		Collections.sort(results, (o1, o2) -> {
			int sortNum = Math.min(o1.length, o2.length);
			for(int i = 0; i < sortNum; i++) {
				int r = o1[i].compareTo(o2[i]);
				if(r != 0) {
					return r;
				}
			}
			return 0;
		});
		return results;
	}
	
	private List<int[]> sortInt(List<int[]> results) {
		
		results = new ArrayList<>(results);
		Collections.sort(results, (o1, o2) -> {
			int sortNum = Math.min(o1.length, o2.length);
			for(int i = 0; i < sortNum; i++) {
				int r = o1[i] - o2[i];
				if(r != 0) {
					return r;
				}
			}
			return 0;
		});
		return results;
	}
}
