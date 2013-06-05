// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import crf.utils.UniStateSlot;
import crf.utils.ViterbiStateSlot;
import utils.common.CollectionUtil;

import java.util.*;
import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/22/13
 */
public class CRFDecoder {
	private CRFModel crfModel;

	private FeatExtractor featExtractor;

	private Set<String> candSet;

	private TaggedSentence taggedSentence;

	private List<ViterbiStateSlot> decodingStates;

	private int m;

	private List<String> tags;

	private final static Logger logger = Logger.getLogger(CRFDecoder.class.getName());

	public CRFDecoder(TaggedSentence taggedSentence, CRFModel crfModel, FeatExtractor featExtractor, Set<String> candSet) {
		this.crfModel = crfModel;
		this.featExtractor = featExtractor;
		this.candSet = candSet;
		this.taggedSentence = taggedSentence;
		this.m = taggedSentence.size();
		decodingStates = new CollectionUtil<ViterbiStateSlot>().getEmptyList(m + 1);
		tags = new CollectionUtil<String>().getEmptyList(m + 1);

	}

	public void viterbiDecode() {
		// Initialize the 1st state.
		UniStateSlot firstState = new UniStateSlot();
		Map<String, String> firstTraceBackMap = new HashMap<String, String>();
		for (String state : crfModel.getCanddidates()) {
			Map<String, Double> featTbl = new HashMap<String, Double>();
			featExtractor.extract(taggedSentence, "*", state, 1, featTbl);
			double value = crfModel.getLinearValue(featTbl);
			firstState.setValue(state, value);
			firstTraceBackMap.put(state, "*");
		}
		decodingStates.set(1, new ViterbiStateSlot(firstState, firstTraceBackMap));
		for (int i = 2; i <= m; ++i) {
			logger.info("in slot " + i);
			UniStateSlot slotState = new UniStateSlot();
			Map<String, String> traceBackMap = new HashMap<String, String>();
			for (String state : candSet) {
				logger.info("now state is " + state);
				double maxValue = Double.NEGATIVE_INFINITY;
				for (String lastState : candSet) {
					Map<String, Double> featTbl = new HashMap<String, Double>();
					featExtractor.extract(taggedSentence, lastState, state, i, featTbl);
					logger.info("features : " + featTbl.keySet());
					double lastMax = decodingStates.get(i - 1).getStateSlot().getValue(lastState);
					logger.info("last tag " + lastState + " last value is " + lastMax);
					double value = lastMax + crfModel.getLinearValue(featTbl);
					logger.info("last tag " + lastState + " now tag " + state + " now value is " + value);
					if (value > maxValue) {
						maxValue = value;
						slotState.setValue(state, maxValue);
						traceBackMap.put(state, lastState);
					}
				}
			}
			ViterbiStateSlot viterbiStateSlot = new ViterbiStateSlot(slotState, traceBackMap);
			decodingStates.set(i, viterbiStateSlot);
		}
		traceBack();
	}

	private void traceBack() {
		ViterbiStateSlot lastStateSlot = decodingStates.get(m);
		double max = Double.NEGATIVE_INFINITY;
		String lastTag = null;
		String prevState = null;
		for (String state : candSet) {
			logger.info(state);
			double value = lastStateSlot.getStateSlot().getValue(state);
			if (value > max) {
				max = value;
				lastTag = state;
				prevState = lastStateSlot.getTraceBackMap().get(lastTag);
			}
		}
		tags.set(m, lastTag);
		for (int i = m - 1; i >= 1; --i) {
			String nowState = prevState;
			tags.set(i, nowState);
			ViterbiStateSlot stateSlot = decodingStates.get(i);
			prevState = stateSlot.getTraceBackMap().get(nowState);
		}
		tags.set(0, prevState);
	}

	public List<String> getTags() {
		return tags;
	}
}
