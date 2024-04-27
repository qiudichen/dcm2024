package com.demo.lib.generator.graph.bfs;

/**
 * To Path represent a leading shift with a path to the end. 
 * key - the leading shift id and DailyRule ids in the path 
 * 		 are combine into a single long key
 * size - number of layers
 * 
 */
public class ToPath extends BasePath {
	public ToPath(int[] toNodes) {
		super(toNodes);
	}

	public int getLeadingShift() {
		int header[] = unpack(this.getKey(), 1);
		return header[0];
	}
}
