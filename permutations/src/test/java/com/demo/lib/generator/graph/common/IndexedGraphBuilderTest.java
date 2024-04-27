package com.demo.lib.generator.graph.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IndexedGraphBuilderTest {

	@Test
	public void builderTest() {
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
	
		List<Integer>[] edgelinkes = graph.getEdges();
		Set<Integer> snodes = graph.getStartNodes();

		for(int sIdx : snodes) {
			Node startNode = graph.getNodeBy(sIdx);
			Assertions.assertEquals("d", startNode.toString());
			assertion(startNode, graph);
		}
		
		for(int eIdx : graph.getEndNodes()) {
			Node endNode = graph.getNodeBy(eIdx);
			Assertions.assertTrue(endNode.toString().equals("a1") || endNode.toString().equals("a2"));
			assertion(endNode, graph);			
		}
		
		for(Node node : graph.getNodes()) {
			assertion(node, graph);
		}
	}
	
	private void assertion(Node parentNode, IndexedGraph graph) {
		List<Integer>[] endNodes = graph.getEdges();
		List<Integer>[] edgelinkes = graph.getEdges();
		long parentId = parentNode.getNodeId();
		int parentIndex = graph.getIndexBy(parentId);
		switch((short)parentId) {
		case 101: //d node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("d", parentNode.toString());
			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 102 || childNode.getNodeId() == 103);
				 Assertions.assertTrue(childNode.toString().equals("c") || childNode.toString().equals("e"));
			}			
			break;
		case 102: //c node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("c", parentNode.toString());
			
			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 104 || childNode.getNodeId() == 105);
				 Assertions.assertTrue(childNode.toString().equals("b") || childNode.toString().equals("f"));
			}				
			break;
		case 103: //e node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("e", parentNode.toString());

			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 104 || childNode.getNodeId() == 106);
				 Assertions.assertTrue(childNode.toString().equals("f") || childNode.toString().equals("g"));
			}				
			break;
		case 104://f node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("f", parentNode.toString());

			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 105 
						 || childNode.getNodeId() == 106
						 || childNode.getNodeId() == 107);
				 Assertions.assertTrue(childNode.toString().equals("b") 
						 || childNode.toString().equals("a1")
						 || childNode.toString().equals("g"));
			}			
			break;
		case 105: //b node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("b", parentNode.toString());

			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 107 || childNode.getNodeId() == 108);
				 Assertions.assertTrue(childNode.toString().equals("a1") || childNode.toString().equals("a2"));
			}			
			break;
		case 106: //g node
			Assertions.assertEquals(parentId, parentNode.getNodeId());
			Assertions.assertEquals("g", parentNode.toString());

			for(int childIdx : edgelinkes[parentIndex]) {
				 Node childNode = graph.getNodeBy(childIdx);
				 Assertions.assertTrue(childNode.getNodeId() == 107 || childNode.getNodeId() == 108);
				 Assertions.assertTrue(childNode.toString().equals("a1") || childNode.toString().equals("a2"));
			}			
			break;
		case 107: //a1 node
		case 108: //a2 node
			Assertions.assertNull(edgelinkes[parentIndex]);
			break;
		}
		
		
	}
}
