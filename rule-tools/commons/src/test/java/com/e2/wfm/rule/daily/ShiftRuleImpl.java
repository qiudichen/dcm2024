package com.e2.wfm.rule.daily;

import com.e2.wfm.commons.utils.SequenceGenerator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import lombok.*;

@Getter
@Builder(builderClassName = "builder")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class ShiftRuleImpl implements ShiftRule {
	
	private static SequenceGenerator generator = new SequenceGenerator();
	
	private String oid;
	private Long id;	
    private String name;
	private String shiftType;
    private LocalTime earliestStartTime;
    private LocalTime latestStartTime;
    private Duration startTimeIncrement;
    private Duration shiftLength;
    private List<ShiftEvent> activities;
    
    private SplitShiftRule splitShift;

	public ShiftRuleImpl(String oid, Long id, String name, String shiftType, LocalTime earliestStartTime,
			LocalTime latestStartTime, Duration startTimeIncrement, Duration shiftLength, List<ShiftEvent> activities,
			SplitShiftRule splitShift) {
		super();
		this.oid = oid;
		
		if(SequenceGenerator.isNullId(id)) {
			this.id = generator.nextId();
		} else {
			this.id = id;
		}
		
		this.name = name;
		this.shiftType = shiftType;
		this.earliestStartTime = earliestStartTime;
		this.latestStartTime = latestStartTime;
		this.startTimeIncrement = startTimeIncrement;
		this.shiftLength = shiftLength;
		this.activities = activities;
		this.splitShift = splitShift;
	}
    
}
