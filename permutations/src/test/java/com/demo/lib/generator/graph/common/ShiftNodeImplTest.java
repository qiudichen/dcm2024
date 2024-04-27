package com.demo.lib.generator.graph.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShiftNodeImplTest {
	@Test
	public void sortingTest() {
		List<Node> list = new ArrayList<>();
		Random random = new Random();

		for(int i = 0; i < 100; i++) {
			double result = random.nextDouble(-100d, 100d);
			Node node = new ShiftNodeImpl(i+1, result);
			list.add(node);
		}
		
		Collections.sort(list);
		double previousScore = Double.MAX_VALUE;
		
		for(Node node : list) {
			Assertions.assertTrue(node.getScore() < previousScore);
			previousScore = node.getScore();
		}
	}
}
