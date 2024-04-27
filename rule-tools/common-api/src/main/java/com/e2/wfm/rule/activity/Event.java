package com.e2.wfm.rule.activity;

import com.e2.wfm.rule.Entity;

/**
 * Activity Code API
 */
public interface Event extends Entity {
	boolean isOpen();
	
	EventType getType();
}
