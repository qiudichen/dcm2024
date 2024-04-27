package com.demo.lib.generator.graph.dfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.demo.lib.generator.graph.common.Graph;
import com.demo.lib.generator.graph.common.IndexedGraph;
import com.demo.lib.generator.graph.common.Node;

public class DepthFirstSelector {

	private Graph graph;
	private int depth;
	
	public DepthFirstSelector(Graph graph, int depth) {
		this.graph = graph;
		this.depth = depth;
	}
	
	/*
	 * find all possible selection start from start node to end node
	 */
	public List<int[]> findAllFromStartToEnd() {
		Set<Integer> startNodes = graph.getStartNodes();
		IntStack stack = new IntStack(depth);
		List<int[]> result = new ArrayList<>();
		for(Integer parent : startNodes) {
			stack.push(parent);
			visit(parent, stack, result);
			Integer removed = stack.pop();
			assert parent.intValue() == removed.intValue();
		}
		System.out.println("Total Visiting count: " + stack.getVistingCount());
		return result;
	}
	
	private void visit(Integer parent, IntStack stack, List<int[]> result) {
		if(graph.isEndNode(parent)) {
			int[] r = stack.cloneStack();
			result.add(r);
			//Integer removed = stack.pop();
			//assert parent.intValue() == removed.intValue();
			return;
		}
		
		List<Integer> edges = graph.getEdgesBy(parent);
		for(Integer child : edges) {
			stack.push(child);
			visit(child, stack, result);
			Integer removed = stack.pop();
			assert child.intValue() == removed.intValue();
		}
	}
	
	/**
	 * iterate all nodes and return all selection if the length is between
	 * minUsage and maxUsage 
	 * @param maxUsage
	 * @param minUsage
	 * @return
	 */
	public List<int[]> findAllSelection(int minUsage, int maxUsage) {
		if(minUsage > maxUsage) {
			int temp = maxUsage;
			maxUsage = minUsage;
			minUsage = temp;
		}
		
		int numOfNodes = graph.getNodes().length;
		IntStack stack = new IntStack(minUsage, maxUsage);
		List<int[]> result = new ArrayList<>();
		for(int parent = 0; parent < numOfNodes; parent++) {
			stack.push(parent);
			visitBySize(parent, stack, result);
			Integer removed = stack.pop();
			assert parent == removed.intValue();
		}
		System.out.println("Total Visiting count: " + stack.getVistingCount());
		return result;
	}
	
	private void visitBySize(Integer parent, IntStack stack, List<int[]> result) {
		if(stack.isValid()) {
			int[] r = stack.cloneStack();
			result.add(r);
		}		
		if(graph.isEndNode(parent) || stack.isEnd()) {
			return;
		}
		
		List<Integer> edges = graph.getEdgesBy(parent);
		for(Integer child : edges) {
			stack.push(child);
			visitBySize(child, stack, result);
			Integer removed = stack.pop();
			assert child.intValue() == removed.intValue();
		}
	}
	
	
	/**
	 * 
	 * find the first valid selection
	 * from start to end node
	 *
	 * @return index array of selection node
	 */
	public int[] findFirstSelection() {
		Set<Integer> startNodes = graph.getStartNodes();
		Integer parent = startNodes.iterator().next();
		IntStack stack = new IntStack(depth);
		stack.push(parent);
		return getFirstSelection(parent, stack);
	}
	
	private int[] getFirstSelection(Integer parent, IntStack stack) {
		if(!graph.isEndNode(parent)) {
			int[] r = stack.cloneStack();
			return r;
		}
		List<Integer> edges = graph.getEdgesBy(parent);
		Integer firstChild = edges.get(0);
		return getFirstSelection(firstChild, stack);
	}
	
	/**
	 * 
	 * @return index array of selection node
	 */
	public int[] findMaxScoreSelection() {
		Set<Integer> startNodes = graph.getStartNodes();
		ScoredStack stack = new ScoredStack(depth, this.graph);
		BestSelection bestSelection = new BestSelection();
		for(Integer parent : startNodes) {
			stack.push(parent);
			visit(parent, stack, bestSelection);
			Integer removed = stack.pop();
			assert parent.intValue() == removed.intValue();
		}
		return bestSelection.getIndexes();
	}
	
	private void visit(Integer parent, ScoredStack stack, BestSelection result) {
		if(!graph.isEndNode(parent)) {
			double score = stack.getCurrentScore();
			if(result.isGreater(score)) {
				int[] r = stack.cloneStack();
				result.update(r);				
			}
			Integer removed = stack.pop();
			assert parent.intValue() == removed.intValue();
			return;
		}
		
		List<Integer> edges = graph.getEdgesBy(parent);
		for(Integer child : edges) {
			stack.push(child);
			visit(child, stack, result);
			Integer removed = stack.pop();
			assert child.intValue() == removed.intValue();
		}
	}
	
	class BestSelection {
		int[] indexes;
		double score;
		
		public BestSelection() {
		}
		
		public BestSelection(int[] indexes, double score) {
			super();
			this.indexes = indexes;
			this.score = score;
		}
		
		boolean isGreater(double score) {
			return (indexes == null || this.score < score);
		}
		
		void update(int[] indexes) {
			this.indexes = indexes;
		}

		public int[] getIndexes() {
			return indexes;
		}

		public double getScore() {
			return score;
		}
	}
}
