// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import crf.common.CRFModel;
import crf.features.FeatExtractor;
import crf.features.TaggedSentence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Date May 16, 2013
 */
public class CRFStateTransformer extends StateTransformer {
	private TaggedSentence taggedSentence;
	private FeatExtractor featExtractor;

	private CRFModel CrfModel;

	private List<Map<String, Double>> featMapList;
	private List<String> tags;

	private final static Logger logger = Logger.getLogger(CRFStateTransformer.class.getName());

	public CRFStateTransformer(TaggedSentence taggedSentence, FeatExtractor featExtractor,
	                           CRFModel crfModel) {
		this.taggedSentence = taggedSentence;
		this.featExtractor = featExtractor;
		this.CrfModel = crfModel;
		this.featMapList = null;
		this.tags = null;
		logger.info("Initialize a state transformer");
	}

	public CRFStateTransformer(List<Map<String, Double>> featMapList, List<String> tags, TaggedSentence taggedSentence,
	                           FeatExtractor featExtractor, CRFModel crfModel) {
		this.featMapList = featMapList;
		this.taggedSentence = taggedSentence;
		this.featExtractor = featExtractor;
		this.tags = tags;
		this.CrfModel = crfModel;
	}

	public double getTranformProb(String lastState, String nowState, int pos) {
		Map<String, Double> featTbl = new HashMap<String, Double>();
		featExtractor.extract(taggedSentence, lastState, nowState, pos, featTbl);
		// logger.info("features : " + featTbl.keySet());
		return CrfModel.getExpValue(featTbl);
	}

	/*public double getTransformTrainProb(String lastState, String nowState,int pos) {
		Map<String, Double> featTbl = featMapList.get(pos);
		featExtractor.extractBiGram(taggedSentence, lastState, nowState, pos, featTbl);
		logger.info("features : " + featTbl.keySet());
		return CrfModel.getExpValue(featTbl);
	}*/
}
