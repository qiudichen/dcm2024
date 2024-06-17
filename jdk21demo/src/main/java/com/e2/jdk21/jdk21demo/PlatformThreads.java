package com.e2.jdk21.jdk21demo;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class PlatformThreads 
{
    public static void main( String[] args )
    {
    	var i = 10_000;

    	testPlatformThreads(1000);
        testPlatformThreads(i);
        testPlatformThreads(100_000);
        testPlatformThreads(1_000_000);
    }
    
    private static void testPlatformThreads(int maximum) {
        long time = System.currentTimeMillis();
 
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, maximum).forEach(i -> {
            	
				System.out.println("Thread " + i + " is running");
                executor.submit(() -> {
                	System.out.println("Thread id: " + Thread.currentThread().threadId());
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }
 
        time = System.currentTimeMillis() - time;
        System.out.println("Number of threads = " + maximum + ", Duration(ms) = " + time);
    }    
}
