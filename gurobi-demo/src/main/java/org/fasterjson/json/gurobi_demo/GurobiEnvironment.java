package org.fasterjson.json.gurobi_demo;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;

public class GurobiEnvironment {
    private static final String ENVIRONMENT_VARIABLE_VALUE_CONVENTION = "_VALUE";
    private static final String ENVIRONMENT_VARIABLE_KEY_HANDLE_CONVENTION = "_HANDLE";
    
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	private final static Map<String, String> parameters = new HashMap<>();
	
	private final static EnvironmentVariableRetriever environmentVariableRetriever = new EnvironmentVariableRetriever();
	
	private final static ParameterStoreRetriever parameterStoreRetriever = new ParameterStoreRetriever();
	
	private final static StandardWriter writer = new StandardWriter();
	
	private final static String LOG_PATH = "gurobi1111.log";
    public static GRBEnv getGurobiEnvironment() throws GRBException, NoSuchFieldException {
        try {
        	
			String isvKey = getParameter(GurobiISVConstants.GUROBI_ISV_PARAMETER_STORE_KEY);
			return new GRBEnv(LOG_PATH, GurobiISVConstants.GUROBI_ISV_NAME, GurobiISVConstants.APPLICATION_NAME,
					GurobiISVConstants.GUROBI_ISV_EXPIRATION_DATE, isvKey);
        } catch (GRBException | NoSuchFieldException e) {
            throw e;
        }
    }
    
    public static GRBEnv getGurobiEnvironmentNoLog() throws GRBException, NoSuchFieldException {
    	try {
    		
    		String isvKey = getParameter(GurobiISVConstants.GUROBI_ISV_PARAMETER_STORE_KEY);
    		return new GRBEnv(null, GurobiISVConstants.GUROBI_ISV_NAME, GurobiISVConstants.APPLICATION_NAME,
    				GurobiISVConstants.GUROBI_ISV_EXPIRATION_DATE, isvKey);
    	} catch (GRBException | NoSuchFieldException e) {
    		throw e;
    	}
    }
    
    public static GRBEnv getGurobiEnvironment1() throws GRBException, NoSuchFieldException {
        try {
			String isvKey = getParameter(GurobiISVConstants.GUROBI_ISV_PARAMETER_STORE_KEY);
			GRBEnv env = new GRBEnv(true);
			env.set("GURO_PAR_ISVNAME", GurobiISVConstants.GUROBI_ISV_NAME);
			env.set(GRB.StringParam.CSAppName, GurobiISVConstants.APPLICATION_NAME);
			env.set("GURO_PAR_ISVEXPIRATION", "" + GurobiISVConstants.GUROBI_ISV_EXPIRATION_DATE);
			env.set(GRB.StringParam.CloudSecretKey,isvKey);
			env.set(GRB.StringParam.LogFile.name(), LOG_PATH);
			return env;
        } catch (GRBException | NoSuchFieldException e) {
            throw e;
        }
    }
    
    public static GRBEnv getGurobiEnvironment1NoLog() throws GRBException, NoSuchFieldException {
    	try {
    		String isvKey = getParameter(GurobiISVConstants.GUROBI_ISV_PARAMETER_STORE_KEY);
    		GRBEnv env = new GRBEnv(true);
    		env.set("GURO_PAR_ISVNAME", GurobiISVConstants.GUROBI_ISV_NAME.toUpperCase());
    		env.set("GURO_PAR_ISVAPPNAME", GurobiISVConstants.APPLICATION_NAME);
    		env.set("GURO_PAR_ISVEXPIRATION", "" + GurobiISVConstants.GUROBI_ISV_EXPIRATION_DATE);
    		env.set("GURO_PAR_ISVKEY",isvKey);
    		return env;
    	} catch (GRBException | NoSuchFieldException e) {
    		throw e;
    	}
    }
    
    private static String getParameter(String parameterName) throws NoSuchFieldException {
        if (parameters.containsKey(parameterName)) {
            return parameters.get(parameterName);
        } else {
            return getParameterFromRetrievers(parameterName);
        }
    }
    
    private static String getParameterFromRetrievers(String parameterName) throws NoSuchFieldException {
        String secretManagerValue = null;
        try {
            secretManagerValue = environmentVariableRetriever.getParameter(parameterName + ENVIRONMENT_VARIABLE_VALUE_CONVENTION);
        } catch (NoSuchFieldException e) {
            try {
                String parameterStoreKeyName = environmentVariableRetriever.getParameter(parameterName + ENVIRONMENT_VARIABLE_KEY_HANDLE_CONVENTION);
                secretManagerValue = parameterStoreRetriever.getParameter(parameterStoreKeyName);
            } catch (NoSuchFieldException ex) {
                throw ex;
            }
        }

        parameters.put(parameterName, getValueFromSecretManagerValue(secretManagerValue));
        return parameters.get(parameterName);
    }
    
    private static String getValueFromSecretManagerValue(String secretManagerValue) throws NoSuchFieldException {
        try {
            Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(secretManagerValue).elements();
            JsonNode jsonNode = jsonNodeIterator.next();
            return jsonNode.asText();
        } catch (Exception e) {
            throw new NoSuchFieldException("SecretManager value could not be parsed.");
        }
    }
    
    public static void main(String[] argv) {

    	System.setProperty("GUROBI_ISV_KEY_VALUE", "{\"ISV\":\"J6ZBUZ7Q\"}");
    	System.setProperty("GUROBI_ISV_KEY_HANDLE", "/aws/reference/secretsmanager/dcm/batch/ISV");
    	
    	System.out.println("--- start test 1: constructor with log --------------------------");
    	int index = 0;
    	try {
            GRBEnv env = GurobiEnvironment.getGurobiEnvironment();
            for (int i = 0; i < 1000; i++) {
            	index = i;
            	GRBModel model = new GRBModel(env);
            	File file = new File(LOG_PATH);
            	if(file.exists()) {
            		file.delete();
            	}
            }
            System.out.println("------- test 1 All passed");
        } catch (GRBException | java.lang.NoSuchFieldException e) {
        	System.out.println("--- test 1 error in iteration: " + index);
        	e.printStackTrace();
        }
    	
    	System.out.println("--- start test 2: constructor without log --------------------------");
    	index = 0;
    	try {
    		GRBEnv env = GurobiEnvironment.getGurobiEnvironmentNoLog();
    		for (int i = 0; i < 5000; i++) {
    			index = i;
    			GRBModel model = new GRBModel(env);
    		}
    		System.out.println("------- test 2 All passed -- " + index);
    	} catch (GRBException | java.lang.NoSuchFieldException e) {
    		System.out.println("--- test 2 error in iteration: " + index);
    		e.printStackTrace();
    	}
    	
    	System.out.println("--- start test 3: empty constructor with log --------------------------");
    	index = 0;
    	try {
    		GRBEnv env1 = GurobiEnvironment.getGurobiEnvironment1();
    		for (int i = 0; i < 2000; i++) {
    			GRBModel model1 = new GRBModel(env1);
            	File file = new File(LOG_PATH);
            	if(file.exists()) {
            		file.delete();
            	}
    		}
    		System.out.println("------- test 3 All passed");
    	} catch (GRBException | java.lang.NoSuchFieldException e) {
    		System.out.println("--- test 3 error in iteration: " + index);
    		e.printStackTrace();
    	}
    	
    	System.out.println("--- start test 4: empty constructor without log getGurobiEnvironment1NoLog --------------------------");
    	index = 0;
    	try {
    		GRBEnv env1 = GurobiEnvironment.getGurobiEnvironment1NoLog();
    		for (int i = 0; i < 1000; i++) {
    			GRBModel model1 = new GRBModel(env1);
    		}
    		System.out.println("------- test 4 All passed");
    	} catch (GRBException | java.lang.NoSuchFieldException e) {
    		System.out.println("--- test 4 error in iteration: " + index);
    		e.printStackTrace();
    	}
    }
}
