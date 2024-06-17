package com.e2.wfm.gurobidemo;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

import java.util.List;

public class ScheduleModel {
    public static void main(String[] args) {
	GRBEnv env = null;
	GRBModel model = null;
        try {
            // Create a new environment
	    env = GurobiUtil.getGurobiEnvironment("dcmModel.log");
	    
	    // Create a new model
	    model = new GRBModel(env);
	    int numEmp = 10;
	    int numSchedules = 3;
	    double[][] requirements = null; 
	    int numCt = 1;
	    int numSG = 1;
	    
	    Model modelObj = new Model(model, numEmp, numSchedules, requirements, numCt, numSG);
	    
	    // Create variables
	    
	    // Optimize model
	    model.write("model.lp");
	    model.write("model.rlp");
	    model.write("model.mps");
	    model.write("model.rew");
	    model.optimize();
	    
	    int status = model.get(GRB.IntAttr.Status);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    GurobiUtil.dispose(model);	
	    GurobiUtil.dispose(env);	
	}
    }
    
    public static void createModel(Model modelObj, int numEmp, int numSchedules, double[][] requirements) throws GRBException {
	createScheduleVariables(modelObj, numEmp, numSchedules);
	
    }
    
    public static void createScheduleVariables(Model modelObj, int numEmp, int numSchedules) throws GRBException {
	GRBVar[][] scheduleVars = new GRBVar[numEmp][numSchedules];
	GRBConstr[] scheduleConstrs = new GRBConstr[numEmp];
	for(int empIndex = 0; empIndex < numEmp; empIndex++) {
	    // One Schedule Candidate Per Employee Constraint 
	    GRBLinExpr expr = new GRBLinExpr();
	    for(int scheduleIndex = 0; scheduleIndex < numSchedules; scheduleIndex++) {
		scheduleVars[empIndex][scheduleIndex] = modelObj.getModel().addVar(0.0, GRB.INFINITY, 0.0d, GRB.CONTINUOUS, 
			"s" + empIndex + "_" + scheduleIndex);
		expr.addTerm(1.0, scheduleVars[empIndex][scheduleIndex]);
	    }
	    scheduleConstrs[empIndex] = modelObj.getModel().addConstr(expr, GRB.EQUAL, 1.0, "OneCandidatePer");
        }
	modelObj.setScheduleVars(scheduleVars);
	modelObj.setScheduleConstrs(scheduleConstrs);
    }
    
    public static void createSkillGroupConstraints(Model modelObj, int numCt, int numSG) throws GRBException {

    }

    static class InputModel {
	
    }
    
    static class Model {
	private GRBModel model;
	private int numEmp;
	private int numSchedules;
	private double[][] requirements;
	private int numCt;
	private int numSG;
	
	
	private GRBVar scheduleVars[][];
	private GRBConstr scheduleConstrs[];
	public Model(GRBModel model, int numEmp, int numSchedules, double[][] requirements, int numCt, int numSG) {
	    super();
	    this.model = model;
	    this.numEmp = numEmp;
	    this.numSchedules = numSchedules;
	    this.requirements = requirements;
	    this.numCt = numCt;
	    this.numSG = numSG;
	}
	public GRBModel getModel() {
	    return model;
	}
	public void setModel(GRBModel model) {
	    this.model = model;
	}
	public int getNumEmp() {
	    return numEmp;
	}
	public void setNumEmp(int numEmp) {
	    this.numEmp = numEmp;
	}
	public int getNumSchedules() {
	    return numSchedules;
	}
	public void setNumSchedules(int numSchedules) {
	    this.numSchedules = numSchedules;
	}
	public double[][] getRequirements() {
	    return requirements;
	}
	public void setRequirements(double[][] requirements) {
	    this.requirements = requirements;
	}
	public int getNumCt() {
	    return numCt;
	}
	public void setNumCt(int numCt) {
	    this.numCt = numCt;
	}
	public int getNumSG() {
	    return numSG;
	}
	public void setNumSG(int numSG) {
	    this.numSG = numSG;
	}
	public GRBVar[][] getScheduleVars() {
	    return scheduleVars;
	}
	public void setScheduleVars(GRBVar[][] scheduleVars) {
	    this.scheduleVars = scheduleVars;
	}
	public GRBConstr[] getScheduleConstrs() {
	    return scheduleConstrs;
	}
	public void setScheduleConstrs(GRBConstr[] scheduleConstrs) {
	    this.scheduleConstrs = scheduleConstrs;
	}
    }
}
