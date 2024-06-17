package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBVar;

import java.util.HashMap;
import java.util.Map;

public class SkillGroupConstraint extends Constraint {
    //FTE constraint in each interval
    private GRBConstr[] constrs;
    
    //Ygti group by contact type
    private Map<Long, GRBVar[]> variables;

    public SkillGroupConstraint(long id, GRBConstr[] constrs) {
	super(id);
	this.constrs = constrs;
	this.variables = new HashMap<>();
    }

    public GRBConstr[] getConstrs() {
        return constrs;
    }

    public GRBConstr getConstr(int interval) {
        return constrs[interval];
    }
    
    public Map<Long, GRBVar[]> getVariables() {
        return variables;
    }
    
    public void addVariable(GRBVar ygti, long contactType, int interval) {
	GRBVar[] vars = variables.computeIfAbsent(contactType, f -> new GRBVar[constrs.length]);
	vars[interval] = ygti;
    }
}
