package com.demo.lib.generator.graph.bfs;

/**
 * From Path represent a serial selected shifts in each layer. 
 * key - all shift's ids are combine into a single long key
 * size - number of layers
 * score - total score of this From Path (sum of score of all shifts)
 */
public class FromPath extends BasePath {

	private double score;
	
	private FromPath() {
		
	}
	
	public FromPath(int[] fromNodes, double score) {
		super(fromNodes);
		this.score = score;
	}

	public FromPath appendNode(int node, double score) {
		FromPath fromPath = new FromPath();
		fromPath.key = this.key << 10 | node;
		fromPath.size = this.size + 1;
		fromPath.score = this.score + score;
		return fromPath; 
	}

	public long combine(int array[]) {
		long t = 0;
		for(int i = 0; i < array.length; i++) {
			int a = array[i] << i * 10;
			t = a | t;
		}
		return t;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FromPath other = (FromPath) obj;
		return Double.doubleToLongBits(score) == Double.doubleToLongBits(other.score);
	}

	
}
