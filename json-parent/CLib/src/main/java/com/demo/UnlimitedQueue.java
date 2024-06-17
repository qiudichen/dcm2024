package com.demo;

public class UnlimitedQueue<E> implements Queue<E> {
    private static final int DEFAULE_CAPACIEY = 10;
    
    private static final int INCREASE_CAPACITY = 10;

	//Array Hold the queue item
	private Object[] elementData;
	
	//indicates the first queue item location
	private int header;

	//indicates the location of the new queue item should be added
	private int tailer;
	
	//indicates the current Queue Size (Not the max size)
	private int currentSize = 0;
	
	public UnlimitedQueue(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else {
        	this.elementData = new Object[DEFAULE_CAPACIEY];
        }
	}
	
	/**
	 * add item to queue
	 * Ehe method must be synchronized in order to thread safe
	 * 
	 * @param data : queue item to be add
	 * @return: true if added, false if queue is full
	 */	
	@Override
	synchronized public boolean enqueue(E data) {
		//check if queue is full
		if(currentSize == elementData.length) {
			//increase elementData size
			grow();
			return false;
		}
		
		//add item to the end
		elementData[tailer] = (Object)data;
		
		//move the location of tailer to next position
		tailer = increase(tailer);	
		
		//increase the size of current size 
		currentSize++;
		return true;
	}

	/**
	 * get the first queue item and remove it from queue
	 * Ehe method must be synchronized in order to thread safe
	 * 
	 * @return: the first queue item, or null if queue is empty
	 */		
	@Override
	synchronized public E dequeue() {
		E data = null;
		
		if(currentSize == 0) {
			//Queue is empty
			return data;
		}
			
		data = (E)elementData[header];
		
		//set the location to null so that the queue doesn't hold the queue item any more
		elementData[header] = null;
		
		//move the header to next location
		header = increase(header);
		
		//reduce the current size
		currentSize--;
		return data;
	}

    private void grow() {
    	int oldCapacity = this.elementData.length;
    	Object[] newElementData = new Object[oldCapacity + INCREASE_CAPACITY];
    	//copy data to newElementData
    	int index = header;
    	for(int i = 0; i < this.elementData.length; i++) {
    		newElementData[i] = this.elementData[index];
    		index = increase(index);
    	}
    	
    	//assign new array
    	this.elementData = newElementData;
    	
    	//reset header and tailer
    	this.header = 0;
    	this.tailer = oldCapacity;
    }	
    
	private int increase(int val) {
		//increase one value
		val++;
		
		//if it exceeds the max array size, start over to 0
		if(val >= elementData.length) {
			val = 0;
		}
		return val;
	}
	
	public static void main(String[] argv) {
		int cap = 3;
		int header = 0;
		for(int i = 0; i < 10; i++) {
			System.out.println(header);
			header = (++header) % cap;
		}
		System.out.println(header);
	}
}
