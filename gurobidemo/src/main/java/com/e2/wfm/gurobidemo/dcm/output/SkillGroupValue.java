package com.e2.wfm.gurobidemo.dcm.output;

import java.util.Map;

public class SkillGroupValue extends Value {
    private double[] dualValues;
    private Map<Long, double[]> varValues;
    
    public SkillGroupValue(long id, double[] dualValues, Map<Long, double[]> varValues) {
	super(id);
	this.dualValues = dualValues;
	this.varValues = varValues;
    }

    public double[] getDualValues() {
        return dualValues;
    }

    public Map<Long, double[]> getVarValues() {
        return varValues;
    }
}
