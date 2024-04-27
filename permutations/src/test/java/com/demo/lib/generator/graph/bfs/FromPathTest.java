package com.demo.lib.generator.graph.bfs;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class FromPathTest {
	@Test
	public void cloneTest() {
		
	}

	@Test
	public void hashTest() {
		String UUID_name 
        = "007a4f19-b3cf-4294-bd97-6a6e3e57bb38"; 

    // Displaying the UUID 
    System.out.println("The specified String is: "
                       + UUID_name); 

    // Creating the UUID 
    UUID UUID_1 
        = UUID 
              .fromString(UUID_name); 
    
		int size = 500;

		size = size << 10;
		long hash = combine(new int[] {1, 2, 3});
		int t[] = unpack(hash, 3);
		
		int a[] = new int[3];
		Set<Integer> set = new HashSet<>();
		for(int i = 1; i < size; i++) {
			a[0] = i;
			for(int j = 1; j < size; j++) {
				a[1] = j;
//				FromPath fromPath = new FromPath(a);
//				int hash = fromPath.hashCode();
				for(int k = 1; k < size; k++) {
					a[2] = k;
				}
//				FromPath o = map.put(hash, fromPath);
//				if(o != null) {
//					System.out.println(hash);
//				}
			}
		}
		
		System.out.println(set.size());
	}
//	
//	public int paringFunction(int array[]) {
//		if(array == null || array.length == 0) {
//			return 0;
//		}
//		
//		if(array.length == 1) {
//			return array[0];
//		}
//		
//		int hash = paringFunction(array[0], array[1]);
//		for(int i = 2; i < array.length; i++) {
//			hash = paringFunction(hash, array[i]);
//		}
//		return hash;
//	}
//	
	public long paringFunction(int i, int j, int k) {
		long t = paringFunction(j, k);
		return paringFunction(i, (int)t);
	}
	
	public long paringFunction(int i, int j) {
		int t = i+j;
		t = (t-1) * (t-2)/2 + i;
		return t;
	}
	
	public long combine(int array[]) {
		long t = 0;
		for(int i = 0; i < array.length; i++) {
			int a = array[i] << i * 10;
			t = a | t;
		}
		return t;
	}
	
	long combine(int a, int b, int c)
	{
	    return (a << 20) | (b << 10) | c;
	}
	
	int[] unpack(long combined, int size) {
		int[] array = new int[size];
		for(int i = 0; i < size; i++) {
			array[i] = (int)combined >> (i * 10) & 0x3ff;
		}
		return array;
	}
}
