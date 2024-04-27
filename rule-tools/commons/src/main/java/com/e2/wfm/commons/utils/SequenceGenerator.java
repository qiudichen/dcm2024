package com.e2.wfm.commons.utils;

import java.util.concurrent.atomic.AtomicLong;

public class SequenceGenerator extends ThreadLocal<AtomicLong> {
	public static final Long NULL_ID = null;
	
	@Override
    protected AtomicLong initialValue() {      	   
        return new AtomicLong(initValue);
     }
    
    private long initValue;
    
    public SequenceGenerator() {
    	this(0);
    }

    public SequenceGenerator(long initValue) {
    	this.initValue = initValue;
    }
    
    public static boolean isNullId(Long id) {
    	return id == null;
    }
    
    public long nextId() {
    	return super.get().getAndIncrement();
    }
}
