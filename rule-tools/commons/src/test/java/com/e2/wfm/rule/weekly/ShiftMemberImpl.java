package com.e2.wfm.rule.weekly;

import com.e2.wfm.rule.common.IntegerRange;
import com.e2.wfm.rule.common.time.DayPattern;
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
public class ShiftMemberImpl implements ShiftMember {
	private String dailyRuleOid;
	private DayPattern allowableDays;
	private IntegerRange usage;
}
