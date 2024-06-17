package com.e2.wfm.gurobidemo.dcm.output;

import java.util.Map;

public class EmployeeValue extends Value {
    private final long groupId;
    private final double dualValue;
    private final long schedulingUnitId;
    private final Map<Integer, Double> scheduleValues;
    
    public EmployeeValue(long id, long groupId, long schedulingUnitId, Map<Integer, Double> scheduleValues, double dualValue) {
	super(id);
	this.groupId = groupId;
	this.dualValue = dualValue;
	this.schedulingUnitId = schedulingUnitId;
	this.scheduleValues = scheduleValues;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getSchedulingUnitId() {
        return schedulingUnitId;
    }

    public double getDualValue() {
	return dualValue;
    }
    
    public Map<Integer, Double> getScheduleValues() {
        return scheduleValues;
    }
}
