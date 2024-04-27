package com.demo.lib.generator.graph.dijkstra;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.demo.lib.generator.graph.common.Graph;
import com.demo.lib.generator.graph.common.Node;

public class DijkstraSelector {
	private Graph graph;
	
	public DijkstraSelector(Graph graph) {
		this.graph = graph;
	}
	
	public int[] findMinScoreSelection() {
		Set<Integer> starts = this.graph.getStartNodes();
		Selection bestSelection = null;
		for(Integer start : starts) {
			Selection selection = findMinScoreSelection(start);
			if(bestSelection == null) {
				bestSelection = selection;
			} else if(bestSelection.score < selection.score) {
				bestSelection = selection;
			}
		}
		return bestSelection.v;
	}

	private Selection findMinScoreSelection(Integer start) {
		Node parent = this.graph.getNodeBy(start);
		List<ScoredNode> visited = new LinkedList<>();
		ScoredNode from = new ScoredNode(start, parent.getScore());
		visited.add(from);
		
		ScoredMap unvisited = new ScoredMap();
		
		ScoredNode next = null;
		List<Integer> edges = this.graph.getEdgesBy(start);
		for(Integer idx : edges) {
			Node childNode = this.graph.getNodeBy(idx);
			ScoredNode child = new ScoredNode(idx, childNode.getScore(), from);
			if(next == null) {
				next = child;
			} else if(next.getScore() > child.getScore()) {
				unvisited.put(child);
			} else {
				unvisited.put(next);
				next = child;
			}
		}
		
		ScoredNode selectedNode = findMinScoreSelection(next, visited, unvisited);
		return new Selection(selectedNode.getIndexes(), selectedNode.getScore());
	}
	
	private ScoredNode findMinScoreSelection(ScoredNode next, List<ScoredNode> visited, 
			ScoredMap unvisited) {
		if(this.graph.isEndNode(next.getIndex()))  {
			return next;
		}
		visited.add(next);
		
		List<Integer> edges = this.graph.getEdgesBy(next.getIndex());
		
		for(Integer idx : edges) {
			ScoredNode child = unvisited.getScoredNode(idx);
			if(child == null) {
				Node childNode = this.graph.getNodeBy(idx);
				child = new ScoredNode(idx, childNode.getScore(), next);
				unvisited.put(child);
			} else {
				unvisited.update(next, child);
			}
		}
		ScoredNode ret = unvisited.removeMinSoredNode();
		
		return findMinScoreSelection(ret, visited, unvisited);
	}
	
	class Selection {
		int[] v;
		double score;
		public Selection(int[] v, double score) {
			super();
			this.v = v;
			this.score = score;
		}
	}
}
