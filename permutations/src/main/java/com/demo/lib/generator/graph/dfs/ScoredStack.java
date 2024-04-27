package com.demo.lib.generator.graph.dfs;

import com.demo.lib.generator.graph.common.Scorable;

public class ScoredStack extends IntStack {
	private Scorable scorable;
	
	private double currentScore;
	
	public ScoredStack(int depth, Scorable scorable) {
		super(depth);
		this.scorable = scorable;
		this.currentScore = 0;
	}

	public double getCurrentScore() {
		return currentScore;
	}	
	
	@Override
	public int push(int v) {
		int i = super.push(v);
		currentScore = this.currentScore + scorable.getScore(i);
		return i;
	}
	
	@Override
	public int pop() {
		int i = super.pop();
		currentScore = this.currentScore - scorable.getScore(i);
		return i;		
	}
}
