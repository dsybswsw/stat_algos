// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import utils.common.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BiStateSlot {
	private Map<Pair<String, String>, Double> stateValues;

	private final static Logger logger = Logger.getLogger(BiStateSlot.class.getName());

	public BiStateSlot() {
		stateValues = new HashMap<Pair<String, String>, Double>();
	}

	public void setValue(String prevState, String nextState, double value) {
		stateValues.put(new Pair<String, String>(prevState, nextState), value);
	}

	public double getValue(String prevState, String nextState) {
		Pair<String, String> biState = new Pair<String, String>(prevState, nextState);
		if (!stateValues.containsKey(biState)) {
			logger.info("State " + biState.toString() + " doesn't exist!");
			return 0.0;
		}
		return stateValues.get(biState);
	}
}
