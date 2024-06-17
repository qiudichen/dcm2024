package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBVar;

public class SchedulingUnitConstraint extends Constraint {
    private GRBConstr[] constrs;
    private GRBVar[] lowerSlacks;
    private GRBVar[] upperSlacks;
    
    public SchedulingUnitConstraint(long id, GRBConstr[] constrs, GRBVar[] lowerSlacks, GRBVar[] upperSlacks) {
	super(id);
	this.constrs = constrs;
	this.lowerSlacks = lowerSlacks;
	this.upperSlacks = upperSlacks;
    }

    public GRBConstr[] getConstrs() {
        return constrs;
    }
    
    public GRBConstr getConstr(int interval) {
	return constrs[interval];
    }

    public GRBVar[] getLowerSlacks() {
        return lowerSlacks;
    }

    public GRBVar[] getUpperSlacks() {
        return upperSlacks;
    }
}
