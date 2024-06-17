package org.Logback.common;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

public class ReportPatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {
	
    @Override
    public void setLayout(Layout<ILoggingEvent> layout) {
       // throw new UnsupportedOperationException("one cannot set the layout of " + this.getClass().getName());
    }

    @Override
    public void start() {
        PatternLayout patternLayout = new ReportLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
        this.layout = patternLayout; //undo damage done by parent class
    }
}
