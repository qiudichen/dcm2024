package com.e2.wfm.gurobidemo.dcm;

import com.e2.wfm.gurobidemo.GurobiUtil;
import com.e2.wfm.gurobidemo.dcm.data.ModelInputConstant;
import com.e2.wfm.gurobidemo.dcm.data.ModelInputSingleDayTwoContactType;
import com.e2.wfm.gurobidemo.dcm.input.DCMModelInput;
import com.e2.wfm.gurobidemo.dcm.input.EmployeeSchedule;
import com.e2.wfm.gurobidemo.dcm.output.DCMModelOutput;
import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBModel;

import java.util.List;
import java.util.Map;

public class GurobiScheduleProbelm {

    public static void main(String[] args) {
	int iteration = 20;
	ModelInputConstant modelInputHelper = new ModelInputSingleDayTwoContactType();
	DCMModelInput modelInput = modelInputHelper.createModelInput(iteration);
	
	GRBEnv env = null;
	GRBModel model = null;
	try {
	    env = GurobiUtil.getGurobiEnvironment("dcmModel.log");
	    // Create a new model
	    model = new GRBModel(env);
	    GurobiSolver solver = new GurobiSolver(modelInput, model);
	    Map<Long, int[]> coverages = DCMModelOutput.getCoverages(modelInput.getEmployees(), modelInputHelper.getInterval());
	    
	    solver.initModel();
	    // Optimize model
	    for(int i = 1; i < iteration; i++) {
		int status = solver.solve();
		DCMModelOutput modelOutput = null;
		if(status == GRB.Status.OPTIMAL) {
		    modelOutput = solver.getValues();
		    modelOutput.setCoverages(coverages);
		    System.out.println("Objective Value: " + modelOutput.getObjectiveValue());
		} else {
		    System.out.println("Model is infeasible");
		    System.exit(1);
		}
		List<EmployeeSchedule> schedules = modelInputHelper.getBeseSchedules(modelOutput, modelInput.getEmployees());
		solver.nextIteration(schedules, i);
		coverages = DCMModelOutput.getCoverages(schedules, modelInputHelper.getInterval());
	    }
	} catch (Exception e) {
	    e.printStackTrace();	    
	} finally {
	    GurobiUtil.dispose(model);	
	    GurobiUtil.dispose(env);	
	}
    }
}
