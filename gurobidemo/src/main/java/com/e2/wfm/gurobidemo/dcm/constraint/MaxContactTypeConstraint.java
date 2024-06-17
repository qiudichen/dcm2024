package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBVar;

public class MaxContactTypeConstraint extends Constraint {
    GRBVar ctMaxUnder;
    GRBConstr[] ctMaxUnderConstrs;
    GRBVar ctMaxOver;
    GRBConstr[] ctMaxOverConstrs;
    
    public MaxContactTypeConstraint(long id, GRBVar ctMaxUnder, GRBConstr[] ctMaxUnderConstrs, GRBVar ctMaxOver,
	    GRBConstr[] ctMaxOverConstrs) {
	super(id);
	this.ctMaxUnder = ctMaxUnder;
	this.ctMaxUnderConstrs = ctMaxUnderConstrs;
	this.ctMaxOver = ctMaxOver;
	this.ctMaxOverConstrs = ctMaxOverConstrs;
    }

    public GRBVar getCtMaxUnder() {
        return ctMaxUnder;
    }

    public GRBConstr[] getCtMaxUnderConstrs() {
        return ctMaxUnderConstrs;
    }

    public GRBVar getCtMaxOver() {
        return ctMaxOver;
    }

    public GRBConstr[] getCtMaxOverConstrs() {
        return ctMaxOverConstrs;
    }
}
