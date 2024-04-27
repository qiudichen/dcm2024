package com.demo.lib.generator.graph.common;

import java.util.Objects;

public class ShiftNodeImpl implements Node {

	private long id;
	private double score;
	private String name;
	
	public ShiftNodeImpl(String name, long id, double score) {
		super();
		this.name = name;
		this.id = id;
		this.score = score;
	}

	public ShiftNodeImpl(long id, double score) {
		this(null, id, score);
	}
	
	public long getId() {
		return id;
	}

	@Override
	public int compareTo(Node o) {
		if(o == null) {
			return 1;
		}
		
		double d = o.getScore() - this.score;
		if(d == 0d) {
			return 0;
		}
		return d > 0d ? 1 : -1;
	}

	@Override
	public long getNodeId() {
		return getId();
	}

	@Override
	public double getScore() {
		return this.score;
	}

	@Override
	public boolean isGreater(Node o) {
		return this.score - o.getScore() > 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public String toString() {
		return this.name == null ? "" + this.id : this.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiftNodeImpl other = (ShiftNodeImpl) obj;
		return id == other.id;
	}
}
