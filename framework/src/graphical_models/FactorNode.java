package graphical_models;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/29/13
 */
public class FactorNode extends Node {
	private Map<String, Double> featContexts;

	public FactorNode(int id, String name) {
		super(id, name);
		featContexts = new HashMap<String, Double>();
	}
}
