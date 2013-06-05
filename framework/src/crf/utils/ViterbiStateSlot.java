// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/22/13
 */
public class ViterbiStateSlot {
	private UniStateSlot stateSlot;

	private Map<String, String> lastStateMap;

	public ViterbiStateSlot(UniStateSlot stateSlot, Map<String, String> lastStateMap) {
		this.stateSlot = stateSlot;
		this.lastStateMap = lastStateMap;
	}

	public UniStateSlot getStateSlot() {
		return stateSlot;
	}

	public Map<String, String> getTraceBackMap() {
		return lastStateMap;
	}
}
