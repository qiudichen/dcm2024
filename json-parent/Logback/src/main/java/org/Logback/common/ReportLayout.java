package org.Logback.common;

import ch.qos.logback.classic.PatternLayout;

public class ReportLayout extends PatternLayout {
    public ReportLayout() {
        DEFAULT_CONVERTER_MAP.put("m", MyMessageConverter.class.getName());
        DEFAULT_CONVERTER_MAP.put("msg", MyMessageConverter.class.getName());
        DEFAULT_CONVERTER_MAP.put("message", MyMessageConverter.class.getName());
        CONVERTER_CLASS_TO_KEY_MAP.put(MyMessageConverter.class.getName(), "message");
    }
}
