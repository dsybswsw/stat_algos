// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class Indexer {
	private Map<String, Integer> featIndexMap;

	private Map<String, Integer> outLabelIndexMap;

	private List<String> indexFeatList;

	private List<String> indexOutLabel;

	public Indexer(String trainingFile) {
		// Initialize the features and output labels from the training file.
		List<MaxEntUnit> trainData = MaxEntFileHandler.transFileToDataStruct(trainingFile);
		initialize(trainData);
	}

	public Indexer(List<MaxEntUnit> trainData) {
		initialize(trainData);
	}

	public Indexer(List<String> indexFeatList, List<String> indexOutLabel) {
		this.indexFeatList = indexFeatList;
		this.indexOutLabel = indexOutLabel;
		featIndexMap = new HashMap<String, Integer>();
		outLabelIndexMap = new HashMap<String, Integer>();

		for (int i = 0; i < indexFeatList.size(); ++i) {
			featIndexMap.put(indexFeatList.get(i), i);
		}

		for (int i = 0; i < indexOutLabel.size(); ++i) {
			outLabelIndexMap.put(indexOutLabel.get(i), i);
		}
	}

	public void initialize(List<MaxEntUnit> trainData) {
		featIndexMap = new HashMap<String, Integer>();
		outLabelIndexMap = new HashMap<String, Integer>();
		indexFeatList = new ArrayList<String>();
		indexOutLabel = new ArrayList<String>();
		initStrIndex(trainData);
	}

	private void initStrIndex(List<MaxEntUnit> trainData) {
		for (MaxEntUnit unit : trainData) {
			for (String feat : unit.getFeatSet()) {
				featIndexMap.put(feat, -1);
			}
			outLabelIndexMap.put(unit.getCategory(), -1);
		}

		// Allcate index for each label.
		int featIndex = 0;
		for (String feat : featIndexMap.keySet()) {
			indexFeatList.add(feat);
			featIndexMap.put(feat, featIndex);
			++featIndex;
		}

		int cateIndex = 0;
		for (String category : outLabelIndexMap.keySet()) {
			indexOutLabel.add(category);
			outLabelIndexMap.put(category, cateIndex);
			++cateIndex;
		}
	}

	public int getFeatIndex(String feat) {
		return featIndexMap.get(feat);
	}

	public int getFeatLength() {
		return indexFeatList.size();
	}

	public int getOutputLength() {
		return indexOutLabel.size();
	}

	public String getFeatContext(int index) {
		return indexFeatList.get(index);
	}

	public String getOutput(int index) {
		return indexOutLabel.get(index);
	}

	public int getOutputIndex(String outLabel) {
		return outLabelIndexMap.get(outLabel);
	}
}
