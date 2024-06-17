package com.e2.wfm.gurobidemo.dcm.data;

import com.e2.wfm.gurobidemo.dcm.input.DCMModelInput;
import com.e2.wfm.gurobidemo.dcm.input.Employee;
import com.e2.wfm.gurobidemo.dcm.input.EmployeeSchedule;
import com.e2.wfm.gurobidemo.dcm.output.DCMModelOutput;

import java.util.List;

public abstract class ModelInputConstant {
    protected int dailyInterval = 96;
    protected int empNumPerSkillGroup = 20;
    
    protected abstract int getNumOfDays();
    
    public int getInterval() {
	return dailyInterval * getNumOfDays();
    }
    
    public abstract DCMModelInput createModelInput(int maxIteration);
    
    public abstract List<EmployeeSchedule> getBeseSchedules(DCMModelOutput modelOutput, List<Employee> employees);
}
