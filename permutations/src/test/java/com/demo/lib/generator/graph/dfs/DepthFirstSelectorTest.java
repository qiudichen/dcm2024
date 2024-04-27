package com.demo.lib.generator.graph.dfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.demo.lib.generator.graph.common.IndexedGraph;
import com.demo.lib.generator.graph.common.IndexedGraphBuilder;
import com.demo.lib.generator.graph.common.Node;
import com.demo.lib.generator.graph.common.ShiftNodeImpl;

public class DepthFirstSelectorTest {

	@Test
	public void findAllStoETest() {
		IndexedGraph graph = buildGraph();
		
		Set<String> resultSet = resultStartToEnd();

		DepthFirstSelector selector = new DepthFirstSelector(graph, 5);
		List<int[]> allList = selector.findAllFromStartToEnd();
		
		Assertions.assertEquals(resultSet.size(), allList.size());
		
		for(int[] selection : allList) {
			StringBuilder buf = new StringBuilder();
			for(int index : selection) {
				buf.append(graph.getNodeBy(index).toString()).append("-");
			}
			System.out.println(buf.toString());
			Assertions.assertTrue(resultSet.contains(buf.toString()), buf.toString());
		}
	}
	
	@Test
	public void findAllTest() {
		IndexedGraph graph = buildGraph();
		
		Set<String> resultSet = resultStartToEnd();
		
		DepthFirstSelector selector = new DepthFirstSelector(graph, 5);
		List<int[]> allList = selector.findAllSelection(3, 5);
		
		//Assertions.assertEquals(resultSet.size(), allList.size());
		
		for(int[] selection : allList) {
			StringBuilder buf = new StringBuilder();
			for(int index : selection) {
				buf.append(graph.getNodeBy(index).toString()).append("-");
			}
			System.out.println(buf.toString());
			//Assertions.assertTrue(resultSet.contains(buf.toString()), buf.toString());
		}
	}
	
	private IndexedGraph buildGraph() {
		ShiftNodeImpl d = new ShiftNodeImpl("d", 101, 10d);
		ShiftNodeImpl c = new ShiftNodeImpl("c", 102, 3d);
		ShiftNodeImpl e = new ShiftNodeImpl("e", 103, 4d);
		ShiftNodeImpl f = new ShiftNodeImpl("f", 104, 6d);
		ShiftNodeImpl b = new ShiftNodeImpl("b", 105, 10d);
		ShiftNodeImpl g = new ShiftNodeImpl("g", 106, 8d);
		ShiftNodeImpl a1 = new ShiftNodeImpl("a1", 107, 14d);
		ShiftNodeImpl a2 = new ShiftNodeImpl("a2", 108, 12d);
		
		Map<Node, Node[]> edges = new HashMap<>();
		
		edges.put(d, new Node[] {c,e});
		edges.put(c, new Node[] {f, b});
		edges.put(e, new Node[] {f, g});
		edges.put(f, new Node[] {b, g, a1});
		edges.put(b, new Node[] {a1, a2});
		edges.put(g, new Node[] {a1, a2});
		
		IndexedGraphBuilder builder = new IndexedGraphBuilder(edges);
		IndexedGraph graph = builder.build();	
		return graph;
	}
	
	private Set<String> resultStartToEnd() {
		Set<String> resultSet = new HashSet<>();
		resultSet.add("d-c-b-a1-");
		resultSet.add("d-c-b-a2-");
		resultSet.add("d-c-f-b-a1-");
		resultSet.add("d-c-f-b-a2-");
		resultSet.add("d-c-f-a1-");
		resultSet.add("d-c-f-g-a1-");
		resultSet.add("d-c-f-g-a2-");
		resultSet.add("d-e-f-b-a1-");
		resultSet.add("d-e-f-b-a2-");
		resultSet.add("d-e-f-g-a1-");
		resultSet.add("d-e-f-g-a2-");
		resultSet.add("d-e-f-a1-");
		resultSet.add("d-e-g-a1-");
		resultSet.add("d-e-g-a2-");
		return resultSet;
	}
}
