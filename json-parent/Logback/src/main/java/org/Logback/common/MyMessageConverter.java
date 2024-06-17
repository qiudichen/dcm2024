package org.Logback.common;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class MyMessageConverter extends MessageConverter {

	@Override
    public String convert(ILoggingEvent event) {
        String fqn = getFullyQualifiedName(event);
/*

        if (abbreviator == null) {
            return fqn;
        } else {
            return abbreviator.abbreviate(fqn);
        }
*/
        return fqn + "|||" + event.getFormattedMessage();
    }

    protected String getFullyQualifiedName(ILoggingEvent event) {
        StackTraceElement[] cda = event.getCallerData();
		if (cda != null && cda.length > 0) {
			if (cda.length == 1) {
				// no parent class
				return cda[0].getClassName() + "::" + cda[0].getMethodName() + ":" + cda[0].getLineNumber();
			} else {
				return cda[1].getClassName() + "::" + cda[1].getMethodName() + ":" + cda[1].getLineNumber();
			}

        } else {
            return CallerData.CALLER_DATA_NA;
        }
    }
}
