package com.demo.lib.generator.graph.dijkstra;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.demo.lib.generator.graph.common.IndexedGraph;
import com.demo.lib.generator.graph.common.IndexedGraphBuilder;
import com.demo.lib.generator.graph.common.Node;
import com.demo.lib.generator.graph.common.ShiftNodeImpl;

public class ScoredNodeTest {
	@Test
	public void test() {
		ScoredNode d = new ScoredNode(0, 0d);
		Assertions.assertEquals(0, d.getIndex());
		Assertions.assertEquals(0d, d.getScore());
		Assertions.assertEquals(1, d.getDepth());
		int[] v = d.getIndexes();
		Assertions.assertEquals(1, v.length);
		Assertions.assertEquals(0, v[0]);
		
		ScoredNode c = new ScoredNode(1, 3d, d);
		
		Assertions.assertEquals(1, c.getIndex());
		Assertions.assertEquals(3d, c.getScore());
		Assertions.assertEquals(2, c.getDepth());
		v = c.getIndexes();
		Assertions.assertEquals(2, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
				
		
		ScoredNode e = new ScoredNode(2, 4d, d);
		Assertions.assertEquals(2, e.getIndex());
		Assertions.assertEquals(4d, e.getScore());
		Assertions.assertEquals(2, e.getDepth());
		v = e.getIndexes();
		Assertions.assertEquals(2, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(2, v[1]);
		
		ScoredNode fc = new ScoredNode(3, 6d, c);
		
		Assertions.assertEquals(3, fc.getIndex());
		Assertions.assertEquals(9d, fc.getScore());
		Assertions.assertEquals(3, fc.getDepth());
		v = fc.getIndexes();
		Assertions.assertEquals(3, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);
		
		ScoredNode fe = new ScoredNode(3, 6d, e);
		Assertions.assertEquals(3, fe.getIndex());
		Assertions.assertEquals(10d, fe.getScore());
		Assertions.assertEquals(3, fe.getDepth());
		v = fe.getIndexes();
		Assertions.assertEquals(3, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(2, v[1]);
		Assertions.assertEquals(3, v[2]);
		
		ScoredNode bfc = new ScoredNode(4, 10d, fc);
		Assertions.assertEquals(4, bfc.getIndex());
		Assertions.assertEquals(19d, bfc.getScore());
		Assertions.assertEquals(4, bfc.getDepth());
		v = bfc.getIndexes();
		Assertions.assertEquals(4, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);		
		
		ScoredNode bfe = new ScoredNode(4, 10d, fe);
		Assertions.assertEquals(4, bfe.getIndex());
		Assertions.assertEquals(20d, bfe.getScore());
		Assertions.assertEquals(4, bfe.getDepth());
		v = bfe.getIndexes();
		Assertions.assertEquals(4, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(2, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);
		
		ScoredNode a1bfc = new ScoredNode(5, 14d, bfc);
		
		Assertions.assertEquals(5, a1bfc.getIndex());
		Assertions.assertEquals(33d, a1bfc.getScore());
		Assertions.assertEquals(5, a1bfc.getDepth());
		v = a1bfc.getIndexes();
		Assertions.assertEquals(5, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);
		Assertions.assertEquals(5, v[4]);
		
		ScoredNode a1bfe = new ScoredNode(5, 14d, bfe);
		Assertions.assertEquals(5, a1bfe.getIndex());
		Assertions.assertEquals(34d, a1bfe.getScore());
		Assertions.assertEquals(5, a1bfe.getDepth());
		v = a1bfe.getIndexes();
		Assertions.assertEquals(5, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(2, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);
		Assertions.assertEquals(5, v[4]);
		
		
		ScoredNode a2bfc = new ScoredNode(6, 12d, bfc);
		
		Assertions.assertEquals(6, a2bfc.getIndex());
		Assertions.assertEquals(31d, a2bfc.getScore());
		Assertions.assertEquals(5, a2bfc.getDepth());
		v = a2bfc.getIndexes();
		Assertions.assertEquals(5, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);
		Assertions.assertEquals(6, v[4]);		
		
		ScoredNode a2bfe = new ScoredNode(6, 12d, bfe);
		
		Assertions.assertEquals(6, a2bfe.getIndex());
		Assertions.assertEquals(32d, a2bfe.getScore());
		Assertions.assertEquals(5, a2bfe.getDepth());
		v = a2bfe.getIndexes();
		Assertions.assertEquals(5, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(2, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(4, v[3]);
		Assertions.assertEquals(6, v[4]);	
		
		//ScoredNode fc = new ScoredNode(3, 6d, c);
		ScoredNode a1fc = new ScoredNode(5, 14d, fc);
		
		Assertions.assertEquals(5, a1fc.getIndex());
		Assertions.assertEquals(23d, a1fc.getScore());
		Assertions.assertEquals(4, a1fc.getDepth());
		v = a1fc.getIndexes();
		Assertions.assertEquals(4, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(5, v[3]);

		Assertions.assertFalse(a1fc.updateFrom(bfc));
		Assertions.assertEquals(5, a1fc.getIndex());
		Assertions.assertEquals(23d, a1fc.getScore());
		Assertions.assertEquals(4, a1fc.getDepth());
		v = a1fc.getIndexes();
		Assertions.assertEquals(4, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(5, v[3]);

		//ScoredNode a1bfc = new ScoredNode(5, 14d, bfc);
		Assertions.assertTrue(a1bfc.updateFrom(fc));
		Assertions.assertEquals(5, a1fc.getIndex());
		Assertions.assertEquals(23d, a1fc.getScore());
		Assertions.assertEquals(4, a1fc.getDepth());
		v = a1fc.getIndexes();
		Assertions.assertEquals(4, v.length);
		Assertions.assertEquals(0, v[0]);
		Assertions.assertEquals(1, v[1]);
		Assertions.assertEquals(3, v[2]);		
		Assertions.assertEquals(5, v[3]);
		
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
}
