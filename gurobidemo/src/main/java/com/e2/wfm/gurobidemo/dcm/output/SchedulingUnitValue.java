package com.e2.wfm.gurobidemo.dcm.output;

public class SchedulingUnitValue extends Value {
    private double[] dualValues;
    private double[] lowerSlackValues;
    private double[] upperSlackValues;
    
    public SchedulingUnitValue(long id, double[] dualValues, double[] lowerSlackValues, double[] upperSlackValues) {
	super(id);
	this.dualValues = dualValues;
	this.lowerSlackValues = lowerSlackValues;
	this.upperSlackValues = upperSlackValues;
    }

    public double[] getDualValues() {
        return dualValues;
    }

    public double[] getLowerSlackValues() {
        return lowerSlackValues;
    }

    public double[] getUpperSlackValues() {
        return upperSlackValues;
    }
}
