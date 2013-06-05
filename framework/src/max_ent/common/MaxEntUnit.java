// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import java.util.Map;
import java.util.Set;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class MaxEntUnit {
	private String category;

	private Map<String, Double> featTbl;

	public MaxEntUnit(String category, Map<String, Double> featTbl) {
		this.category = category;
		this.featTbl = featTbl;
	}

	public String getCategory() {
		return category;
	}

	public MaxEntUnit clone() {
		// featTbl will not change so we don't clone it.
		return new MaxEntUnit(category, featTbl);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setFeatTbl(Map<String, Double> featTbl) {
		this.featTbl = featTbl;
	}

	public double getValue(String key) {
		if (!featTbl.containsKey(key)) {
			return 0;
		}
		return featTbl.get(key);
	}

	public Set<String> getFeatSet() {
		return this.featTbl.keySet();
	}
}
