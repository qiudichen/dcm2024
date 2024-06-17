package org.Logback.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

	private boolean started = false;
	
	/*
	 * configuration order:
	 * context, property, environment
	 */
	@Override
	public void start() {
		if (started) return;

        String tenant_id = System.getProperty("tenant_id"); 
        String env_tenant_id = System.getenv("tenant_id");

        Context context = getContext();
        context.putProperty("TENANT_ID", tenant_id);
        started = true;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isResetResistant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLevelChange(Logger arg0, Level arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReset(LoggerContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(LoggerContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(LoggerContext arg0) {
		// TODO Auto-generated method stub

	}

}
