package com.demo.lib.generator.permutation;

import java.util.ArrayList;
import java.util.List;

/**
 * Repeatable Permutation problem: from n elements, select m elements and it is repeatable.
 * For example has three elements: 1, 2, 3
 *    select 2, then we have
 *    1, 1
 *    1, 2
 *    1, 3
 *    2, 1
 *    2, 2
 *    2, 3
 *    3, 1
 *    3, 2
 *    3, 3
 * 
 * @author DavidChen
 *
 */
public class RepeatablePermutationGenerator {

	private int values[];
	
	private int[][] results;
	
	private int current;
	
	public RepeatablePermutationGenerator() {
		
	}
	
	/**
	 * 
	 * @param elements can be selected;
	 * @param selected
	 */
	public int[][] generate(int[] elements, int selected) {
		if(elements.length < selected) {
			selected = elements.length;
		}
		values = new int[selected];
		int count = getTotalOptions(elements.length, selected);
		results = new int[count][selected];
		current = 0;
		recursiveSelect(elements,selected,0);
		return results;
    }
	
	private int getTotalOptions(int n, int m) {
		int count = 1;
		for(int i = 0; i < m; i++) {
			count = count * n;
		}
		return count;
	}
	
	private void recursiveSelect(int[] elements, int selected, int cur) {
        if(cur == selected) {
        	System.arraycopy(values, 0, results[current], 0, values.length);
        	current++;
            return;
        }
         
        for(int i=0;i<elements.length;i++) {
        	values[cur] = elements[i];
        	recursiveSelect(elements, selected, cur+1);
            values[cur] = 0;
        }
    }
	
	public static void main(String[] args) {
        int shu[] = {1,2,3,4};
        RepeatablePermutationGenerator iteration = new RepeatablePermutationGenerator();
        int[][] results = iteration.generate(shu, 4);
        System.out.println(results);
    }
}
