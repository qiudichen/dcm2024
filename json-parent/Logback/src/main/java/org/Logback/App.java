package org.Logback;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
    	Map<Long, TestObj[]> map = new HashMap<>();
    	TestObj[] objs = map.computeIfAbsent(10l, x -> new TestObj[10]);
    	for(int i = 0; i < 10; i++) {
    		TestObj obj = objs[i];
    		if(obj == null) {
    			objs[i] = new TestObj();
    		}
    	}
    	
        logger.trace("Entering method foo()");
        logger.debug("Received request from 198.12.34.56");
        logger.info("User logged in: john");
        logger.warn("Connection to server lost. Retrying...");
        logger.error("Failed to write data to file: myFile.txt");
        
        try (LoggerReportWithData report = LoggerReportWithData.open("report1")) {
        	report.info("To report1: Report info message ");
        	report.debug("To report1: Report debug message ");  
        	String name = report.getFileName();
        	System.out.println(name);
        } finally {
        	LoggerReportWithData r = LoggerReportWithData.getReport();
        	String name = r.getFileName();
        	System.out.println(name);
        	
        } 
        
        try (LoggerReportWithData report = LoggerReportWithData.open("report2")) {
        	report.info("To report2: Report info message ");
        	report.debug("To report2: Report debug message ");
        } finally {
        	LoggerReportWithData r = LoggerReportWithData.getReport();
        	String name = r.getFileName();
        } 
        
        long[] ids1 = new long[] {2,3,4,1};
        long[] ids2 = new long[] {1,2,3,4};
        long[] ids3 = new long[] {1,2,3,4,5};
        long[] ids4 = new long[] {1,2,3};
        long[] ids5 = new long[] {};
        long[] ids6 = null;
        long[] ids7 = null;
        
        Arrays.sort(ids1);
        
        boolean flag = Arrays.equals(ids1, ids2);
        flag = Arrays.equals(ids1, ids3);
        flag = Arrays.equals(ids1, ids4);
        flag = Arrays.equals(ids1, ids5);
        flag = Arrays.equals(ids1, ids6);
        flag = Arrays.equals(ids6, ids7);
        flag = Arrays.equals(ids5, ids6);
        
   }
}
