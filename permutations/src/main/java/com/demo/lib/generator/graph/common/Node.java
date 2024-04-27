package com.demo.lib.generator.graph.common;

public interface Node extends Comparable<Node>{
	long getNodeId();	
	double getScore();
	boolean isGreater(Node o);
}
