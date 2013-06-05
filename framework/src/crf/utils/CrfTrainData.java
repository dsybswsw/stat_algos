// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import utils.common.CollectionUtil;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Date May 17, 2013
 */
public class CrfTrainData {
	private List<TaggedSentence> trainData;

	private List<List<String>> trainTags;

	private List<List<Map<String, Double>>> featSequence;

	private Set<String> tagSet;

	private List<Map<String, Double>> sequenceFeatTbl;

	private FeatExtractor featExtractor;

	private CrfIndexer indexer;

	private final static Logger logger = Logger.getLogger(CrfTrainData.class.getName());

	public CrfTrainData(List<TaggedSentence> trainData, List<List<String>> tags,
	                    FeatExtractor featExtractor) {
		this.trainData = trainData;
		this.trainTags = tags;
		this.featExtractor = featExtractor;
		tagSet = new HashSet<String>();
		sequenceFeatTbl = new ArrayList<Map<String, Double>>();
		for (List<String> tagList : trainTags) {
			for (int i = 1; i < tagList.size(); ++i) {
				String tag = tagList.get(i);
				tagSet.add(tag);
			}
		}

		Map<String, Double> featTbl = new HashMap<String, Double>();
		for (int i = 0; i < trainTags.size(); ++i) {
			TaggedSentence taggedSentence = trainData.get(i);
			List<String> seqTags = trainTags.get(i);
			for (int m = 1; m <= taggedSentence.size(); ++m) {
				featExtractor.extract(taggedSentence, seqTags.get(m - 1), seqTags.get(m), m, featTbl);
			}
		}

		indexer = new CrfIndexer(featTbl.keySet(), tagSet);
		featSequence = new ArrayList<List<Map<String, Double>>>();
		extractFeats();
	}

	public Set<String> getTagSet() {
		return tagSet;
	}

	public List<Map<String, Double>> getFeatSeq(int i) {
		return featSequence.get(i);
	}

	public List<String> getTagList(int i) {
		return trainTags.get(i);
	}

	private void extractFeats() {
		int idx = 0;
		for (TaggedSentence taggedSentence : trainData) {
			List<String> tags = trainTags.get(idx);
			List<Map<String, Double>> featSeq = new CollectionUtil<Map<String, Double>>().
							getEmptyList(tags.size());
			for (int i = 1; i < tags.size(); ++i) {
				Map<String, Double> featTbl = new HashMap<String, Double>();
				featExtractor.extract(taggedSentence, tags.get(i - 1), tags.get(i), i, featTbl);
				featSeq.set(i, featTbl);
			}
			featSequence.add(featSeq);
			++idx;

			// Get the whole sentence feature map.
			Map<String, Double> seqFeatTbl = new HashMap<String, Double>();
			for (int i = 1; i < tags.size(); ++i) {
				Map<String, Double> featTbl = featSeq.get(i);
				logger.info("interative features :" + featTbl.keySet());
				for (String feat : featTbl.keySet()) {
					if (seqFeatTbl.containsKey(feat)) {
						seqFeatTbl.put(feat, seqFeatTbl.get(feat) + featTbl.get(feat));
					} else {
						seqFeatTbl.put(feat, featTbl.get(feat));
					}
				}
			}
			sequenceFeatTbl.add(seqFeatTbl);
		}
	}

	public double getFeatValue(int idx, int pos, int k) {
		String feat = indexer.getFeatContext(k);
		if (feat == null)
			return 0;
		Map<String, Double> featTbl = featSequence.get(idx).get(pos);
		if (featTbl.containsKey(feat)) {
			return featTbl.get(feat);
		} else {
			return 0;
		}
	}

	public List<TaggedSentence> getTrainData() {
		return trainData;
	}

	public int size() {
		return trainData.size();
	}

	public List<Map<String, Double>> getFeatList(int i) {
		return featSequence.get(i);
	}

	public double getSeqFeatValue(int i, String featContext) {
		if (featContext == null)
			return 0;
		Map<String, Double> seqFeatMap = this.sequenceFeatTbl.get(i);
		if (!seqFeatMap.containsKey(featContext)) {
			return 0;
		}
		return seqFeatMap.get(featContext);
	}

	public CrfIndexer getCRFIndexer() {
		return indexer;
	}
}
