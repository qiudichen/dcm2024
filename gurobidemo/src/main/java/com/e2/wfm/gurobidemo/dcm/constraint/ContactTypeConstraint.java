package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBVar;

public class ContactTypeConstraint extends Constraint {
    private GRBConstr[] constrs;
    private GRBVar[] unders;
    private GRBVar[] overs;
    
    public ContactTypeConstraint(long id, GRBConstr[] constrs, GRBVar[] unders, GRBVar[] overs) {
	super(id);
	this.constrs = constrs;
	this.unders = unders;
	this.overs = overs;
    }

    public GRBConstr[] getConstrs() {
        return constrs;
    }

    public GRBVar[] getUnders() {
        return unders;
    }

    public GRBVar[] getOvers() {
        return overs;
    }
}
