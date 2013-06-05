package graphical_models;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/29/13
 */
public class VariableNode extends Node {
	private Map<Integer, Double> jointProb;

	private int statelength;

	public VariableNode(int id, String name, int stateLength) {
		super(id, name);
		jointProb = new HashMap<Integer, Double>();
		this.statelength = stateLength;
	}

	public int getStateLength() {
		return statelength;
	}

	public double getJointProb(int stateIdx) {
		if (stateIdx >= statelength || !jointProb.containsKey(stateIdx))
			return 0;
		return jointProb.get(stateIdx);
	}

	public void setJointProb(int idx, double prob) {
		if (idx < statelength)
			jointProb.put(idx, prob);
	}
}
