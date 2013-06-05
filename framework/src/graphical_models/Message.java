package graphical_models;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/29/13
 */
public class Message {
	private Map<Integer, Double> messageValTbl;

	public Message() {
		messageValTbl = new HashMap<Integer, Double>();
	}

	public void setStateVal(int stateIdx, double val) {
		messageValTbl.put(stateIdx, val);
	}

	public double getStateVal(int stateIdx) {
		return messageValTbl.get(stateIdx);
	}

	public String toString() {
		return messageValTbl.toString();
	}
}
