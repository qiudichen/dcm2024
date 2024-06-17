package com.demo;

import java.util.Random;

public class ThreadClient extends Thread {
	private QueueService<String> queueService;
	private static Random rn = new Random();
	
	public ThreadClient(QueueService<String> queueService) {
		this.queueService = queueService;
	}
	
	public void run() 
    { 
		System.out.println("Run Thread " + this.getId());
		while(true) {
			String outMsg = this.queueService.dequeue();
			if(outMsg == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("remove Message " + outMsg + " by thread: " + this.getId());
			}
		}
    } 
	
	private boolean enQueue() {
		int v = rn.nextInt() % 2;
		return v==0;
	}
	
	public static void main(String[] argv) {
		QueueService<String> queueService = new QueueService<>(5);
		
		System.out.println(queueService.enqueue("test1"));
//		System.out.println(queueService.enqueue("test2"));
//		System.out.println(queueService.enqueue("test3"));
//		System.out.println(queueService.enqueue("test4"));
//		System.out.println(queueService.enqueue("test5"));
		
		ThreadClient[] clients = new ThreadClient[1];
		
		for(int i = 0; i < clients.length; i++) {
			clients[i] = new ThreadClient(queueService);
		}

		for(ThreadClient client : clients) {
			client.start();
		}

		try {
			while(true) {
				Thread.sleep(10000);
				if(isFinished(clients)) {
					System.exit(0);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static boolean isFinished(ThreadClient[] clients) {
		for(ThreadClient client : clients) {
			if(client.isAlive()) {
				return false;
			}
		}
		return true;
	}
}
