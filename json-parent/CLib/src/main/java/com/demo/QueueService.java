package com.demo;

public class QueueService<T> {
	private int maxSize;
	
	private Object[] queueData;
	
	private int header;
	
	private int tailer;
	
	private int queueSize = 0;
	
	public QueueService(int maxSize) {
		this.maxSize = maxSize;
		header = 0;
		tailer = 0;
		queueSize = 0;
		queueData = new Object[maxSize];
	}
	
	synchronized public boolean enqueue(T data) {
		if(queueSize == maxSize) {
			return false;
		}
		queueData[tailer] = (Object)data;
		tailer = increase(tailer);	
		queueSize++;
		return true;
	}
		
	synchronized public T dequeue() {
		T data = null;
		
		if(queueSize == 0) {
			//Queue is empty
			return data;
		}
			
		data = (T)queueData[header];
		//clear up
		queueData[header] = null;
		header = increase(header);
		queueSize--;
		return data;
	}
	
	public int getQueueSize() {
		return queueSize;
	}
	
	private int increase(int val) {
		val++;
		if(val >= maxSize) {
			val = 0;
		}
		return val;
	}
}

