package com.e2.wfm.gurobidemo.dcm;

import com.e2.wfm.gurobidemo.dcm.constraint.ContactTypeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.EmployeeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.MaxContactTypeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.SchedulingUnitConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.SkillGroupConstraint;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

public class Register {
    private final GRBModel model;
    
    private SkillGroupConstraint[] groupConstraints;
    
    private ContactTypeConstraint[] contactTypeConstraints;
    
    private MaxContactTypeConstraint[] maxContactTypeConstraints;
    
    private SchedulingUnitConstraint[] schedulingUnitConstraints;
    
    private EmployeeConstraint[] employeeConstraints;
    
    public Register(GRBModel model) {
	super();
	this.model = model;
    }

    public GRBModel getModel() {
        return model;
    }

    public void setGroupConstraints(SkillGroupConstraint[] groupConstraints) {
        this.groupConstraints = groupConstraints;
    }
    
    public void setCtConstraints(ContactTypeConstraint[] contactTypeConstraints) {
        this.contactTypeConstraints = contactTypeConstraints;
    }

    public ContactTypeConstraint[] getContactTypeConstraints() {
        return contactTypeConstraints;
    }
    
    public ContactTypeConstraint getContactTypeConstraint(long contactTypeId) {
	return contactTypeConstraints[(int)contactTypeId];
    }

    public SkillGroupConstraint[] getSkillGroupConstraints() {
	return groupConstraints;
    }
    
    public GRBVar getSkillGroupVariables(long skillGroupId, long contactTypeId, int interval) {
	return groupConstraints[(int)skillGroupId].getVariables().get(contactTypeId)[interval];
    }

    public SchedulingUnitConstraint[] getSchedulingUnitConstraints() {
        return schedulingUnitConstraints;
    }

    public void setSchedulingUnitConstraints(SchedulingUnitConstraint[] schedulingUnitConstraints) {
	this.schedulingUnitConstraints = schedulingUnitConstraints;
    }

    public SkillGroupConstraint[] getGroupConstraints() {
        return groupConstraints;
    }

    public SchedulingUnitConstraint getSchedulingUnitConstraint(long schedulingUnitId) {
	return schedulingUnitConstraints[(int)schedulingUnitId];
    }
    
    public void setEmployeeConstraints(EmployeeConstraint[] employeeConstraints) {
	this.employeeConstraints = employeeConstraints;
    }
    
    public EmployeeConstraint[] getEmployeeConstraints() {	
	return this.employeeConstraints;
    }
    
    public SkillGroupConstraint getSkillGroupConstraint(long skillGroupId) {
	return groupConstraints[(int)skillGroupId];
    }

    public MaxContactTypeConstraint[] getMaxContactTypeConstraints() {
        return maxContactTypeConstraints;
    }

    public void setMaxContactTypeConstraints(MaxContactTypeConstraint[] maxContactTypeConstraints) {
        this.maxContactTypeConstraints = maxContactTypeConstraints;
    }
}
