package com.demo.lib.generator.graph.dfs;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.demo.lib.generator.graph.common.Scorable;

public class ScoredStackTest {
	@Test
	public void test() {
		Map<Integer, Double> scoreMap = new HashMap<>();
		
		for(int i = 0; i < 7; i++) {
			scoreMap.put(i, 2d * (i+1));
		}
		
		Scorable scorable = new Scorable() {

			@Override
			public double getScore(int index) {
				return scoreMap.get(index);
			}
			
		};
		
		ScoredStack scoredStack = new ScoredStack(7, scorable);
		double score = 0d;
		for(int i = 0; i < 7; i++) {
			scoredStack.push(i);
			score = score + 2d * (i+1);
			Assertions.assertEquals(score, scoredStack.getCurrentScore());
			int[] v = scoredStack.cloneStack();
			Assertions.assertEquals(i+1, v.length);
		}

		try {
			scoredStack.push(8);
			Assertions.fail("");
		} catch(IndexOutOfBoundsException e) {
			
		}
		
		for(int i = 6; i >= 0; i--){
			scoredStack.pop();
			score = score - 2d * (i+1);
			Assertions.assertEquals(score, scoredStack.getCurrentScore());
			int[] v = scoredStack.cloneStack();
			Assertions.assertEquals(i, v.length);
		}
		
		try {
			scoredStack.pop();
			Assertions.fail("");
		} catch(EmptyStackException e) {
			
		}
		
		score = 0d;
		int j = 1;
		for(int i = 6; i >= 0; i--) {
			scoredStack.push(i);
			score = score + 2d * (i+1);
			Assertions.assertEquals(score, scoredStack.getCurrentScore());
			int[] v = scoredStack.cloneStack();
			Assertions.assertEquals(j, v.length);
			j++;
		}
	}
}
