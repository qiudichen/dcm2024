package com.e2.jdk21.jdk21demo;

import java.util.List;
import java.util.concurrent.ThreadFactory;

public class VirtualThread implements Runnable {
	private int id;
	
	public VirtualThread(int id) {
		this.id = id;
	}
	
	public static void main(String[] args) {
		
		WithUserSession session = new WithUserSession();
		session.processWithUser("testUser");
		session.processWithUser("testUser1");

		WithUserSession session1 = new WithUserSession();
		session1.processWithUser("testUser2");
		
		for (var i = 1; i < 10; i++) {
			Thread virtualThread = Thread.ofVirtual().unstarted(new VirtualThread(i));
			virtualThread.start();
			 String str = String.format("Java 21 virtual thread number %s is running.", i);
			 System.out.println(str);
		}
	}

	@Override
	public void run() {
		System.out.println("Virtual thread is running");
		WithUserSession user = new WithUserSession();
		String id1 = "testUser1====" + id;
		user.processWithUser(id1);
	}

	private static void patternMatching(Object obj) {
		switch (obj) {
		case Integer i -> {
			System.out.println("Integer: " + i);
		}
		case String s -> System.out.println("String: " + s);
		default -> System.out.println("Unknown type");
		}
	}
}
