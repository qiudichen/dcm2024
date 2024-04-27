package com.e2.wfm.commons.pattern;

import com.e2.wfm.commons.pattern.rule.ShiftUsageRule;
import com.e2.wfm.rule.RuleConstants;
import com.e2.wfm.rule.RuleFixtures;
import com.e2.wfm.rule.RuleFixtures.ShiftFixtures;
import com.e2.wfm.rule.common.IntegerRange;
import com.e2.wfm.rule.weekly.ScheduleRule;
import com.e2.wfm.rule.weekly.ShiftMember;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatternDataBusTest {
	@Test
	void fixedRuleTest() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.fixedScheduleRule;

		PatternDataBus dataBus = new PatternDataBus(schedule);
		
		assertUsage(schedule, dataBus);
		
		Assertions.assertNull(dataBus.getConsecutiveDaysOff()); 
		Assertions.assertNull(dataBus.getConsecutiveDaysOn()); 
		
		//assert selectable elements
		String oid = RuleFixtures.ShiftFixtures.fixedShift.getOid();

		int[][] elements = dataBus.getSelectableIds();
		
		Assertions.assertEquals(1, elements[0].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[0][0]);
		for(int i = 1; i < elements.length - 1; i++) {
			Assertions.assertEquals(2, elements[i].length);
			Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
			Assertions.assertEquals(1, elements[i][1]);
			String actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
			Assertions.assertEquals(oid, actualOid);
		}
		Assertions.assertEquals(1, elements[elements.length - 1].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[0][0]);
	}

	@Test
	void twoShift1Test() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.twoShiftScheduleRule1;
		PatternDataBus dataBus = new PatternDataBus(schedule);
		assertUsage(schedule, dataBus);
		
		Assertions.assertNull(dataBus.getConsecutiveDaysOff()); 
		Assertions.assertNull(dataBus.getConsecutiveDaysOn()); 
		
		assertTwoDailRueMemeber(dataBus);				
	}
	
	@Test
	void twoShift2Test() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.twoShiftScheduleRule2;
		PatternDataBus dataBus = new PatternDataBus(schedule);
		assertUsage(schedule, dataBus);
		
		Assertions.assertNull(dataBus.getConsecutiveDaysOff()); 
		IntegerRange daysOn = dataBus.getConsecutiveDaysOn();
		Assertions.assertNotNull(dataBus.getConsecutiveDaysOn());
		Assertions.assertEquals(7, daysOn.getMax());
		Assertions.assertEquals(2, daysOn.getMin());
		
		assertTwoDailRueMemeber(dataBus);		
	}

	@Test
	void threeShift1Test() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.threeShiftScheduleRule1;
		PatternDataBus dataBus = new PatternDataBus(schedule);
		assertUsage(schedule, dataBus);
		
		Assertions.assertNull(dataBus.getConsecutiveDaysOff()); 
		Assertions.assertNull(dataBus.getConsecutiveDaysOn()); 
		assertthreeDailRueMemeber(dataBus);
	}
	
	@Test
	void threeShift2Test() {
		ScheduleRule schedule = RuleFixtures.ScheduleFixtures.threeShiftScheduleRule2;
		PatternDataBus dataBus = new PatternDataBus(schedule);
		assertUsage(schedule, dataBus);
		
		IntegerRange dayOff = dataBus.getConsecutiveDaysOff();
		Assertions.assertNotNull(dayOff); 
		Assertions.assertEquals(1, dayOff.getMax());
		Assertions.assertEquals(1, dayOff.getMin());
		Assertions.assertNull(dataBus.getConsecutiveDaysOn());
		
		assertthreeDailRueMemeber(dataBus);
	}
	
	private void assertUsage(ScheduleRule schedule, PatternDataBus dataBus) {
		Map<String, Integer> oidToIdMap = dataBus.getIdToOidMap().entrySet().stream()
				.collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
		//assert usage rule
		Assertions.assertEquals(schedule.getDailyRuleMembers().size() + 1, dataBus.getShiftUsageRuleSize());
		
		ShiftUsageRule offRule = dataBus.getShiftUsageRule(RuleConstants.OFF_VAL);
		Assertions.assertEquals(7 - schedule.getUsage().getMin(), offRule.getMaxUsage());
		Assertions.assertEquals(7 - schedule.getUsage().getMax(), offRule.getMinUsage());
		Assertions.assertEquals(RuleConstants.OFF_VAL, offRule.getShiftId());
		
		for(ShiftMember shift : schedule.getDailyRuleMembers()) {
			String oid = shift.getDailyRuleOid();
			int id = oidToIdMap.get(oid);
			ShiftUsageRule rule = dataBus.getShiftUsageRule(id);
			IntegerRange usage = shift.getUsage();
			Assertions.assertEquals(usage.getMax(), rule.getMaxUsage());
			Assertions.assertEquals(usage.getMin(), rule.getMinUsage());
			Assertions.assertEquals(id, rule.getShiftId());
		}
	}
	
	
	private void assertTwoDailRueMemeber(PatternDataBus dataBus) {
		int[][] elements = dataBus.getSelectableIds();
		
		for(int i = 0; i < 7; i++) {
			Assertions.assertEquals(2, elements[i].length);
			Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
			for(int j = 1; j < 2; j++) {
				String actualOid = dataBus.getIdToOidMap().get(elements[i][j]);
				if(i == 0 || i == 6) {
					Assertions.assertEquals(1, elements[i][j]);
					Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);
				} else {
					Assertions.assertEquals(2, elements[i][j]);
					Assertions.assertEquals(ShiftFixtures.shiftOid3, actualOid);					
				}
			}
		}
	}
	
	private void assertthreeDailRueMemeber(PatternDataBus dataBus) {
		int[][] elements = dataBus.getSelectableIds();
		
		String actualOid = null;
		int i = 0; //SUN
		Assertions.assertEquals(2, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(1, elements[i][1]);

		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);

		i = 1; //MON
		Assertions.assertEquals(3, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(2, elements[i][1]);
		Assertions.assertEquals(3, elements[i][2]);

		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid2, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][2]);
		Assertions.assertEquals(ShiftFixtures.shiftOid3, actualOid);
		
		i = 2; //TUE
		Assertions.assertEquals(4, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(1, elements[i][1]);
		Assertions.assertEquals(2, elements[i][2]);
		Assertions.assertEquals(3, elements[i][3]);
		
		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][2]);
		Assertions.assertEquals(ShiftFixtures.shiftOid2, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][3]);
		Assertions.assertEquals(ShiftFixtures.shiftOid3, actualOid);
		
		i = 3; //WED
		Assertions.assertEquals(3, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(1, elements[i][1]);
		Assertions.assertEquals(3, elements[i][2]);
		
		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][2]);
		Assertions.assertEquals(ShiftFixtures.shiftOid3, actualOid);
		
		i = 4; //THU
		Assertions.assertEquals(3, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(1, elements[i][1]);
		Assertions.assertEquals(2, elements[i][2]);
		
		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][2]);
		Assertions.assertEquals(ShiftFixtures.shiftOid2, actualOid);
		
		i = 5; //FRI
		Assertions.assertEquals(3, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(2, elements[i][1]);
		Assertions.assertEquals(3, elements[i][2]);
		
		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid2, actualOid);
		actualOid = dataBus.getIdToOidMap().get(elements[i][2]);
		Assertions.assertEquals(ShiftFixtures.shiftOid3, actualOid);
		
		i = 6; //SAT
		Assertions.assertEquals(2, elements[i].length);
		Assertions.assertEquals(RuleConstants.OFF_VAL, elements[i][0]);
		Assertions.assertEquals(1, elements[i][1]);
		
		actualOid = dataBus.getIdToOidMap().get(elements[i][1]);
		Assertions.assertEquals(ShiftFixtures.shiftOid1, actualOid);
	}	
}
