package org.Logback;

import org.Logback.common.ReportPatternLayoutEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import ch.qos.logback.core.rolling.RollingFileAppender;

public class LoggerReportWithData implements AutoCloseable, Report {
	
	private static final Logger loggerRoot = LoggerFactory.getLogger("root");
	private static final String DIR_PATH = "logs";
	private static final String EXTENSION = ".log";
	private static final String PATTERN = "%date|||%-4relative|||[%thread]|||%-5level|||%marker|||%msg%n";
		
	private static final LoggerReportWithData report = new LoggerReportWithData();
	
	private final ch.qos.logback.classic.Logger loggerReport = 
			(ch.qos.logback.classic.Logger)LoggerFactory.getLogger(LoggerReportWithData.class);
	
	private FileAppender<ILoggingEvent> fileAppender;
	
    public static LoggerReportWithData open(String fileName) {
    	report.openAppender(fileName);
        return report;
    }
    
    public static LoggerReportWithData getReport() {
    	return report;
    }
    
	private LoggerReportWithData() {
		createFileAppender();
	}

	private void openAppender(String fileName) {
		String fullName = getFile(fileName);
		if(fileAppender.isStarted() && fileAppender.getFile().equals(fullName)) {
			String msg = "Report already starts with name " + fullName;
			loggerRoot.warn(msg);
			return;
		}

		Appender<ILoggingEvent> existingAppender = loggerReport.getAppender(fileAppender.getName());
		if(existingAppender != null) {
			loggerReport.detachAppender(existingAppender);
			existingAppender.stop();
		}
		
		fileAppender.setFile(fullName);
		fileAppender.start();
        loggerReport.addAppender(fileAppender);
	}
	
	public void close() {
		boolean success = loggerReport.detachAppender(fileAppender);
		if(fileAppender.isStarted()) {
			fileAppender.stop();
		}
	}
	
	private String getFile(String fileName) {
		return DIR_PATH + "/" + fileName + EXTENSION;
	}
	
	private void createFileAppender() {
		if(fileAppender != null) {
			return;
		}
		
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        fileAppender = new RollingFileAppender<>(); //new FileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setName("report");
        
        PatternLayoutEncoderBase<ILoggingEvent> encoder = new ReportPatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(PATTERN);
        encoder.start();
        fileAppender.setEncoder(encoder);
        
        RollingFileAppender a = (RollingFileAppender)fileAppender;
        
        a.setRollingPolicy(null);
        a.setTriggeringPolicy(null);
	}
	
	public String getFileName() {
		if(this.fileAppender != null) {
			return this.fileAppender.getFile();
		} else {
			return null;
		}
	}
	
	public void info(String msg) {
		loggerReport.info(msg);
	}
	
	public void debug(String msg) {
		loggerReport.debug(msg);
	}
}