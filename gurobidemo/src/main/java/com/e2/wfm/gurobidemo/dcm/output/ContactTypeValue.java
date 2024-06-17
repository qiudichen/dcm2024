package com.e2.wfm.gurobidemo.dcm.output;

public class ContactTypeValue extends Value {
    private final double[] underValues;
    private final double[] overValues;
    private final double[] dualValues;
    public ContactTypeValue(long id, double[] underValues, double[] overValues, double[] dualValues) {
	super(id);
	this.underValues = underValues;
	this.overValues = overValues;
	this.dualValues = dualValues;
    }
    
    public double[] getUnderValues() {
        return underValues;
    }
    
    public double[] getOverValues() {
        return overValues;
    }
    
    public double[] getDualValues() {
	return dualValues;
    }
}
