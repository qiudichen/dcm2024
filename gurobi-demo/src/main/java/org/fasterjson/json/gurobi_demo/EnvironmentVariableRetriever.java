package org.fasterjson.json.gurobi_demo;

public class EnvironmentVariableRetriever {
    public String getParameter(String parameterName) throws NoSuchFieldException {
        String secretValue = System.getenv(parameterName);
        
        
        if (secretValue == null) {
        	secretValue = System.getProperty(parameterName);
        	if (secretValue == null) {
        		 throw new NoSuchFieldException(parameterName + " does not exist in environment variable.");
        	}
        }
        return secretValue;
    }
}
