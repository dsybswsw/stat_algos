// Copyright (c) 2013. Shiwei Wu reserved.
package crf.features;

import utils.common.Pair;

import java.util.Map;

public class FeatExtractor {
	private FeatTemlate featTemplate;

	private static FeatExtractor featExtractor;

	public FeatExtractor() {
	}

	private FeatExtractor(FeatTemlate featTemplate) {
		this.featTemplate = featTemplate;
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