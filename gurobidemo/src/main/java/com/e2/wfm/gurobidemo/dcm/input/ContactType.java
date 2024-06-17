package com.e2.wfm.gurobidemo.dcm.input;

public class ContactType extends Entity {
    private final double[] requirements;
    public ContactType(long id, double[] requirements) {
	super(id);
	this.requirements = requirements;
    }
    
    public double[] getRequirements() {
        return requirements;
    }
}
