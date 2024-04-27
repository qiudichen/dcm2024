package com.demo.lib.sort.heapsort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HeapNetHelp {
	private static Random random = new Random();
	
	public static int netLayer(int size) {
		int layer = 0;
		layer = log2(size) + 1;
		return layer;
	}
	
	public static int log2(int n)
    {
        int result = (int)(Math.log(n) / Math.log(2));
        return result;
    }
	
	public static int lastNodeWithChild(int size) {
		return size / 2-1;
	}
	
	public static int leftChild(int nodeIndex) {
		return nodeIndex * 2 + 1;
	}
	
	public static boolean isHeapNet(int[] arr) {
		int len = arr.length;
		int lastNodeId = lastNodeWithChild(len);
		
		for(int i = 0; i <= lastNodeId; i++) {
			int left = leftChild(i);
			if(arr[i] < arr[left]) {
				return false;
			}
			
			int right = left + 1;
			if(right < len && arr[i] < arr[right]) {
				return false;
			}
		}
		return true;
	}
	
	public static int[] shuffle(int arr[]) {
		int len = arr.length;
		List<Integer> list = new ArrayList<>(len);
		for(int v : arr) {
			list.add(v);
		}
		
		int values[] = new int[len];
		
		for(int i = 0; i < len; i++) {
			int index = randomInt(len - i);
			values[i] = list.get(index);
			list.remove(index);
		}
		
		for(int i = len - 1; i >= 0; i--) {
			list.add(values[i]);
		}
		
		for(int i = 0; i < len; i++) {
			int index = randomInt(len - i);
			values[i] = list.get(index);
			list.remove(index);
		}
				
		return values;
	}
	
	public static int randomInt(int len) {
		int low = 0;
		int high = len - 1;
		return randomInt(low, high);
	}
	
	public static int randomInt(int low, int high) {
		if(low == high) {
			return 0;
		}
		int result = random.nextInt(high-low) + low;
		return result;
	}
}
