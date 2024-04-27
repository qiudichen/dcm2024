package com.e2.wfm.commons.pattern;

import com.e2.wfm.commons.pattern.rule.ShiftUsageRule;
import com.e2.wfm.rule.weekly.ScheduleRule;

public class PatternGenerator implements Runnable {
	private final PatternDataBus patternDataBus;
	private final ScheduleListener listener;
	
	public static PatternGenerator createPatternGenerator(ScheduleRule scheduleRule) {
		PatternDataBus patternDataBus = new PatternDataBus(scheduleRule);
		ScheduleListener listener = new ScheduleListenerImpl(patternDataBus.getQueueSize());
		return new PatternGenerator(patternDataBus, listener);
	}
	
	public PatternGenerator(PatternDataBus patternDataBus, ScheduleListener listener) {
		this.patternDataBus = patternDataBus;
		this.listener = listener;
	}
	
	@Override
	public void run() {
		int layer = 0;
		recursiveSelect(patternDataBus, layer);
		listener.setCompleted();
	}
	
	private void recursiveSelect(PatternDataBus patternDataBus, int layer) {
		if(patternDataBus.isLastLayer(layer)) {
			patternDataBus.processResult(listener);
			return;
		}
		
		//get all elements in this layer
		int[] elements = patternDataBus.getSelectableIds()[layer];
		SelectedValue selectedValue = patternDataBus.getSelectedValue();
		//pick up an element from this layer one by one
		for(int i = 0; i < elements.length; i++) {
			int id = elements[i];
			//check how many is selected of this id
			int usage = selectedValue.getUsage(id);
			ShiftUsageRule shiftUsageRule = patternDataBus.getShiftUsageRule(id);
			if(shiftUsageRule != null && shiftUsageRule.isGreaterMax(usage + 1)) {
				//reach the max uage if select this one. skip it
				continue;
			}
			//pick this element in this layer
			selectedValue.setValue(id, layer);
			recursiveSelect(patternDataBus, layer + 1);
			selectedValue.resetValue(layer);
		}
	}

	public ScheduleListener getListener() {
		return listener;
	}
}
