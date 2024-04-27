package com.e2.wfm.commons.pattern.rule;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(builderClassName = "builder")
@EqualsAndHashCode
@AllArgsConstructor
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class ShiftUsageRuleImpl implements ShiftUsageRule {
	private final int shiftId;
	private final int maxUsage;
	private final int minUsage;
	
	@Override
	public boolean isGreaterMax(int val) {
		return val > this.maxUsage;
	}
	@Override
	public boolean isLessMin(int val) {
		return val < this.minUsage;
	}
	@Override
	public boolean isInvalidUsage(int val) {
		return this.isLessMin(val) || this.isGreaterMax(val);
	}
}
