package com.demo.lib.generator.graph.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexedGraph implements Graph {
	private int numOfNode;
	 
    private LinkedList<Integer> edges[];

    private Map<Long, Integer> idToIdxMap;
    
    private Node nodes[];
    
    private int nextIdx;
    
    private Set<Integer> startNodes;

    private Set<Integer> endNodes;
    
    protected IndexedGraph(int numOfNode) {
    	this.numOfNode = numOfNode;
    	edges = new LinkedList[this.numOfNode];
    	this.idToIdxMap = new HashMap<>(this.numOfNode);
    	this.nodes = new Node[this.numOfNode];
    	this.nextIdx = 0;
    	
    	this.startNodes = new HashSet<>();
    	this.endNodes = new HashSet<>();
    }

	protected void init() {
    	Set<Integer> nodeHasInEdge = new HashSet<>();
    	for(int i = 0; i < numOfNode; i++) {
    		if(edges[i] != null) {
    			edges[i].forEach(index -> {
    				nodeHasInEdge.add(index);
    			});
    		} else {
    			//node doesn't have out edge
    			this.endNodes.add(i);
    		}
    	}
    	
    	for(int i = 0; i < numOfNode; i++) {
    		if(!nodeHasInEdge.contains(i)) {
    			startNodes.add(i);
    		}
    	}
    }
    
    protected void addEdge(Node fromNode, Node toNode) {
    	int fromIndex = getNodeIndex(fromNode);
    	int toIndex = getNodeIndex(toNode);
    	//add out edges fromIndex - toIndex
    	if(edges[fromIndex] == null) {
    		edges[fromIndex] = new LinkedList<>();
    	}
    	
    	edges[fromIndex].add(toIndex);    	
    }
    
    private int getNodeIndex(Node node) {
    	if(idToIdxMap.containsKey(node.getNodeId())) {
    		return idToIdxMap.get(node.getNodeId());
    	} else {
    		int index = nextIdx;
    		nextIdx++;
    		nodes[index] = node;
    		idToIdxMap.put(node.getNodeId(), index);
    		return index;
    	}
    }

	protected Set<Integer> getEndNodes() {
		return endNodes;
	}   
	
	protected int getIndexBy(long nodeId) {
    	return this.idToIdxMap.get(nodeId);
    }
    
	public Node[] getNodes() {
    	return this.nodes;
    }    

    protected List<Integer>[] getEdges() {
		return edges;
	}
    
    @Override
    public List<Integer> getEdgesBy(int index) {
    	if(index < edges.length) {
    		return edges[index];
    	} else {
    		return Collections.emptyList();
    	}
    }
    
    @Override
    public boolean hasChild(int index) {
    	return edges[index] != null && !edges[index].isEmpty();
    }
    
    @Override
    public boolean isEndNode(Integer index) {
    	return this.endNodes.contains(index);
    }
    
    @Override
    public Node getNodeBy(int index) {
    	return nodes[index];
    }

    @Override
	public Set<Integer> getStartNodes() {
		return startNodes;
	}

	@Override
	public double getScore(int index) {
		return getNodeBy(index).getScore();
	} 
}
