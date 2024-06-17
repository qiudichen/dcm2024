package com.e2.wfm.gurobidemo.dcm.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCMModelInput {
    private final List<SkillGroup> skillGroups;
    private final List<SchedulingUnit> schedulingUnits;
    private final List<ContactType> contactTypes;
    private final List<Employee> employees;
    
    private final int interval;
    
    private double underCoeff = 10.0;
    private double maxUnderCoeff = 200.0;
    private double overCoeff = 10.0;
    private double maxOverCoeff = 200.0;
    private double lowerCoeff = 2000.0;
    private double upperCoeff = 2000.0;
    private final Map<Long, List<Long>> contactToSkillGroup;
    
    public DCMModelInput(List<SkillGroup> skillGroups, List<SchedulingUnit> schedulingUnits,
	    List<ContactType> contactTypes, List<Employee> employees) {
	super();
	this.skillGroups = skillGroups;
	this.schedulingUnits = schedulingUnits;
	this.contactTypes = contactTypes;
	this.employees = employees;
	this.interval = contactTypes.get(0).getRequirements().length;
	
	this.contactToSkillGroup = new HashMap<>();
	for(SkillGroup sg : skillGroups) {
	    sg.getContactTypes().forEach(contactType ->{
		List<Long> sgList = contactToSkillGroup.computeIfAbsent(contactType.getId(), f -> new ArrayList<>());
		sgList.add(sg.getId());
	    });
	}
    }

    public List<SkillGroup> getSkillGroups() {
        return skillGroups;
    }

    public List<SchedulingUnit> getSchedulingUnits() {
        return schedulingUnits;
    }

    public List<ContactType> getContactTypes() {
        return contactTypes;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    
    public int getInterval() {
	return interval;
    }

    public double getUnderCoeff() {
        return underCoeff;
    }

    public double getOverCoeff() {
        return overCoeff;
    }
    
    public double getMaxUnderCoeff() {
	return maxUnderCoeff;
    }
    
    public double getMaxOverCoeff() {
	return maxOverCoeff;
    }
    
    public double getLowerCoeff() {
	return lowerCoeff;
    }
    
    public double getUpperCoeff() {
	return upperCoeff;
    }
    
    public List<Long> getSkillGroupIds(Long contactTypeId) {
	return this.contactToSkillGroup.get(contactTypeId);
    }
}
