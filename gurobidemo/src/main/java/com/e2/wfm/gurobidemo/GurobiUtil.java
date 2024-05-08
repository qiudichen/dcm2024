package com.e2.wfm.gurobidemo;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBModel;

public class GurobiUtil {
    public static final String GUROBI_KEY_10 = "J6ZBUZ7Q";
    public static final String GUROBI_KEY_11 = "0MXJN1TW";
    public static final String GUROBI_ISV_NAME = "Nice";
    public static final String APPLICATION_NAME = "DCM";
    public static final int GUROBI_ISV_EXPIRATION_DATE = 20260101;

    public static GRBEnv getGurobiEnvironment(String logfile) throws Exception {
	String isvKey = GUROBI_KEY_11;
	// don't set log to file, it may hit max limitation of file handler in windows
	GRBEnv env = new GRBEnv(null, GUROBI_ISV_NAME, APPLICATION_NAME, GUROBI_ISV_EXPIRATION_DATE, isvKey);

	env.set(GRB.IntParam.Threads, Runtime.getRuntime().availableProcessors());
	env.set("logFile", logfile);
	return env;
    }

    public static void dispose(GRBEnv env) {
	try {
	    if(env != null) {
		env.dispose();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void dispose(GRBModel model) {
	try {
	    if(model != null) {
		model.dispose();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }    
}
