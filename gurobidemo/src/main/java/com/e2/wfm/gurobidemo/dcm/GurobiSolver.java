package com.e2.wfm.gurobidemo.dcm;

import static com.gurobi.gurobi.GRB.MINIMIZE;

import com.e2.wfm.gurobidemo.dcm.constraint.ContactTypeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.EmployeeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.MaxContactTypeConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.ScheduleCandidate;
import com.e2.wfm.gurobidemo.dcm.constraint.SchedulingUnitConstraint;
import com.e2.wfm.gurobidemo.dcm.constraint.SkillGroupConstraint;
import com.e2.wfm.gurobidemo.dcm.input.ContactType;
import com.e2.wfm.gurobidemo.dcm.input.DCMModelInput;
import com.e2.wfm.gurobidemo.dcm.input.Employee;
import com.e2.wfm.gurobidemo.dcm.input.EmployeeSchedule;
import com.e2.wfm.gurobidemo.dcm.input.SchedulingUnit;
import com.e2.wfm.gurobidemo.dcm.input.SkillGroup;
import com.e2.wfm.gurobidemo.dcm.output.ContactTypeValue;
import com.e2.wfm.gurobidemo.dcm.output.DCMModelOutput;
import com.e2.wfm.gurobidemo.dcm.output.EmployeeValue;
import com.e2.wfm.gurobidemo.dcm.output.SchedulingUnitValue;
import com.e2.wfm.gurobidemo.dcm.output.SkillGroupValue;
import com.gurobi.gurobi.GRB;
import com.gurobi.gurobi.GRBConstr;
import com.gurobi.gurobi.GRBException;
import com.gurobi.gurobi.GRBLinExpr;
import com.gurobi.gurobi.GRBModel;
import com.gurobi.gurobi.GRBVar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GurobiSolver {
    private DCMModelInput dcmModel;
    private GRBModel model;
    private Register register;

    public GurobiSolver(DCMModelInput dcmModel, GRBModel model) throws GRBException {
	super();
	this.dcmModel = dcmModel;
	this.register = new Register(model);
	// Set objective
	model.set(GRB.IntAttr.ModelSense, MINIMIZE);
	this.model = model;
    }

    public void initModel() throws GRBException {
	addSkillGroupConstraints();
	addContactTypeConstraints();
	addSeatLimitConstraints();
	addInitEmployeeConstraints();
	updateScheduleVariables();
	addMaxVariableas();
    }	

    public int solve() throws GRBException {
	model.write("model.lp");
	model.write("model.rlp");
	model.write("model.mps");
	model.write("model.rew");
	model.optimize();
	return model.get(GRB.IntAttr.Status);
    }    
    
    public DCMModelOutput getValues() throws GRBException {
	SkillGroupValue[] groupValues = getSkillGroupValues();
	SchedulingUnitValue[] schedulingUnitValues = getSchedulingUnitValues();
	EmployeeValue[] employeeValues = getEmployeeValues();
	ContactTypeValue[] contactTypeValues = getContactTypeValues();
	double objectiveValue = getObjectiveValue();
	return new DCMModelOutput(objectiveValue, groupValues, schedulingUnitValues, employeeValues, contactTypeValues);
    }
    
    protected ContactTypeValue[] getContactTypeValues() throws GRBException {
	ContactTypeValue[] contactTypeValue = new ContactTypeValue[dcmModel.getContactTypes().size()];
	ContactTypeConstraint[] constraints = this.register.getContactTypeConstraints();
	for (int i = 0; i < contactTypeValue.length; i++) {
	    contactTypeValue[i] = getContactTypeValue(constraints[i]);
	}
	return contactTypeValue;
    }
    
    private ContactTypeValue getContactTypeValue(ContactTypeConstraint contactTypeConstraint) throws GRBException {
	double[] underValues = new double[contactTypeConstraint.getUnders().length];
	double[] overValues = new double[contactTypeConstraint.getOvers().length];
	double[] dualValues = new double[contactTypeConstraint.getConstrs().length];
	for (int i = 0; i < dcmModel.getInterval(); i++) {
	    underValues[i] = this.getVariableValue(contactTypeConstraint.getUnders()[i]);
	    overValues[i] = this.getVariableValue(contactTypeConstraint.getOvers()[i]);
	    dualValues[i] = getDualValue(contactTypeConstraint.getConstrs()[i]);
	}
	return new ContactTypeValue(contactTypeConstraint.getId(), underValues, overValues, dualValues);
    }

    protected EmployeeValue[] getEmployeeValues() throws GRBException {
	EmployeeValue[] employeeValues = new EmployeeValue[dcmModel.getEmployees().size()];
	EmployeeConstraint[] employeeConstraints = this.register.getEmployeeConstraints();
	for (int i = 0; i < employeeValues.length; i++) {
	    employeeValues[i] = getEmployeeValue(employeeConstraints[i]);
	}
	return employeeValues;
    }
    
    private EmployeeValue getEmployeeValue(EmployeeConstraint employeeConstraint) throws GRBException {
	Map<Integer, Double> scheduleValues = new HashMap<>();
	double dualValue = this.getDualValue(employeeConstraint.getConstr());
	
	for(Entry<Integer, ScheduleCandidate> entry : employeeConstraint.getScheduleVars().entrySet()) {
	    entry.getValue().getScheduleVar();
	    double value = this.getVariableValue(entry.getValue().getScheduleVar());
	    scheduleValues.put(entry.getKey(), value);
	}		
	return new EmployeeValue(employeeConstraint.getId(), employeeConstraint.getGroupId(), employeeConstraint.getSchedulingUnitId(), scheduleValues, dualValue);
    }

    protected SchedulingUnitValue[] getSchedulingUnitValues() throws GRBException {
	SchedulingUnitValue[] unitValues = new SchedulingUnitValue[dcmModel.getSchedulingUnits().size()];
	SchedulingUnitConstraint[] constraints = this.register.getSchedulingUnitConstraints(); 
	for (int i = 0; i < constraints.length; i++) {
	    unitValues[i] = getSchedulingUnitValue(constraints[i]);
	}
	return unitValues;
    }
    
    protected SchedulingUnitValue getSchedulingUnitValue(SchedulingUnitConstraint schedulingUnitConstraint) throws GRBException {
	double[] dualValues = new double[schedulingUnitConstraint.getConstrs().length];
	double[] lowerSlackValues = new double[schedulingUnitConstraint.getLowerSlacks().length];
	double[] upperSlackValues = new double[schedulingUnitConstraint.getUpperSlacks().length];
	for (int i = 0; i < dcmModel.getInterval(); i++) {
	    dualValues[i] = getDualValue(schedulingUnitConstraint.getConstr(i));
	    lowerSlackValues[i] = getVariableValue(schedulingUnitConstraint.getLowerSlacks()[i]);
	    upperSlackValues[i] = getVariableValue(schedulingUnitConstraint.getUpperSlacks()[i]);
	}
	return new SchedulingUnitValue(schedulingUnitConstraint.getId(), dualValues, lowerSlackValues, upperSlackValues);
    }
    
    protected SkillGroupValue[] getSkillGroupValues() throws GRBException {
	SkillGroupConstraint[] groupConstraints = this.register.getSkillGroupConstraints();
	SkillGroupValue[] groupValues = new SkillGroupValue[groupConstraints.length];
	for(int i = 0; i < groupConstraints.length; i++) {
	    groupValues[i] = getSkillGroupValue(groupConstraints[i]);
	}
	return groupValues;
    }
    
    protected SkillGroupValue getSkillGroupValue(SkillGroupConstraint skillGroupConstraint) throws GRBException {
	double[] dualValues = new double[dcmModel.getInterval()];
	Map<Long, double[]> varValues = new HashMap<>();
	
	for (int i = 0; i < dcmModel.getInterval(); i++) {
	    GRBConstr constr = skillGroupConstraint.getConstr(i);
	    dualValues[i] = getDualValue(constr);

	    Map<Long, GRBVar[]> variables = skillGroupConstraint.getVariables();
	    for(Entry<Long, GRBVar[]> entry : variables.entrySet()) {
		Long contactTypeId = entry.getKey();
		GRBVar[] vars = entry.getValue();
		double[] values = varValues.computeIfAbsent(contactTypeId, f -> new double[vars.length]);
		for(int j = 0; j < vars.length; j++) {
		    values[j] = getVariableValue(vars[j]);
		}
	    }
	}
	return new SkillGroupValue(skillGroupConstraint.getId(), dualValues, varValues);
    }
    
    protected void addSeatLimitConstraints() throws GRBException {
	SchedulingUnitConstraint[] constraints = new SchedulingUnitConstraint[dcmModel.getSchedulingUnits().size()];
	for(SchedulingUnit schedulingUnit : dcmModel.getSchedulingUnits()) {
	    constraints[(int)schedulingUnit.getId()] = addSeatLimitConstraint(schedulingUnit);
	}
	this.register.setSchedulingUnitConstraints(constraints);
    }
    
    private SchedulingUnitConstraint addSeatLimitConstraint(SchedulingUnit schedulingUnit) throws GRBException {
	int interval = dcmModel.getInterval();
	int[] minSeatLimits = schedulingUnit.getMinimumSeatLimits();
	int[] maxSeatLimits = schedulingUnit.getMaximumSeatLimits();
	//LSli- USli+∑_a∑_jSaj=[MinSeatLimit si,MaxSeatLimit si]
	long schedulingUnitId = schedulingUnit.getId();
	GRBConstr[] constrs = new GRBConstr[interval];
	GRBVar[] lowerSlacks = new GRBVar[interval];
	GRBVar[] upperSlacks = new GRBVar[interval];
	
	SchedulingUnitConstraint constraint = new SchedulingUnitConstraint(schedulingUnitId, constrs, lowerSlacks, upperSlacks);
	
	for(int i = 0; i < interval; i++) {
	    if(minSeatLimits[i] == 0 && maxSeatLimits[i] == 0) {
		continue;
	    }
            GRBLinExpr expr = new GRBLinExpr();
            lowerSlacks[i] = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getLowerCoeff(), 
        	    GRB.CONTINUOUS, "ls_l" + schedulingUnitId + "_i" + i);
            expr.addTerm(1.0, lowerSlacks[i]);
            upperSlacks[i] = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getUpperCoeff(),
        	    GRB.CONTINUOUS, "us_l" + schedulingUnitId + "_i" + i);
            expr.addTerm(-1.0, upperSlacks[i]);
            constrs[i] = model.addRange(expr, minSeatLimits[i], maxSeatLimits[i], 
        	    "sl_l" + schedulingUnitId + "_i" + i);
        }
	return constraint;
    }
    
    protected void addSkillGroupConstraints() throws GRBException {
	SkillGroupConstraint[] constraints = new SkillGroupConstraint[dcmModel.getSkillGroups().size()];
	for(SkillGroup skillGroup : this.dcmModel.getSkillGroups()) {
	    constraints[(int)skillGroup.getId()] = addSkillGroupConstraints(skillGroup);
	}
	this.register.setGroupConstraints(constraints);
    }
    
    private SkillGroupConstraint addSkillGroupConstraints(SkillGroup skillGroup) throws GRBException {
	int interval = dcmModel.getInterval();
	GRBConstr[] constrs = new GRBConstr[interval];
	long groupId = skillGroup.getId();
	SkillGroupConstraint constraint = new SkillGroupConstraint(groupId, constrs);
	//-∑_tYgti+∑_a∑_jSaj= 0
	for (int i = 0; i < interval; i++) {
	    GRBLinExpr expr = new GRBLinExpr();
	    for(ContactType contactType : skillGroup.getContactTypes()) {
		long contactTypeId = contactType.getId();
		GRBVar ygti = model.addVar(0.0, GRB.INFINITY, 0.0d, GRB.CONTINUOUS, 
			"y_g" + groupId + "_t" + contactTypeId + "_i" + i);
		expr.addTerm(-1.0, ygti);
		constraint.addVariable(ygti, contactTypeId, i);
	    }
	    constrs[i] = model.addConstr(expr, GRB.EQUAL, 0.0, "fte_g" + groupId + "_i" + i);
	}
	return constraint;
    }
    
    protected void addContactTypeConstraints() throws GRBException {
	ContactTypeConstraint[] constraints = new ContactTypeConstraint[dcmModel.getContactTypes().size()];
	for (ContactType contactType : this.dcmModel.getContactTypes()) {
	    constraints[(int) contactType.getId()] = addContactTypeConstraints(contactType);
	}
	this.register.setCtConstraints(constraints);
    }
    
    private ContactTypeConstraint addContactTypeConstraints(ContactType contactType) throws GRBException {
	int interval = dcmModel.getInterval();
	GRBConstr[] constrs = new GRBConstr[interval];
	GRBVar[] unders = new GRBVar[interval];
	GRBVar[] overs = new GRBVar[interval];
	
	long contactTypeId = contactType.getId();
	ContactTypeConstraint constraint = new ContactTypeConstraint(contactTypeId, constrs, unders, overs);
	double[] requirements = contactType.getRequirements();
	//Uti- Oti+∑_g Ygti =Rti
	for (int i = 0; i < interval; i++) {
            GRBLinExpr expr = new GRBLinExpr();
            unders[i] = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getUnderCoeff(), 
        	    GRB.CONTINUOUS, "under_t" + contactTypeId + "_i" + i);
            expr.addTerm(1.0, unders[i]);
            overs[i] = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getOverCoeff(), 
        	    GRB.CONTINUOUS, "over_t" + contactTypeId + "_i" + i);
            expr.addTerm(-1.0, overs[i]);
            //add skill group variables
            List<Long> sgIds = this.dcmModel.getSkillGroupIds(contactTypeId);
            for(Long sgId : sgIds) {
        	GRBVar ygti = this.register.getSkillGroupVariables(sgId, contactTypeId, i);
        	expr.addTerm(1.0, ygti);
            }
            constrs[i] = model.addConstr(expr, GRB.EQUAL, requirements[i], "ndf_t" + contactTypeId + "_i" + i);
	}
	return constraint;
    }
    
    private void addMaxVariableas() throws GRBException {
	MaxContactTypeConstraint[] constraints = new MaxContactTypeConstraint[dcmModel.getContactTypes().size()];
	for (ContactType contactType : this.dcmModel.getContactTypes()) {
	    constraints[(int)contactType.getId()] = addMaxVariablea(contactType);
	}
	this.register.setMaxContactTypeConstraints(constraints);
    }
    
    private MaxContactTypeConstraint addMaxVariablea(ContactType contactType) throws GRBException {
	long contactTypeId = contactType.getId();
	ContactTypeConstraint contactTypeConstraint = this.register.getContactTypeConstraint(contactTypeId);
	
	GRBVar ctMaxUnder = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getMaxUnderCoeff(), 
    	    GRB.CONTINUOUS, "ctMaxUnder_t" + contactTypeId);
	GRBVar[] underVars = contactTypeConstraint.getUnders();	
	GRBConstr[] ctMaxUnderConstrs = new GRBConstr[underVars.length];
	
	for (int i = 0; i < underVars.length; i++) {
	    GRBLinExpr linExpr = new GRBLinExpr();
	    linExpr.addTerm(1, ctMaxUnder);
	    linExpr.addTerm(-1, underVars[i]);
	    ctMaxUnderConstrs[i] = model.addConstr(linExpr, GRB.GREATER_EQUAL, 0d, "ctMaxUnderConstr_t" + contactTypeId + "_i" + i);
	}

	GRBVar ctMaxOver = model.addVar(0.0, GRB.INFINITY, this.dcmModel.getMaxOverCoeff(), 
		GRB.CONTINUOUS, "ctMaxOver_t" + contactTypeId);
	GRBVar[] overVars = contactTypeConstraint.getOvers();
	GRBConstr[] ctMaxOverConstrs = new GRBConstr[overVars.length];
	for (int i = 0; i < overVars.length; i++) {
	    GRBLinExpr linExpr = new GRBLinExpr();
	    linExpr.addTerm(1, ctMaxOver);
	    linExpr.addTerm(-1, overVars[i]);
	    ctMaxOverConstrs[i] = model.addConstr(linExpr, GRB.GREATER_EQUAL, 0d, "ctMaxOverConstr_t" + contactTypeId + "_i" + i);
	}
	return new MaxContactTypeConstraint(contactTypeId, ctMaxUnder, ctMaxUnderConstrs, ctMaxOver, ctMaxOverConstrs);
    }

    public void nextIteration(List<EmployeeSchedule> schedules, int iteration) throws GRBException {
	EmployeeConstraint[] constraints = this.register.getEmployeeConstraints();
	for(EmployeeSchedule schedule : schedules) {
	    EmployeeConstraint employeeConstraint = constraints[(int)schedule.getId()];
	    addEmployeeSchedule(employeeConstraint, schedule, iteration);
	}
	updateScheduleVariables();
    }
    
    private void addEmployeeSchedule(EmployeeConstraint constraint, EmployeeSchedule schedule, int iteration) throws GRBException {
	long empId = constraint.getId();
	GRBVar scheduleVar = model.addVar(0.0, GRB.INFINITY, 0.0d, GRB.CONTINUOUS, "s_a" + empId + "_j" + iteration);
	this.model.chgCoeff(constraint.getConstr(), scheduleVar, 1.0);
	constraint.addIterationScheduleVar(scheduleVar, schedule.getSchedules(), iteration);
    }
    
    protected void addInitEmployeeConstraints() throws GRBException {
	EmployeeConstraint[] constraints = new EmployeeConstraint[dcmModel.getEmployees().size()];
	for (Employee employee : dcmModel.getEmployees()) {
	    constraints[(int)employee.getId()] = addInitEmployeeConstraint(employee);
	}
	this.register.setEmployeeConstraints(constraints);
    }
    
    private EmployeeConstraint addInitEmployeeConstraint(Employee employee) throws GRBException {
	
	long empId = employee.getId();
	//∑_jSaj=1  
	GRBVar schedule = model.addVar(0.0, GRB.INFINITY, 0.0d, GRB.CONTINUOUS, 
		"s_a" + empId + "_j" + 0);

	GRBLinExpr expr = new GRBLinExpr();
	expr.addTerm(1.0, schedule);
	GRBConstr constr = model.addConstr(expr, GRB.EQUAL, 1.0, "OneCandidatePer_a" + empId);
	
	return new EmployeeConstraint(empId, employee.getSkillGroupId(), employee.getSchedulingUnitId(), constr, schedule, employee.getSchedules());
    }

    public Register getRegister() {
	return register;
    }
    
    protected void updateScheduleVariables() throws GRBException {
	for(EmployeeConstraint employeeConstraint : this.register.getEmployeeConstraints()) {
	    updateEmployeeSchedule(employeeConstraint);
	}
    }

    private void updateEmployeeSchedule(EmployeeConstraint employeeConstraint) throws GRBException {
	if(!employeeConstraint.hasVariable()) {
	    return;
	}
	ScheduleCandidate scheduleVar = employeeConstraint.getScheduleVar();
	GRBVar schedule = scheduleVar.getScheduleVar();
	
	long skillGroupId = employeeConstraint.getGroupId();
	SkillGroupConstraint skillGroupConstraint = this.register.getSkillGroupConstraint(skillGroupId);
	SchedulingUnitConstraint schedulingUnitConstraint = this.register.getSchedulingUnitConstraint(employeeConstraint.getSchedulingUnitId());
	
	int[] scheduleIntervals = scheduleVar.getSchedules();
	for(int i = 0; i < scheduleIntervals.length; i++) {
	    if(scheduleIntervals[i] != 1) {
		continue;
	    }
	    //add skill group variables
	    GRBConstr gConstr = skillGroupConstraint.getConstr(i);
	    this.model.chgCoeff(gConstr, schedule, 1.0);
	    
	    //add scheduling unit variables
	    if(schedulingUnitConstraint != null) {
		GRBConstr sConstr = schedulingUnitConstraint.getConstr(i);
		if(sConstr != null) {
		    this.model.chgCoeff(sConstr, schedule, 1.0);
		}
	    }
	}
	employeeConstraint.updateScheduleVar();
    }
 
    protected double getObjectiveValue() throws GRBException {
	double objectValue = model.get(GRB.DoubleAttr.ObjVal);
	return objectValue;
    }
    
    protected double getVariableValue(GRBVar x) throws GRBException {
	if (x == null) {
	    return 0.0;
	}
	return x.get(GRB.DoubleAttr.X);
    }
    
    protected double getDualValue(GRBConstr constr) throws GRBException {
	if (constr == null) {
	    return 0.0;
	}
	return constr.get(GRB.DoubleAttr.Pi);
    }
}
