package com.e2.wfm.gurobidemo.dcm.data;

import com.e2.wfm.gurobidemo.dcm.input.ContactType;
import com.e2.wfm.gurobidemo.dcm.input.DCMModelInput;
import com.e2.wfm.gurobidemo.dcm.input.Employee;
import com.e2.wfm.gurobidemo.dcm.input.SchedulingUnit;
import com.e2.wfm.gurobidemo.dcm.input.SkillGroup;

import java.util.ArrayList;
import java.util.List;

public class ModelInputHelper {

    public static DCMModelInput createModelInput() {
	int interval = 96;
	// 96 intervals per day
	// 0~3, 4~7, 8~11, 12~15, 16~19, 20~23, 24~27, 28~31, 32~35, 36~39, 40~43, 44~47, 48~51, 52~55, 56~59, 60~63, 64~67, 68~71, 72~75, 76~79, 80~83, 84~87, 88~91, 92~95
	// 0    1    2     3      4      5      6      7      8      9      10     11     12     13     14     15     16     17     18     19     20     21     22     23	
	
	List<ContactType> contactTypes = createContactTypes(interval);
	List<SkillGroup> skillGroups = createSkillGroups(contactTypes);
	List<Employee> employees = createEmployees(skillGroups, interval);
	List<SchedulingUnit> schedulingUnits = createSchedulingUnits(interval, employees);
	return new DCMModelInput(skillGroups, schedulingUnits, contactTypes, employees);
    }
    
    private static List<ContactType> createContactTypes(int interval) {
	List<ContactType> contactTypes = new ArrayList<>();
	double[] requirements = new double[interval];
	for (int i = 0; i < interval; i++) {
	    requirements[i] = 0.0;
	}
	contactTypes.add(new ContactType(0, requirements));
	
	requirements = new double[interval];
	for (int i = 0; i < interval; i++) {
	    requirements[i] = 0.0;
	}
	contactTypes.add(new ContactType(1, requirements));
	
	requirements = new double[interval];
	for (int i = 0; i < interval; i++) {
	    requirements[i] = 0.0;
	}
	contactTypes.add(new ContactType(2, requirements));
	return contactTypes;
    }
    
    private static List<SchedulingUnit> createSchedulingUnits(int interval, List<Employee> employees) {
	List<SchedulingUnit> schedulingUnits = new ArrayList<>();
	int[] minimumSeatLimits = new int[interval];
	int[] maximumSeatLimits = new int[interval];
	for (int i = 0; i < interval; i++) {
	    minimumSeatLimits[i] = 0;
	    maximumSeatLimits[i] = 0;
	}
	schedulingUnits.add(new SchedulingUnit(0, minimumSeatLimits, maximumSeatLimits, employees));
	return schedulingUnits;
    }
    
    private static List<SkillGroup> createSkillGroups(List<ContactType> contactTypes) {
	List<SkillGroup> skillGroups = new ArrayList<>();
	for (int i = 0; i < 5; i++) {
	    //0 - 0,1
	    //1 - 1,2
	    //2 - 0,2
	    //3 - 0,
	    //4 - 0,1,2
	    List<ContactType> cts = null;
	    switch(i) {
	    case 0:
		cts = contactTypes.stream()
		.filter(contactType -> contactType.getId() == 0 || contactType.getId() == 1).toList();
		break;
	    case 1:
		cts = contactTypes.stream().filter(contactType -> contactType.getId() == 1 || contactType.getId() == 2).toList();
		break;
	    case 2:
		cts = contactTypes.stream().filter(contactType -> contactType.getId() == 0 || contactType.getId() == 2).toList();
		break;
	    case 3:
		cts = contactTypes.stream().filter(contactType -> contactType.getId() == 0).toList();
		break;
	    case 4:
		cts = contactTypes;
		break;
		default:
		    cts = new ArrayList<>();
	    }
	    skillGroups.add(new SkillGroup(i, cts));
	}
	return skillGroups;
    }
    
    private static List<Employee> createEmployees(List<SkillGroup> skillGroups, int interval) {
	List<Employee> employees = new ArrayList<>();
	for (SkillGroup skillGroup : skillGroups) {
	    long groupId = skillGroup.getId();
	    for (int i = 0; i < 10; i++) {
		skillGroup.addEmployee(new Employee(i + 10 * (int)groupId, groupId, 0, createSchedule(groupId, interval)));
	    }
	    employees.addAll(skillGroup.getEmployees());
	}
	return employees;
    }
    
    private static int[] createSchedule(long groupId, int interval) {
	int[] schedules = new int[interval];
	// 0~3, 4~7, 8~11, 12~15, 16~19, 20~23, 24~27, 28~31, 32~35, 36~39, 40~43,
	// 44~47, 48~51, 52~55, 56~59, 60~63, 64~67, 68~71, 72~75, 76~79, 80~83, 84~87,
	// 88~91, 92~95
	// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
	switch ((int) groupId) {
	case 0:
	    for (int i = 28; i < 67; i++) {
		schedules[i] = 1;
	    }
	    // break1
	    schedules[36] = 0;
	    // lunch
	    schedules[44] = 0;
	    schedules[45] = 0;
	    schedules[46] = 0;
	    schedules[47] = 0;
	    // break2
	    schedules[56] = 0;
	    break;
	case 1:
	    for (int i = 29; i < 68; i++) {
		schedules[i] = 1;
	    }
	    // break1
	    schedules[37] = 0;
	    // lunch
	    schedules[45] = 0;
	    schedules[46] = 0;
	    schedules[47] = 0;
	    schedules[48] = 0;
	    // break2
	    schedules[57] = 0;
	    break;	    
	    
	case 2:
	    for (int i = 30; i < 69; i++) {
		schedules[i] = 1;
	    }
	    // break1
	    schedules[38] = 0;
	    // lunch
	    schedules[46] = 0;
	    schedules[47] = 0;
	    schedules[48] = 0;
	    schedules[49] = 0;
	    // break2
	    schedules[58] = 0;
	    break;	    
	    
	case 3:
	    for (int i = 31; i < 70; i++) {
		schedules[i] = 1;
	    }
	    // break1
	    schedules[39] = 0;
	    // lunch
	    schedules[47] = 0;
	    schedules[48] = 0;
	    schedules[49] = 0;
	    schedules[50] = 0;
	    // break2
	    schedules[59] = 0;
	    break;	    
	    
	case 4:
	default:    
	    for (int i = 32; i < 71; i++) {
		schedules[i] = 1;
	    }
	    // break1
	    schedules[40] = 0;
	    // lunch
	    schedules[48] = 0;
	    schedules[49] = 0;
	    schedules[50] = 0;
	    schedules[51] = 0;
	    // break2
	    schedules[60] = 0;
	    break;	    
	}

	return schedules;
    }
}
