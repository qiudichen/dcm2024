package com.demo.lib.generator.pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternGenerator {
	final private static int EMPTY_VAL = -1;
	final public static int OFF_VAL = 0;

	public PatternGenerator() {
		
	}
	
	public List<int[]> generate(int[][] elements, MaxMinRule[] maxMinRules) {
		DataBus dataBus = new DataBus();
		dataBus.elements = elements;
		dataBus.maxMinRules = maxMinRules;
		dataBus.selectedValue = new SelectedValue(dataBus.getSelected());
		dataBus.results = new ArrayList<>();
		recursiveSelect(dataBus, 0);
		return dataBus.results;
    }

	private void recursiveSelect(DataBus dataBus, int cur) {
        if(cur == dataBus.getSelected()) {
        	if(dataBus.isValid()) {
	        	int[] copiedValues = dataBus.selectedValue.cloneValues();
	        	dataBus.results.add(copiedValues);
        	}
            return;
        }
        
        for(int i=0; i < dataBus.elements[cur].length;i++) {
        	int value = dataBus.elements[cur][i];
        	int usage = dataBus.selectedValue.getUsage(value);
        	//check if reach MaxUage
        	MaxMinRule rule = dataBus.getMaxMinRule(value);
        	if(rule != null && rule.isGreaterMax(usage + 1)) {
        		continue;
        	}
        	dataBus.selectedValue.setValue(dataBus.elements[cur][i], cur);
        	recursiveSelect(dataBus, cur+1);
        	dataBus.selectedValue.resetValue(cur);
        }
	}

	class SelectedValue {
		//select element of each day
		private int values[];
		//usage of each daily rule
		private int counts[];
		
		public SelectedValue(int size) {
			values = new int[size];
			for(int i = 0; i < size; i++) {
				values[i] = EMPTY_VAL;
			}
			counts = new int[size];
		}
		
		public void setValue(int value, int position) {
			values[position] = value;
			counts[value]++;
		}
		
		public int getUsage(int value) {
			return counts[value];
		}
		
		public void resetValue(int position) {
			counts[values[position]]--;
			values[position] = EMPTY_VAL;
		}
		
		public int[] cloneValues() {
			int[] copiedValues = new int[values.length];
        	System.arraycopy(values, 0, copiedValues, 0, values.length);
        	return copiedValues;
		}
	}
	

	class DataBus {
		private SelectedValue selectedValue;
		private List<int[]> results;
		private int[][] elements;
		private MaxMinRule[] maxMinRules;
		
		public MaxMinRule getMaxMinRule(int value) {
			if(this.maxMinRules == null) {
				return null;
			}
			
			return this.maxMinRules[value];
		}
		
		private int getRuleSize() {
			if(this.maxMinRules == null) {
				return 0;
			}
			
			return this.maxMinRules.length;
		}	
		
		private boolean isValid() {
			if(maxMinRules == null) {
				return true;
			}
			
			for(int i = 0; i < getRuleSize(); i++) {
				MaxMinRule rule = getMaxMinRule(i);
				if(rule == null) {
					continue;
				}
				
				int usage = this.selectedValue.getUsage(i);
				if(rule.isInvalidUsage(usage)) {
					return false;
				}
			}
			return true;
		}	
		
		public int getSelected() {
			return this.elements.length;
		}
	}

	public static long getMaxOptions(int[][] elements) {
		long total = 1;
		for(int[] emelent : elements) {
			total = total * emelent.length;
		}
		return total;
	}
	
	public static void display(int values[]) {
    	for(int v : values) {
    		if(v == 0) {
    			return;
    		}
    	}
    	System.out.println(values);
	}
	
	public static void main(String[] args) {
        int elements[][] = {
        		{OFF_VAL, 1}, 
        		{OFF_VAL, 2}, 
        		{OFF_VAL, 1, 2}, 
        		{OFF_VAL, 1}, 
        		{OFF_VAL, 1, 2}, 
        		{OFF_VAL, 2}, 
        		{OFF_VAL, 1}
        	};
        
        MaxMinRule[] maxMinRules = new MaxMinRule[3];
        maxMinRules[0] = new MaxMinShiftRule(0, 3, 0);
        maxMinRules[1] = new MaxMinShiftRule(1, 2, 5);
        maxMinRules[2] = new MaxMinShiftRule(2, 2, 5);
        PatternGenerator generator = new PatternGenerator();
        
        long start = System.currentTimeMillis();
        List<int[]> results = generator.generate(elements, maxMinRules);
        long end = System.currentTimeMillis();
        System.out.println((end - start));
        for(int[] values : results) {
        	display(values);
        }
        System.out.println(results);
    }
}
