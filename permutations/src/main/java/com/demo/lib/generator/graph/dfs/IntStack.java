package com.demo.lib.generator.graph.dfs;

import java.util.EmptyStackException;

public class IntStack {
	protected int[] stacks;
	protected int current;
	
	protected int maxUsage;
	protected int minUsage;

	private int vistingCount;
	
	public IntStack(int depth) {
		this(0, depth);
	}	
	
	public IntStack(int minUsage, int maxUsage) {
		stacks = new int[maxUsage];
		for(int i = 0; i < maxUsage; i++) {
			stacks[i] = -1;
		}
		current = -1;
		this.maxUsage = maxUsage;
		this.minUsage = minUsage;
		this.vistingCount = 0;
	}	
	
	public int push(int v) {
		if(current+1 >= maxUsage) {
			throw new IndexOutOfBoundsException("Max size of the stack is " + maxUsage);
		}
		stacks[++current] = v;
		//this.vistingCount++;
		return v;
	}
	
	public int pop() {
		if(current < 0) {
			throw new EmptyStackException();
		}
		
		int value = stacks[current];
		stacks[current--] = -1;
		this.vistingCount++;
		return value;
	}
	
	public int peek() {
		if(current < 0) {
			throw new EmptyStackException();
		}
		return stacks[current];
	}
	
    public boolean empty() {
        return (current < 0);
    }
    
    public int size() {
    	return current + 1;
    }
    
    public boolean isEnd() {
    	return current + 1 == this.maxUsage;
    }
    
    public boolean isValid() {
    	int len = size();
    	return  len <= this.maxUsage && len >= this.minUsage;
    }
    
    public int[] cloneStack() {
    	int len = current+1;
    	int[] copy = new int[len];
    	System.arraycopy(stacks, 0, copy, 0, len);
    	return copy;
    }

	public int getVistingCount() {
		return vistingCount;
	}
}
