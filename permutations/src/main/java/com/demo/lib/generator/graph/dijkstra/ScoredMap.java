package com.demo.lib.generator.graph.dijkstra;

import java.util.HashMap;
import java.util.Map;

public class ScoredMap {
	Map<Integer, ScoredNode> unvisited;
	
	ScoredNode minScoreNode;
	
	public ScoredMap() {
		unvisited =  new HashMap<>(); 
	}
	
	public void put(ScoredNode node) {
		unvisited.put(node.getIndex(), node);
		updateMinNode(node);
	}
	
	private void updateMinNode(ScoredNode node) {
		if(minScoreNode == null || minScoreNode.getScore() > node.getScore()) {
			minScoreNode = node;
		}
	}
	
	public ScoredNode getScoredNode(Integer index) {
		return unvisited.get(index);
	}
	
	public void update(ScoredNode from, ScoredNode child) {
		if(child.updateFrom(from)) {
			updateMinNode(child);
		}
	}
	
	public ScoredNode removeMinSoredNode() {
		ScoredNode ret = this.minScoreNode;
		unvisited.remove(this.minScoreNode.getIndex());
		this.minScoreNode = findMinScoreNode();
		return ret;
	}
	
	public boolean isEmpty() {
		return unvisited.isEmpty();
	}
	
	private ScoredNode findMinScoreNode() {
		if(isEmpty()) {
			throw new RuntimeException("It is empty.");
		}
		
		ScoredNode minNode = null;
		for(ScoredNode node : this.unvisited.values()) {
			if(minNode == null || minNode.getScore() > node.getScore()) {
				minNode = node;
			}
		}
		return minNode;
	}
}
