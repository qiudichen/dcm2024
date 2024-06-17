package com.e2.wfm.gurobidemo.dcm.data;

import com.e2.wfm.gurobidemo.dcm.input.ContactType;
import com.e2.wfm.gurobidemo.dcm.input.DCMModelInput;
import com.e2.wfm.gurobidemo.dcm.input.Employee;
import com.e2.wfm.gurobidemo.dcm.input.EmployeeSchedule;
import com.e2.wfm.gurobidemo.dcm.input.SchedulingUnit;
import com.e2.wfm.gurobidemo.dcm.input.SkillGroup;
import com.e2.wfm.gurobidemo.dcm.output.DCMModelOutput;
import com.e2.wfm.gurobidemo.dcm.output.SkillGroupValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to create a model input with a single day and two contact
 * types.
 * 96 intervals per day
 * index: 0~3, 4~7, 8~11, 12~15, 16~19, 20~23, 24~27, 28~31, 32~35, 36~39, 40~43, 44~47, 48~51, 52~55, 56~59, 60~63, 64~67, 68~71, 72~75, 76~79, 80~83, 84~87, 88~91, 92~95
 * hour:  0    1    2     3      4      5      6      7      8      9      10     11     12     13     14     15     16     17     18     19     20     21     22     23	
 * ContactType Hours: 28~75 (7:00~19:00)
 * SkillGroup 0: 0~20
 * SkillGroup 1: 1~20
 * SkillGroup 2: 0,1~20
 * Daily Rule 1: start from 7:00(28), 7:15(29), 7:30(30), 7:45(31), 8:00(32)
 * 		 break1:6(1h30), 7(1h45), 8(2h), 9(2h15)
 *          	 lunch: 16(4h）, 17(4h15), 18(4h30), 19(4h45), 20(5h)
 *          	 break2: 28(7h), 29(7h15), 30(7h30), 31(7h15)
 *          	 end at 17:00(68), 17:15(69), 17:30(70), 17:45(71), 18:00(72)
 *          
 * Daily Rule 2: start from 8:00(32), 8:15(33), 8:30(34), 8:45(35), 9:00(36)
 * 		 break1:6(1h30), 7(1h45), 8(2h), 9(2h15)
 *  		 lunch: 16(4h）, 17(4h15), 18(4h30), 19(4h45), 20(5h)
 *          	 break2: 28(7h), 29(7h15), 30(7h30), 31(7h15)
 *          	 end at 18:00(72), 18:15(73), 18:30(74), 18:45(75), 19:00(76)
 */
public class ModelInputSingleDayTwoContactType extends ModelInputConstant {
    double requriement0 = 22.0;
    double requriement1 = 22.0;
    
    private int[] shiftStarts0 = {28,29,30,31,32};
    private int[] shiftStarts1 = {32,33,34,35,36};
    
    private int shiftLenRule = 40;
    private int lunchLenRule = 4;
    private int breakLenRule = 1;

    private int[] break1Starts = {6,7,8,9};
    private int[] lunchStarts = {16,17,18,19,20};
    private int[] break2Starts = {28,29,30,31};
    
    private int[][] schedulesRule0;
    private int[][] schedulesRule1;
    private Map<Long, Map<Short, Selection>> selections = new HashMap<>();
    private int iteration = 0;
    
    private Selection getIndexOfRule(long groupId, short rule) {
	Selection selection = selections.get(groupId).get(rule);
	return selection;
    }
    
    protected int getNumOfDays() {
	return 1;
    }

    public DCMModelInput createModelInput(int maxIteration) {
	int interval = getInterval();
	// 96 intervals per day
	// 0~3, 4~7, 8~11, 12~15, 16~19, 20~23, 24~27, 28~31, 32~35, 36~39, 40~43, 44~47, 48~51, 52~55, 56~59, 60~63, 64~67, 68~71, 72~75, 76~79, 80~83, 84~87, 88~91, 92~95
	// 0    1    2     3      4      5      6      7      8      9      10     11     12     13     14     15     16     17     18     19     20     21     22     23	
	initSchedule();
	List<ContactType> contactTypes = createContactTypes(interval);
	List<SkillGroup> skillGroups = createSkillGroups(contactTypes);
	
	List<Employee> employees = createEmployees(skillGroups, maxIteration);
	List<SchedulingUnit> schedulingUnits = createSchedulingUnits(interval, employees);
	return new DCMModelInput(skillGroups, schedulingUnits, contactTypes, employees);
    }
    
    private List<ContactType> createContactTypes(int interval) {
	double[] requirements1 = new double[interval];
	for (int i = 28; i < 76; i++) {
	    requirements1[i] = requriement0;
	}
	
	double[] requirements2 = new double[interval];
	for (int i = 28; i < 76; i++) {
	    requirements2[i] = requriement1;
	}
	return List.of(new ContactType(0, requirements1), new ContactType(1, requirements2));
    }

    private List<SkillGroup> createSkillGroups(List<ContactType> contactTypes) {
	List<SkillGroup> skillGroups = new ArrayList<>();
	for (int i = 0; i < 3; i++) {
	    //0 - 0 ~ 20
	    //1 - 1 ~ 20
	    //2 - 0,1 ~ 20
	    List<ContactType> cts = null;
	    switch(i) {
	    case 0:
		cts = contactTypes.stream()
		.filter(contactType -> contactType.getId() == 0).toList();
		break;
	    case 1:
		cts = contactTypes.stream()
		.filter(contactType -> contactType.getId() == 1).toList();
		break;
	    case 2:
		cts = contactTypes;
		break;
	    default:
		    cts = new ArrayList<>();
	    }
	    skillGroups.add(new SkillGroup(i, cts));
	}
	return skillGroups;
    }
    
    private List<SchedulingUnit> createSchedulingUnits(int interval, List<Employee> employees) {
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


    
    private List<Employee> createEmployees(List<SkillGroup> skillGroups, int maxIteration) {
	List<Employee> employees = new ArrayList<>();
	for (SkillGroup skillGroup : skillGroups) {
	    long groupId = skillGroup.getId();
	    Map<Short, Selection> groupSelections = selections.computeIfAbsent(groupId, f -> new HashMap<>());
	    for (int i = 0; i < empNumPerSkillGroup; i++) {
		long empId = empNumPerSkillGroup * groupId + i;
		short dailyRule = (short) (empId % 2);	
		Selection selection = groupSelections.computeIfAbsent(dailyRule, f -> new Selection(groupId, dailyRule, selectSchedule(dailyRule), 0, maxIteration));
		int[] schedule = selection.getSchedule(0);
		skillGroup.addEmployee(new Employee(empId, groupId, 0, schedule));
	    }
	    employees.addAll(skillGroup.getEmployees());
	}
	return employees;
    }
    
    private int[][] selectSchedule(short dailyRule) {
	switch(dailyRule) {
	case 0:
	    return schedulesRule0;
	case 1:
	    return schedulesRule1;
	default:
	    return new int[0][];
	}
    }
    
    public List<EmployeeSchedule> getBeseSchedules(DCMModelOutput modelOutput, List<Employee> employees) {
	iteration++;
	
	SkillGroupValue[] skillGroupValues = modelOutput.getGroupValues();
	List<EmployeeSchedule> employeeSchedules = new ArrayList<>();
	
	for(Employee employee : employees) {
	    long empId = employee.getId();
	    short ruleId = (short) (empId % 2);
	    long groupId = employee.getSkillGroupId();
	    
	    Selection selection = getIndexOfRule(groupId, (short) ruleId);
	    int index = selection.getIndex(iteration);
	    
	    int[] schedule = null;
	    if(index > 0) {
		schedule = selection.getSchedule(index);
	    } else {
		SkillGroupValue skillGroupValue = skillGroupValues[(int) groupId];
		double[] dualValues = skillGroupValue.getDualValues();
		index = getBestSchedule(dualValues, selection);
		selection.addIndex(iteration, index);
		schedule = selection.getSchedule(index);
	    }
	    
	    if(schedule.length > 0 ) {
		employeeSchedules.add(new EmployeeSchedule(empId, groupId, schedule));
	    }
	}
	return employeeSchedules;
    }
    
    private int getBestSchedule(double[] dualValues, Selection selection) {
	int[][] schedules = selection.getSchedules();
	ScoreIndex scoreIndex = new ScoreIndex();
	
	for (int i = 0; i < schedules.length; i++) {
	    if (!selection.isSelected(i)) {
		double score = score(dualValues, schedules[i]);
		scoreIndex.setScore(score, i);
	    }
	}
	return scoreIndex.getIndex();
    }
    
    static class ScoreIndex {
	double score = Integer.MIN_VALUE;
	Set<Integer> indexes = new HashSet<>();
	
	public void addIndex(int index) {
	    indexes.add(index);
	}
	
	public void setScore(double score, int index) {
	    if (score > this.score) {
		this.score = score;
		indexes.clear();
		indexes.add(index);
	    } else if (score == this.score) {
		indexes.add(index);
	    }
	}
	
	public int getIndex() {
	    if(indexes.isEmpty()) {
		return -1;
	    }
	    return indexes.iterator().next();
        }
    }
    
    private double score(double[] dualValues, int[] schedule) {
	double score = 0;
	for (int i = 0; i < schedule.length; i++) {
	    score += (double)(dualValues[i] * schedule[i]);
	}
	return score;
    }

    private void initSchedule() {
	int interval = getInterval();
	schedulesRule0 = ruleSchedule(interval, shiftStarts0, break1Starts, lunchStarts, break2Starts);
	schedulesRule1 = ruleSchedule(interval, shiftStarts1, break1Starts, lunchStarts, break2Starts);
    }
    
    private int[][] ruleSchedule(int interval, int[] shiftStarts, int[] break1Starts, int[] lunchStarts, int[] break2Starts) {
	int size = shiftStarts.length * break1Starts.length * lunchStarts.length * break2Starts.length;
	int[][] schedules = new int[(int) size][interval];
	int idx = 0;
	for(int shiftStart : shiftStarts) {
	    for (int break1Start : break1Starts) {
		for (int lunchStart : lunchStarts) {
		    for (int break2Start : break2Starts) {
			schedules[idx++] = ruleSchedule(interval, shiftStart, shiftLenRule, breakLenRule, lunchLenRule, break1Start, break2Start, lunchStart);
		    }
		}
	    }
	}
	return schedules;
    }
    
    private int[] ruleSchedule(int interval, int startIdx, 
	    	int shiftLen, int breakLen, int lunchLen, 
	    	int break1Start, int break2Start, int lunchStart) {
	int[] schedules = new int[interval];
	//set schedule
	int start = startIdx;
	int end = startIdx + shiftLen;
	setSlotValue(schedules, start, end, 1);
	
	//set break1
	start = startIdx + break1Start;
	end = start + breakLen; 
	setSlotValue(schedules, start, end, -1);
	
	//set lunch
	start = startIdx + lunchStart;
	end = start + lunchLen; 
	setSlotValue(schedules, start, end, -1);
	
	//set break2
	start = startIdx + break2Start;
	end = start + breakLen; 
	setSlotValue(schedules, start, end, -1);
	
	return schedules;
    }
    
    private void setSlotValue(int[] schedules, int start, int end, int value) {
	for (int i = start; i < end; i++) {
	    schedules[i] = value;
	}
    }
    
    static class Selection {
	long groupId;
	short dailyRule;
	int[][] schedules;
	int[] indexes;
	Set<Integer> selected = new HashSet<>();
	
	public Selection(long groupId, short dailyRule, int[][] schedules, int index, int maxIteration) {
	    super();
	    this.groupId = groupId;
	    this.dailyRule = dailyRule;
	    this.indexes = new int[maxIteration];
	    this.schedules = schedules;
	    addIndex(0, index);
	}
	
	public void addIndex(int iteration, int index) {
	    indexes[iteration] = index;
	    selected.add(index);
	}
	
	public int getIndex(int iteration) {
	    return indexes[iteration];
	}
	
	public int[][] getSchedules() {
	    return schedules;
	}
	
	public int[] getSchedule(int index) {
	    if(index < 0) {
		return new int[0];
	    }
	    return schedules[index];
	}
	
	public boolean isSelected(int index) {
	    return selected.contains(index);
	}
    }
}
