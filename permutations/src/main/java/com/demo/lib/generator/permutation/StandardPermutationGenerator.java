package com.demo.lib.generator.permutation;

/**
 * Standard Permutation problem: from n elements, select m elements.
 * For example has three elements: 1, 2, 3
 *    select 2, then we have
 *    1, 2
 *    1, 3
 *    2, 1
 *    2, 3
 *    3, 1
 *    3, 2
 * 
 * @author DavidChen
 *
 */

public class StandardPermutationGenerator {
	public int[][] generate(int[] elements, int selected) {
		if(elements.length < selected) {
			selected = elements.length;
		}
		
		int count = getTotalOptions(elements.length, selected);
		DataBus dataBus = new DataBus();
		dataBus.elements = elements;
		dataBus.values = new int[selected];
		dataBus.selectedFlag = new boolean[elements.length];
		dataBus.results = new int[count][selected];
		dataBus.current = 0;
		recursiveSelect(dataBus, selected, 0);
		return dataBus.results;
    }	
	
	private int getTotalOptions(int n, int m) {
		int numerator = 1;
		for(int i = n; i > n-m; i--) {
			numerator = numerator * i;
		}
		return numerator;
	}
	
	private void recursiveSelect(DataBus dataBus, int selected, int cur) {
        if(cur == selected) {
        	System.arraycopy(dataBus.values, 0, dataBus.results[dataBus.current], 0, dataBus.values.length);
        	dataBus.current++;
            return;
        }
         
        for(int i=0; i < dataBus.elements.length;i++) {	
        	if(!dataBus.selectedFlag[i]) {
        		dataBus.values[cur] = dataBus.elements[i];
        		dataBus.selectedFlag[i] = true;
	        	recursiveSelect(dataBus, selected, cur+1);
	        	dataBus.values[cur] = 0;
	        	dataBus.selectedFlag[i] = false;
        	}
        }
    }	
	
	private class DataBus {
		private int[] values;
		private boolean[] selectedFlag;
		private int[][] results;
		private int current;
		private int[] elements;
	}
	
	public static void main(String[] args) {
		int[] list = new int[35];
    	for(int i = 0; i < list.length; i++) {
    		list[i] = i+1;
    	}
    	long count = 0;
        StandardPermutationGenerator iteration = new StandardPermutationGenerator();
        for(int i = 5; i <= list.length; i++) {
        	int[][] r = iteration.generate(list, i);
        	count = count+ r.length;
        	System.out.println(r.length);
        }
        
        System.out.println(count);
    }	
}
