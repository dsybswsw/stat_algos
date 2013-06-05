// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UniStateSlot {
	private Map<String, Double> stateValues;

	private final static Logger logger = Logger.getLogger(UniStateSlot.class.getName());

	public UniStateSlot() {
		stateValues = new HashMap<String, Double>();
	}

	public void setValue(String state, double value) {
		stateValues.put(state, value);
	}

	public double getValue(String state) {
		if (!stateValues.containsKey(state)) {
			logger.info("State " + state + " doesn't exist!");
			return 0.0;
		}
		return stateValues.get(state);
	}
}
