package com.e2.wfm.gurobidemo.dcm.input;

public class EmployeeSchedule extends Entity implements ScheduleCandidate {
    private final int[] schedules;
    private final long skillGroupId;
    
    public EmployeeSchedule(long id, long skillGroupId, int[] schedules) {
	super(id);
	this.schedules = schedules;
	this.skillGroupId = skillGroupId;
    }

    public long getSkillGroupId() {
        return skillGroupId;
    }
    
    public int[] getSchedules() {
        return schedules;
    }
}
