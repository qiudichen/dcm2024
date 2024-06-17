package com.e2.jdk21.jdk21demo;

public class WithUserSession {
	private final static ScopedValue<String> USER_ID = ScopedValue.newInstance();
	
	public void processWithUser(String sessionUserId) {
		// sessionUserId is bound to the ScopedValue USER_ID for the execution of the 
		// runWhere method, the runWhere method invokes the processRequest method.
		ScopedValue.runWhere(USER_ID, sessionUserId, () -> processRequest());
	 }	
	
	public void processRequest() {
		System.out.println(USER_ID.get());
	}
}
