package com.e2.wfm.commons.pattern;

import static com.e2.wfm.rule.RuleConstants.EMPTY_VAL;
import static com.e2.wfm.rule.RuleConstants.OFF_VAL;

/**
 * Working class to hold current selection
 */
public class SelectedValue {
	//select element of in each layer
	private int[] values;
	
	//usage of elements grouped by id
	private int[] counts;
	
	public SelectedValue() {
		this(7);
	}
	
	public SelectedValue(int size) {
		values = new int[size];
		for(int i = 0; i < size; i++) {
			values[i] = EMPTY_VAL;
		}
		counts = new int[size];
	}
	
	public void setValue(int value, int layer) {
		checkIndex(layer);
		values[layer] = value;
		counts[value]++;
	}
	
	public void resetValue(int layer) {
		checkIndex(layer);
		counts[values[layer]]--;
		values[layer] = EMPTY_VAL;
	}
	
	public int getUsage(int value) {
		return counts[value];
	}
	
	public String getConsectivePattern() {
		StringBuilder builder = new StringBuilder();
		for(int value : values) {
			builder.append((value == OFF_VAL) ? 0 : 1);
		}
		return builder.toString();
	}
	
	public int getSize() {
		return values.length;
	}
	
	public int[] cloneValues() {
		int[] copiedValues = new int[values.length];
    	System.arraycopy(values, 0, copiedValues, 0, values.length);
    	return copiedValues;
	}
	
	void checkIndex(int layer) {
		int endIndex = getSize() - 1;
		if(layer < 0 || layer > endIndex) {
			throw new IndexOutOfBoundsException("The layer must be between 0 to " + endIndex + " but it is " + layer);
		}
	}	
}
