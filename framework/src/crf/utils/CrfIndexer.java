// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import java.util.*;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class CrfIndexer {
	private Map<String, Integer> featIndexMap;

	private List<String> indexFeatList;

	private Set<String> candSet;

	public CrfIndexer(Set<String> allFeatSet, Set<String> candSet) {
		featIndexMap = new HashMap<String, Integer>();
		indexFeatList = new ArrayList<String>();
		this.candSet = candSet;
		List<String> featList = new ArrayList<String>(allFeatSet);
		int index = 0;
		for (int i = 0; i < featList.size(); ++i) {
			String feat = featList.get(i);
			featIndexMap.put(feat, index);
			indexFeatList.add(feat);
			++index;
		}
	}

	public int getFeatIndex(String feat) {
		if (!featIndexMap.containsKey(feat)) {
			return -1;
		}
		return featIndexMap.get(feat);
	}

	public int getFeatLength() {
		return indexFeatList.size();
	}

	public String getFeatContext(int index) {
		if (index < 0 || index >= indexFeatList.size()) {
			return null;
		}
		return indexFeatList.get(index);
	}

	public Set<String> getCandSet() {
		return candSet;
	}
}
