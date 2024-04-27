package com.e2.wfm.rule.weekly;

import com.e2.wfm.commons.utils.SequenceGenerator;
import com.e2.wfm.rule.common.IntegerRange;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Duration;
import java.util.List;

import lombok.*;

@Getter
@Builder(builderClassName = "builder")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class ScheduleRuleImpl implements ScheduleRule {
	
	private static SequenceGenerator generator = new SequenceGenerator();
	
	private String oid;
	private Long id;
    private String name;
	private IntegerRange usage;
    private boolean consistentStartTimes;
	private boolean consistentLunchTimes;
    private boolean consistentBreakTimes;
	private IntegerRange consecutiveDaysOff;
	private IntegerRange consecutiveDaysOn;
    private Duration minHoursBetweenShifts;
    private List<ShiftMember> dailyRuleMembers;
    
	public ScheduleRuleImpl(String oid, Long id, String name, IntegerRange usage, boolean consistentStartTimes,
			boolean consistentLunchTimes, boolean consistentBreakTimes, IntegerRange consecutiveDaysOff,
			IntegerRange consecutiveDaysOn, Duration minHoursBetweenShifts, List<ShiftMember> dailyRuleMembers) {
		super();
		this.oid = oid;
		if(SequenceGenerator.isNullId(id)) {
			this.id = generator.nextId();
		} else {
			this.id = id;
		}
		this.name = name;
		this.usage = usage;
		this.consistentStartTimes = consistentStartTimes;
		this.consistentLunchTimes = consistentLunchTimes;
		this.consistentBreakTimes = consistentBreakTimes;
		this.consecutiveDaysOff = consecutiveDaysOff;
		this.consecutiveDaysOn = consecutiveDaysOn;
		this.minHoursBetweenShifts = minHoursBetweenShifts;
		this.dailyRuleMembers = dailyRuleMembers;
	}
}
