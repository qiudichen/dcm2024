package com.e2.wfm.commons.pattern;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ScheduleListenerImpl implements ScheduleListener {
	private static final long WAIT_SECONDS = 1;
	
	private static final String[] NULL_RESULT = null;
	
	private BlockingQueue<String[]> queue;
	
	private boolean completed;
	
	public ScheduleListenerImpl(int size) {
		this.queue = new ArrayBlockingQueue<>(size);
		this.completed = false;
	}
	
	@Override
	public void addPattern(String[] oids) {
		try {
			this.queue.put(oids);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] getNextOids() {
		try {
			return this.queue.poll(WAIT_SECONDS, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return NULL_RESULT;
		}
	}

	@Override
	public boolean isEOF() {
		return this.isCompleted() && this.queue.isEmpty();
	}
	
	@Override
	public void setCompleted() {
		this.completed = true;
	}

	@Override
	public boolean isCompleted() {
		return this.completed;
	}
}
