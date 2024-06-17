package com.e2.wfm.gurobidemo.dcm.input;

import java.util.List;

public class SchedulingUnit extends Entity {
    private List<Employee> employees;
    private final int[] minimumSeatLimits;
    private final int[] maximumSeatLimits;
    
    public SchedulingUnit(long id, int[] minimumSeatLimits, int[] maximumSeatLimits, List<Employee> employees) {
	super(id);
	this.minimumSeatLimits = minimumSeatLimits;
	this.maximumSeatLimits = maximumSeatLimits;
	this.employees = employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public int[] getMinimumSeatLimits() {
        return minimumSeatLimits;
    }

    public int[] getMaximumSeatLimits() {
        return maximumSeatLimits;
    }    
}
