package com.demo;

public interface Queue<E> {
	
	public boolean enqueue(E data);
	
	public E dequeue();
}
