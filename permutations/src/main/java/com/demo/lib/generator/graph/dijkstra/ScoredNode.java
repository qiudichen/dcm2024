package com.demo.lib.generator.graph.dijkstra;

public class ScoredNode implements Comparable<ScoredNode>{
	private int index;
	
	private double score;
	
	private ScoredNode from;

	public ScoredNode(int index, double score) {
		this(index, score, null);
	}
	
	public ScoredNode(int index, double score, ScoredNode from) {
		super();
		this.index = index;
		this.score = score + (from != null ? from.getScore() : 0d);
		this.from = from;
	}

	public int getIndex() {
		return index;
	}

	public double getScore() {
		return score;
	}

	public ScoredNode getFrom() {
		return from;
	}
	
	public boolean updateFrom(ScoredNode from) {
		if(this.from == from) {
			return false;
		}
		double newScore = this.score + from.getScore() - this.from.getScore();
		if(newScore < this.score) {
			this.depth = 0;
			this.score = newScore;
			this.from = from;
			return true;
		} else {
			return false;
		}
	}

	private int depth;
	
	protected int getDepth() {
		if(this.depth == 0) {
			depth++;
			if(this.from != null) {
				depth = depth + this.from.getDepth();
			}
		}
		return this.depth;
	}

	public int[] getIndexes() {
		int len = getDepth();
		int[] v = new int[len];
		cloneIndex(v, len-1);
		return v;
	}
	
	protected void cloneIndex(int[] v, int i) {
		v[i] = this.index;
		if(this.from != null) {
			i--;
			from.cloneIndex(v, i);
		}
	}
	
	@Override
	public int compareTo(ScoredNode o) {
		if(o == null) {
			return 1;
		}
		
		double d = o.getScore() - this.score;
		if(d == 0d) {
			return 0;
		}
		return d > 0d ? 1 : -1;
	}
}
