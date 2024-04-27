package com.demo.lib.sort.heapsort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeapSortorTest {
	
	@Test
	public void sortTest() {
		int[] arr = new int[] {4, 6, 8, 5, 9};
		HeapSortor.sort(arr);
		for(int i = 1; i < arr.length; i++) {
			 Assertions.assertTrue(arr[i-1] < arr[i]);
		}
		
		sortTest(10);
		sortTest(20);
		sortTest(30);
	}
	
	public void sortTest(int len) {		
		int arr[] = new int[len];
		for(int j = 0; j < len; j++) {
			arr[j] = j+1;
		}
		
		int[] values = HeapNetHelp.shuffle(arr);
		System.out.println(toString(values));
		HeapSortor.sort(values);
		System.out.println(toString(values));
		for(int i = 1; i < values.length; i++) {
			 Assertions.assertTrue(values[i-1] < values[i]);
		}
	}
	
	@Test
	public void adjustHeapTest() {		
		adjustHeapTest(10);
		adjustHeapTest(20);
		adjustHeapTest(30);
		adjustHeapTest(100);
	}
	
	public void adjustHeapTest(int len) {		
		int arr[] = new int[len];
		for(int j = 0; j < len; j++) {
			arr[j] = j+1;
		}
		
		int[] values = HeapNetHelp.shuffle(arr);
		System.out.println(toString(values));
		
        for(int i = values.length/2-1; i>=0; i--){
            HeapSortor.adjustHeap(values, i, values.length);
        }
        System.out.println(toString(values));
        boolean flag = HeapNetHelp.isHeapNet(values);
        Assertions.assertTrue(flag);
	}
		
	private String toString(int[] values) {
		StringBuilder builder = new StringBuilder();
		for(int v : values) {
			builder.append(v).append(" ");
		}
		return builder.toString().trim();
	}		
}
