package com.demo.lib.generator.graph.bfs;

public abstract class BasePath {
	protected long key;
	protected int size;
	
	protected BasePath() {
		
	}
	
	public BasePath(int[] nodes) {
		this.size = nodes.length;
		assert size < 7 : "Path cannot exceed 6 layers.";
		this.key = combineForward(nodes);
		assert key > 0 : "key is negative. node id is not allowed more than 1000.";		
	}

	public long getKey() {
		return key;
	}
	
	public int getSize() {
		return size;
	}
	
	public int[] getNodeIds() {
		return unpack(key, size);
	}
	
	@Override
	public int hashCode() {
		 return Long.hashCode(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasePath other = (BasePath) obj;
		return key == other.key;
	}
	
	public long combineForward(int array[]) {
		long t = 0;
		for(int i = 0; i < array.length; i++) {
			int a = array[i] << i * 10;
			t = a | t;
		}
		return t;
	}
	
	public long combineBackward(int array[]) {
		long t = 0;
		for(int i = 0; i < array.length; i++) {
			int a = array[i] << i * 10;
			t = a | t;
		}
		return t;
	}
	
	public int[] unpack(long combined, int size) {
		int[] array = new int[size];
		for(int i = 0; i < size; i++) {
			array[i] = (int)combined >> (i * 10) & 0x3ff;
		}
		return array;
	}
}
