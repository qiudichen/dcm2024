package com.demo.lib.generator.permutation;

import java.util.Arrays;

/**
 * Full Permutation problem: how many choice with N elements and select N without duplicated selection.
 * For example: 1, 2, 3
 * then we have
 * 	1,2,3
 *  1,3,2
 * 	2,1,3
 * 	2,3,1
 *  3,1,2
 *  3,2,1
 * 
 * @author DavidChen
 *
 */
public class FullPermutationSwaper {

	public FullPermutationSwaper() {
	}
	
	private void swap(int[] elements, int i, int j) {
		int tmp = elements[j];
		elements[j] = elements[i];
		elements[i] = tmp;
	}
	
	public void generator(int[] elements) {
		generator(elements, 0);
	}
	
	private void generator(int[] elements, int cur) {
		if(elements.length == cur) {
			System.out.println(Arrays.toString(elements));
			return;
		}
		
		for(int i = cur; i < elements.length; i++) {
			swap(elements, i, cur);
			generator(elements, cur + 1);
			swap(elements, i, cur);
		}
	}
	
	public static void main(String[] argv) {
		int[] elements = {1, 2, 3, 4};
		
		FullPermutationSwaper swaper = new FullPermutationSwaper();
		swaper.generator(elements);
	}
}
