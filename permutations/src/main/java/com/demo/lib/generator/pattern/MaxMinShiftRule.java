package com.demo.lib.generator.pattern;

public class MaxMinShiftRule implements MaxMinRule {
	private int shiftIndex;
	
	private int maxUsage;
	
	private int minUsage;

	public MaxMinShiftRule(int shiftIndex, int maxUsage, int minUsage) {
		super();
		this.shiftIndex = shiftIndex;
		
		if(maxUsage < minUsage) {
			this.maxUsage = minUsage;
			this.minUsage = maxUsage;			
		} else {
			this.maxUsage = maxUsage;
			this.minUsage = minUsage;
		}
	}

	public int getShiftIndex() {
		return shiftIndex;
	}

	@Override
	public int getMaxUsage() {
		return maxUsage;
	}

	@Override
	public int getMinUsage() {
		return minUsage;
	}
	
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
