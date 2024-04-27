package com.e2.wfm.rule;

import com.e2.wfm.rule.activity.Event;
import com.e2.wfm.rule.activity.EventImpl;
import com.e2.wfm.rule.activity.EventType;
import com.e2.wfm.rule.common.IntegerRange;
import com.e2.wfm.rule.common.time.DayPattern;
import com.e2.wfm.rule.daily.*;
import com.e2.wfm.rule.weekly.ScheduleRule;
import com.e2.wfm.rule.weekly.ScheduleRuleImpl;
import com.e2.wfm.rule.weekly.ShiftMember;
import com.e2.wfm.rule.weekly.ShiftMemberImpl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class RuleFixtures {
	public interface EventFixtures {
		static final String breakOid = "break";
		static final String lunchOid = "lunch";
		static final String openOid = "open";
		
		static final Event breakEvent = EventImpl.builder()
				.oid(breakOid).name("Break")
				.type(EventType.BREAK).open(false)
				.workHours(false).build();
		
		static final Event lunchEvent = EventImpl.builder()
				.oid(lunchOid).name("Lunch")
				.type(EventType.LUNCH).open(false)
				.workHours(false).build();
		
		static final Event openEvent = EventImpl.builder()
				.oid(openOid).name("Open")
				.type(EventType.NORMAL).open(true)
				.workHours(true).build();
		static List<Event> standardActivityCodes =  List.of(breakEvent, lunchEvent, openEvent);
	}
	
	public interface ShiftFixtures {
		List<ShiftEvent> activities = List.of(
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid)
						.relativeStart(Duration.parse("PT2H"))
						.length(Duration.parse("PT15M"))
						.slack(Duration.parse("PT30M"))
						.slackIncrement(Duration.parse("PT15M")).build(),
						
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.lunchOid)
						.relativeStart(Duration.parse("PT4H"))
						.length(Duration.parse("PT1H")).slack(Duration.parse("PT30M"))
						.slackIncrement(Duration.parse("PT15M")).build(),
						
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid).relativeStart(Duration.parse("PT6H"))
						.length(Duration.parse("PT15M")).slack(Duration.parse("PT30M"))
						.slackIncrement(Duration.parse("PT30M")).build());

		List<ShiftEvent> firstActivities = List.of(
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid)
				.relativeStart(Duration.parse("PT2H"))
				.length(Duration.parse("PT15M"))
				.slack(Duration.parse("PT30M"))
				.slackIncrement(Duration.parse("PT15M")).build(),
				
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.lunchOid)
				.relativeStart(Duration.parse("PT4H"))
				.length(Duration.parse("PT1H")).slack(Duration.parse("PT30M"))
				.slackIncrement(Duration.parse("PT15M")).build());
		
		List<ShiftEvent> secondActivities = List.of(
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid)
				.relativeStart(Duration.parse("PT1H"))
				.length(Duration.parse("PT15M"))
				.slack(Duration.parse("PT30M"))
				.slackIncrement(Duration.parse("PT15M")).build());
		
		
		List<ShiftEvent> fixedActivities = List.of(
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid).relativeStart(Duration.parse("PT2H"))
						.length(Duration.parse("PT15M")).slack(Duration.ZERO).slackIncrement(Duration.ZERO).build(),
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.lunchOid).relativeStart(Duration.parse("PT4H"))
						.length(Duration.parse("PT1H")).slack(Duration.ZERO).slackIncrement(Duration.ZERO).build(),
				ShiftEventImpl.builder().activityCodeOid(EventFixtures.breakOid).relativeStart(Duration.parse("PT6H"))
						.length(Duration.parse("PT15M")).slack(Duration.ZERO).slackIncrement(Duration.ZERO).build());
		
		
		static final String shiftOid1 = "shift1";
		static final String shiftOid2 = "shift2";
		static final String shiftOid3 = "shift3";

		static final String fixedDailyOid = "fixedShift";
		

		ShiftRule shift1 = ShiftRuleImpl.builder()
				.oid(shiftOid1)
				.earliestStartTime(LocalTime.parse("06:00:00"))
				.latestStartTime(LocalTime.parse("07:00:00"))
				.shiftLength(Duration.ofHours(8))
				.startTimeIncrement(Duration.parse("PT30M"))
				.activities(activities)
				.build();

		ShiftRule shift2 = ShiftRuleImpl.builder()
				.oid(shiftOid2)
				.earliestStartTime(LocalTime.parse("07:00:00"))
				.latestStartTime(LocalTime.parse("08:00:00"))
				.shiftLength(Duration.ofHours(9))
				.startTimeIncrement(Duration.parse("PT30M"))
				.activities(activities)
				.build();
		
		ShiftRule shift3 = ShiftRuleImpl.builder()
				.oid(shiftOid3)
				.earliestStartTime(LocalTime.parse("08:00:00"))
				.latestStartTime(LocalTime.parse("09:00:00"))
				.shiftLength(Duration.ofHours(10))
				.startTimeIncrement(Duration.parse("PT30M"))
				.activities(activities)
				.build();
		
		ShiftRule fixedShift = ShiftRuleImpl.builder()
				.oid(fixedDailyOid)
				.earliestStartTime(LocalTime.parse("08:00:00"))
				.latestStartTime(LocalTime.parse("08:00:00"))
				.shiftLength(Duration.ofHours(8))
				.startTimeIncrement(Duration.ZERO)
				.activities(fixedActivities)
				.build();
		
		
		ShiftRule splitShiftNoActivity = ShiftRuleImpl.builder()
				.oid(shiftOid1)
				.earliestStartTime(LocalTime.parse("06:00:00"))
				.latestStartTime(LocalTime.parse("07:00:00"))
				.shiftLength(Duration.ofHours(6))
				.startTimeIncrement(Duration.parse("PT30M"))
				.activities(activities)
				.splitShift(
						SplitShiftRuleImpl.builder()
						.shiftLength(Duration.ofHours(4))
						.offSegmentIncrement(Duration.ofMinutes(15))
						.offSegmentMinUsage(Duration.ofMinutes(30))
						.offSegmentMaxUsage(Duration.ofMinutes(60))
						.build())
				.build();
		
		ShiftRule splitShift = ShiftRuleImpl.builder()
				.oid(shiftOid1)
				.earliestStartTime(LocalTime.parse("06:00:00"))
				.latestStartTime(LocalTime.parse("07:00:00"))
				.shiftLength(Duration.ofHours(6))
				.startTimeIncrement(Duration.parse("PT30M"))
				.activities(firstActivities)
				.splitShift(
						SplitShiftRuleImpl.builder()
						.shiftLength(Duration.ofHours(4))
						.offSegmentIncrement(Duration.ofMinutes(15))
						.offSegmentMinUsage(Duration.ofMinutes(30))
						.offSegmentMaxUsage(Duration.ofMinutes(60))
						.activities(secondActivities)
						.build())
				.build();
		
		List<ShiftRule> shifts = List.of(shift1, shift2, shift3, fixedShift);
	}
	
	public interface ScheduleFixtures {
		List<ShiftMember> fixedRuleMember = List.of(
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.fixedDailyOid)
				.usage(IntegerRange.builder().max(5).min(5).build())
				.allowableDays(DayPattern.MON_TUE_WED_THU_FRI).build());
		
		ScheduleRule fixedScheduleRule = ScheduleRuleImpl.builder()
				.oid("fixedScheduleRule")
				.usage(IntegerRange.builder().max(5).min(5).build())
				.dailyRuleMembers(fixedRuleMember)
				.build();
		
		List<ShiftMember> twoDailyRuleMember = List.of(
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.shiftOid1)
						.usage(IntegerRange.builder().max(5).min(2).build())
						.allowableDays(DayPattern.SUN_SAT).build(),
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.shiftOid3)
						.usage(IntegerRange.builder().max(3).min(1).build())
						.allowableDays(DayPattern.MON_TUE_WED_THU_FRI)
						.build());

		List<ShiftMember> threeDailyRuleMember = List.of(
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.shiftOid1)
				.usage(IntegerRange.builder().max(5).min(2).build())
				.allowableDays(DayPattern.SUN_TUE_WED_THU_SAT).build(),
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.shiftOid2)
				.usage(IntegerRange.builder().max(4).min(2).build())
				.allowableDays(DayPattern.MON_TUE_THU_FRI)
				.build(),
				ShiftMemberImpl.builder().dailyRuleOid(ShiftFixtures.shiftOid3)
				.usage(IntegerRange.builder().max(3).min(1).build())
				.allowableDays(DayPattern.MON_TUE_WED_FRI)
				.build());
		
		ScheduleRule twoShiftScheduleRule1 = ScheduleRuleImpl.builder()
				.oid("twoShifts1")
				.name("twoShifts1")
				.usage(IntegerRange.builder().max(7).min(1).build())
				.dailyRuleMembers(twoDailyRuleMember)
				.build();
	
		ScheduleRule twoShiftScheduleRule2 = ScheduleRuleImpl.builder()
				.oid("twoShifts2")
				.name("twoShifts2")
				.consecutiveDaysOn(IntegerRange.builder().min(2).max(7).build())
				.usage(IntegerRange.builder().max(6).min(3).build())
				.dailyRuleMembers(twoDailyRuleMember)
				.build();
		
		ScheduleRule threeShiftScheduleRule1 = ScheduleRuleImpl.builder()
				.oid("threeShifts1")
				.name("threeShifts1")
				.usage(IntegerRange.builder().max(7).min(1).build())
				.dailyRuleMembers(threeDailyRuleMember)
				.build();
		
		ScheduleRule threeShiftScheduleRule2 = ScheduleRuleImpl.builder()
				.oid("threeShifts2")
				.name("threeShifts2")
				.consecutiveDaysOff(IntegerRange.builder().min(1).max(1).build())
				.usage(IntegerRange.builder().max(6).min(3).build())
				.dailyRuleMembers(threeDailyRuleMember)
				.build();
	}
}
