package com.e2.wfm.gurobidemo.dcm.input;

import java.util.ArrayList;
import java.util.List;

public class SkillGroup extends Entity {
    private List<Employee> employees;
    private List<ContactType> contactTypes;

    public SkillGroup(long id, List<ContactType> contactTypes) {
	super(id);
	this.contactTypes = contactTypes;
	employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<ContactType> getContactTypes() {
        return contactTypes;
    }
}
