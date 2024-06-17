package org.Logback;

import java.util.concurrent.atomic.AtomicLong;

public class TestObj {
	private static final AtomicLong atomicLong = new AtomicLong();
	private long id;
	
	public TestObj() {
		this.id = atomicLong.incrementAndGet();
		System.out.println("=======================" + id);
	}
	
	public long getId() {
		return this.id;
	}
}
