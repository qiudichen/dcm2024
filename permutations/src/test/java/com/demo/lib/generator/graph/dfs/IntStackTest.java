package com.demo.lib.generator.graph.dfs;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntStackTest {
	@Test
	public void test() {
		IntStack stack = new IntStack(7);
		
		Assertions.assertTrue(stack.empty());
		for(int i = 0; i < 7; i++) {
			stack.push(i);
			Assertions.assertEquals(i, stack.peek());
			Assertions.assertFalse(stack.empty());
			Assertions.assertEquals(i+1, stack.size());
		}
		
		try {
			stack.push(8);
			Assertions.fail("No IndexOutOfBoundsException");
		} catch(IndexOutOfBoundsException e) {
			
		}
		
		int[] values = stack.cloneStack();
		for(int i = 0; i < values.length; i++) {
			Assertions.assertEquals(i, values[i]);
		}
		
		for(int i = 6; i >= 0; i--) {
			int v = stack.pop();
			Assertions.assertEquals(i, v);
			Assertions.assertEquals(i, stack.size());
		}
		
		try {
			stack.pop();
			Assertions.fail("No EmptyStackException");
		} catch(EmptyStackException e) {
			
		}
	}
}
