package com.demo.lib.generator.pattern;

public interface MaxMinRule {
	public int getMaxUsage();
	public int getMinUsage();
	public boolean isGreaterMax(int val);
	public boolean isLessMin(int val);
	public boolean isInvalidUsage(int val);
}
