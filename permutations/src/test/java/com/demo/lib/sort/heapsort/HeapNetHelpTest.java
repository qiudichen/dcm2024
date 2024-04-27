package com.demo.lib.sort.heapsort;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeapNetHelpTest {
	
	@Test
	public void shuffle() {
		int arr[];
		for(int i = 10; i < 21; i++) {
			arr = new int[i];
			for(int j = 0; j < i; j++) {
				arr[j] = j+1;
			}
			
			int[] values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
			values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
			values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
			values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
			values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
			values = HeapNetHelp.shuffle(arr);
			System.out.println(toString(values));
		}
	}
	
	private String toString(int[] values) {
		StringBuilder builder = new StringBuilder();
		for(int v : values) {
			builder.append(v).append(" ");
		}
		return builder.toString().trim();
	}
	
	@Test
	public void isHeapNet() {
		int[] arr = new int[] {9,8,7,6,5,4,3,2,1,0};
		Assertions.assertTrue(HeapNetHelp.isHeapNet(arr));
		
		arr = new int[] {4,6,8,5,9};
		Assertions.assertFalse(HeapNetHelp.isHeapNet(arr));

		arr = new int[] {9,6,8,5,4};
		Assertions.assertTrue(HeapNetHelp.isHeapNet(arr));
	}
	
	@Test
	public void leftChild() {
		for(int i = 0; i < 31; i++) {
			int idnex = HeapNetHelp.leftChild(i); 
			if(i == 0) {
				Assertions.assertEquals(1, idnex);
			}
			if(i == 1) {
				Assertions.assertEquals(3, idnex);
			}
			if(i == 2) {
				Assertions.assertEquals(5, idnex);
			}
			
			if(i == 10) {
				Assertions.assertEquals(21, idnex);
			}
			if(i == 30) {
				Assertions.assertEquals(61, idnex);
			}
		}
	}
	
	@Test
	public void lastNodeWithChild() {
		for(int i = 1; i < 31; i++){
			int idnex = HeapNetHelp.lastNodeWithChild(i); 
        	if(i == 1) {
        		Assertions.assertEquals(-1, idnex);
        	}
        	if(i >= 2 && i <= 3) {
        		Assertions.assertEquals(0, idnex);
        	}       
        	if(i >= 4 && i <= 5) {
        		Assertions.assertEquals(1, idnex);
        	}       
        	if(i >= 6 && i <= 7) {
        		Assertions.assertEquals(2, idnex);
        	}       
        	if(i >= 8 && i <= 9) {
        		Assertions.assertEquals(3, idnex);
        	}       
        	if(i >= 10 && i <= 11) {
        		Assertions.assertEquals(4, idnex);
        	}       
        	if(i >= 12 && i <= 13) {
        		Assertions.assertEquals(5, idnex);
        	}       
        	if(i >= 14 && i <= 15) {
        		Assertions.assertEquals(6, idnex);
        	}       
		}
	}
	
	@Test
	public void netLayerTest() {
		for(int i = 1; i < 31; i++){
        	int layer = HeapNetHelp.netLayer(i); 
        	System.out.println("len: " + i + "  layer:" + layer);
        	if(i == 1) {
        		Assertions.assertEquals(1, layer);
        	}
        	if(i >= 2 && i <= 3) {
        		Assertions.assertEquals(2, layer);
        	}
        	if(i >= 4 && i <= 7) {
        		Assertions.assertEquals(3, layer);
        	}        	
        	if(i >= 8 && i <= 15) {
        		Assertions.assertEquals(4, layer);
        	}        	
        	if(i >= 16 && i <= 31) {
        		Assertions.assertEquals(5, layer);
        	}        	
        }
	}
}
