// Copyright (c) 2013. Shiwei Wu reserved.
package crf.features;

import utils.common.Pair;

import java.util.Map;

/**
 * Extends the feature extractor.
 * For optimize the feature extraction efficiency in training step.
 * TODO (Shiwei Wu) : To be implemented.
 * <p/>
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class CRFTrainFeatureExtractor extends FeatExtractor {
	private CRFTrainFeatureExtractor() {
		super();
	}

	// To be optimized.
	public void extractUniGram(TaggedSentence taggedSentence, String prevState, String nowState,
	                           int pos, Map<String, Double> featTbl) {
		String context = taggedSentence.getSlot(pos);
		Pair<String, String> feat = new Pair<String, String>(context, nowState);
		featTbl.put(feat.toString(), 1.0);
	}

	public void extractBiGram(TaggedSentence taggedSentence, String prevState, String nowState,
	                          int pos, Map<String, Double> featTbl) {
		Pair<String, String> feat = new Pair<String, String>(prevState, nowState);
		featTbl.put(feat.toString(), 1.0);
	}

	public void extract(TaggedSentence taggedSentence, String prevState, String nowState,
	                    int pos, Map<String, Double> featTbl) {
		extractUniGram(taggedSentence, prevState, nowState, pos, featTbl);
		extractBiGram(taggedSentence, prevState, nowState, pos, featTbl);
	}
}
