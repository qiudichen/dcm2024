package com.e2.wfm.rule.activity;

import com.e2.wfm.commons.utils.SequenceGenerator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.*;

@Getter
@Builder(builderClassName = "builder")
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class EventImpl implements Event {
	private static SequenceGenerator generator = new SequenceGenerator();
	
	private String oid;
	private Long id;
    private String name;
    private boolean open;
	private boolean workHours;
	private EventType type;
	
	public EventImpl(String oid, Long id, String name, boolean open, boolean workHours, EventType type) {
		super();
		this.oid = oid;
		if(SequenceGenerator.isNullId(id)) {
			this.id = generator.nextId();
		} else {
			this.id = id;
		}
		this.name = name;
		this.open = open;
		this.workHours = workHours;
		this.type = type;
	}
}
