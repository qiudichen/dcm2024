package com.demo.lib.generator.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullPermutationGenerator {

    public static void main(String[] argv) {
    	FullPermutationGenerator f = new FullPermutationGenerator();
    	int[] list = new int[35];
    	for(int i = 0; i < list.length; i++) {
    		list[i] = i+1;
    	}
    	List r = f.getAllPermutations(list);
    	System.out.println();
    }
    
    public List<int[]> getAllPermutations(int[] list) {
        return getPermutations(list, null, 0, list.length);
    }
  
    private  List<int[]> getPermutations(int[] list, int[] current, int i, int size) {
        if (i == size) {
            return List.of();
        }
        List<int[]> all = new ArrayList<>();
        for (int j = i; j < size; j++) {
            int[] next;
            if(current == null) {
            	next = new int[] {list[j]};
            } else {
            	next = new int[current.length + 1];
            	System.arraycopy(next, 0, current, 0, current.length);
            	next = Arrays.copyOf(current, current.length + 1);
            	next[current.length] = list[j];
            }
            all.add(next);
            List<int[]> more = getPermutations(list, next, j + 1, size);
            if (!more.isEmpty()) {
                all.addAll(more);
            }
        }
        return all;
    }    
}
