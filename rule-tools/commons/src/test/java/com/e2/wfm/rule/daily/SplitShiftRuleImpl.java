package com.e2.wfm.rule.daily;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(builderClassName = "builder")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class SplitShiftRuleImpl implements SplitShiftRule {
	private Duration shiftLength;
	private Duration offSegmentMaxUsage;
	private Duration offSegmentMinUsage;
	private Duration offSegmentIncrement;
	private List<ShiftEvent> activities;
}
