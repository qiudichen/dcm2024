package com.e2.wfm.gurobidemo;

import static com.gurobi.gurobi.GRB.MINIMIZE;

import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBEnv;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class Lpmod {

    public static void main(String[] args) {
	GRBEnv env = null;
	GRBModel model = null;
        try {
            // Create a new environment
	    env = GurobiUtil.getGurobiEnvironment("lpmod.log");
	    
	    // Create a new model
	    model = new GRBModel(env);
	    
	    // Create variables
	    GRBVar x = model.addVar(0.0, GRB.INFINITY, 1.0d, GRB.CONTINUOUS, "x");
	    GRBVar y = model.addVar(0.0, GRB.INFINITY, 1.0d, GRB.CONTINUOUS, "y");
	    
	    // Set objective
	    model.set(GRB.IntAttr.ModelSense, MINIMIZE);
	    
	    
	    // Add constraint:  2x + 3y >= 15 
	    GRBLinExpr expr = new GRBLinExpr();
	    expr.addTerm(2.0, x);
	    expr.addTerm(3.0, y);
	    GRBConstr constr1 = model.addConstr(expr, GRB.GREATER_EQUAL, 15.0, "c0");
	    
	    // Add constraint:  3x + 2y >= 15 
	    expr = new GRBLinExpr();
	    expr.addTerm(3.0, x);
	    expr.addTerm(2.0, y);
	    GRBConstr constr2 = model.addConstr(expr, GRB.GREATER_EQUAL, 15.0, "c1");
	    
	    // Optimize model
	    model.write("model.lp");
	    model.write("model.rlp");
	    model.write("model.mps");
	    model.write("model.rew");
	    model.optimize();
	    
	    int status = model.get(GRB.IntAttr.Status);
	    
	    if(status == GRB.Status.INFEASIBLE) {
		System.out.println("Model is infeasible");
	    } else if(status == GRB.Status.OPTIMAL) {
		double objectValue = model.get(GRB.DoubleAttr.ObjVal);
		System.out.println(x.get(GRB.StringAttr.VarName) + " = " + x.get(GRB.DoubleAttr.X));
		System.out.println(y.get(GRB.StringAttr.VarName) + " = " + y.get(GRB.DoubleAttr.X));
		System.out.println("Dual Value of " + constr1.get(GRB.StringAttr.ConstrName) + " = " 
			+ constr1.get(GRB.DoubleAttr.Pi));
		System.out.println("Dual Value of " + constr2.get(GRB.StringAttr.ConstrName) + " = " 
			+ constr2.get(GRB.DoubleAttr.Pi));
	    } else {
		System.out.println("Model is not solved. Status = " + status);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    GurobiUtil.dispose(model);	
	    GurobiUtil.dispose(env);	
	}
    }
}
