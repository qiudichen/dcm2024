package com.e2.wfm.gurobidemo.dcm.constraint;

import com.gurobi.gurobi.GRBVar;

public class ScheduleCandidate {
    private int iteration;
    private GRBVar scheduleVar;
    private int[] schedules;
    private boolean updated;

    public ScheduleCandidate(int iteration, GRBVar scheduleVar, int[] schedules) {
	super();
	this.iteration = iteration;
	this.scheduleVar = scheduleVar;
	this.schedules = schedules;
	this.updated = false;
    }

    public boolean isUpdated() {
	return updated;
    }

    public void setUpdated() {
	this.updated = true;
    }

    public int getIteration() {
	return iteration;
    }

    public GRBVar getScheduleVar() {
	return scheduleVar;
    }

    public int[] getSchedules() {
	return schedules;
    }
}
