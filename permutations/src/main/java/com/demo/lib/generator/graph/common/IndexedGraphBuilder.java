package com.demo.lib.generator.graph.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IndexedGraphBuilder {
    private final Map<Node, Node[]> edges;
    
	public IndexedGraphBuilder(Map<Node, Node[]> edges) {
		super();
		this.edges = edges;
	}
	
	public IndexedGraph build() {
		final IndexedGraph graph = new IndexedGraph(getNodeSize());
		this.edges.entrySet().forEach(entry -> {
			Node fromNode = entry.getKey();
			for(Node toNode : entry.getValue()) {
				graph.addEdge(fromNode, toNode);
			}
		});
		
		graph.init();
		return graph;
	}
	
	private int nodeSize = 0;
	private int edgeSize = 0;
	
	protected int getNodeSize() {
		if(nodeSize == 0) {
			init();
		}
		return nodeSize;
	}
	
	protected int getEdgeSize() {
		if(edgeSize == 0) {
			init();
		}
		return edgeSize;		
	}
	
	
	private void init() {
		final Set<Node> allSet = new HashSet<>();
		this.edges.entrySet().forEach(entry -> {
			allSet.add(entry.getKey());
			edgeSize = edgeSize + entry.getValue().length;
			for(Node v : entry.getValue()) {
				allSet.add(v);
			}
		});
		this.nodeSize = allSet.size();
	}
}
