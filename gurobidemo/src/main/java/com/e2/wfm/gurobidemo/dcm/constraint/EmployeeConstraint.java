package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBVar;

import java.util.HashMap;
import java.util.Map;

public class EmployeeConstraint extends Constraint {
    private final long groupId;
    private final long schedulingUnitId;
    
    private GRBConstr constr;
    
    private Map<Integer, ScheduleCandidate> scheduleVars;

    private ScheduleCandidate scheduleVar;
    
    public EmployeeConstraint(long id, long groupId, long schedulingUnitId, GRBConstr constr, GRBVar schaduleVar, int[] schedules) {
	super(id);
	this.groupId = groupId;
	this.schedulingUnitId = schedulingUnitId;
	this.constr = constr;
	scheduleVars = new HashMap<>();
	addIterationScheduleVar(schaduleVar, schedules, 0);
    }

    public GRBConstr getConstr() {
	return constr;
    }
    
    public void addIterationScheduleVar(GRBVar schaduleVar, int[] schedules, int interation) {
	if (scheduleVars.containsKey(interation)) {
	    throw new RuntimeException("Schedule already added for iteration: " + interation);
	}
	if (scheduleVar != null) {
	    throw new RuntimeException("Previous schedule is not updated. iteration: " + interation);
	}
	
	scheduleVar = new ScheduleCandidate(interation, schaduleVar, schedules);
	scheduleVars.put(interation, scheduleVar);
    }
    
    public boolean hasVariable() {
	return this.scheduleVar != null;
    }
    
    public void updateScheduleVar() {
	this.scheduleVar.setUpdated();
	this.scheduleVar = null;
    }
    
    public ScheduleCandidate getScheduleVar() {
	return scheduleVar;
    }
    
    public long getGroupId() {
	return groupId;
    }
    
    public long getSchedulingUnitId() {
	return schedulingUnitId;
    }

    public Map<Integer, ScheduleCandidate> getScheduleVars() {
        return scheduleVars;
    }
}
