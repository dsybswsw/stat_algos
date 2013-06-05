// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.utils.CrfIndexer;
import utils.common.LineReader;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class CRFModel {
	private int featNum;
	private double[] modelWeights;

	private CrfIndexer indexer;

	private Set<String> candStates;

	private final static Logger logger = Logger.getLogger(CRFModel.class.getName());

	public CRFModel(double[] weights, CrfIndexer indexer) {
		this.featNum = indexer.getFeatLength();
		this.modelWeights = weights;
		this.indexer = indexer;
		this.candStates = indexer.getCandSet();
	}

	public CRFModel(String modelFile) throws Exception {
		LineReader lineReader = new LineReader(modelFile);
		candStates = new HashSet<String>();
		// First line : number of candidate state.
		int candNum = Integer.parseInt(lineReader.next());

		for (int i = 0; i < candNum; ++i) {
			candStates.add(lineReader.next());
		}

		featNum = Integer.parseInt(lineReader.next());
		Map<String, Double> featValMap = new HashMap<String, Double>();
		for (int i = 0; i < featNum; ++i) {
			String line = lineReader.next();
			String[] tokens = line.split("\\|\\|\\|");
			String context = tokens[0];
			double value = Double.parseDouble(tokens[1]);
			featValMap.put(context, value);
		}
		indexer = new CrfIndexer(featValMap.keySet(), candStates);

		modelWeights = new double[featNum];
		for (String context : featValMap.keySet()) {
			double value = featValMap.get(context);
			int idx = indexer.getFeatIndex(context);
			modelWeights[idx] = value;
		}
		lineReader.close();
	}

	private Map<Integer, Double> normToIntFeatTbl(Map<String, Double> featTbl) {
		Map<Integer, Double> intFeatTbl = new HashMap<Integer, Double>();
		for (String feat : featTbl.keySet()) {
			int featIndex = indexer.getFeatIndex(feat);
			if (featIndex < 0)
				continue;
			double value = featTbl.get(feat);
			intFeatTbl.put(featIndex, value);
		}
		return intFeatTbl;
	}

	public double getExpValue(Map<String, Double> featTbl) {
		Map<Integer, Double> intFeatTbl = normToIntFeatTbl(featTbl);
		return Math.exp(computeLinearComb(intFeatTbl));
	}

	public double getLinearValue(Map<String, Double> featTbl) {
		Map<Integer, Double> intFeatTbl = normToIntFeatTbl(featTbl);
		return computeLinearComb(intFeatTbl);
	}

	public double computeLinearComb(Map<Integer, Double> intFeatTbl) {
		double linearComb = 0;
		for (int featIdx : intFeatTbl.keySet()) {
			if (featIdx >= featNum)
				logger.info("Feat index out of bounds");
			double value = intFeatTbl.get(featIdx);
			double weight = modelWeights[featIdx];
			linearComb += value * weight;
		}
		return linearComb;
	}

	public void persist(String modelFile) {
		try {
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(modelFile))));
			printWriter.println(candStates.size());
			for (String candState : candStates) {
				printWriter.println(candState);
			}
			printWriter.println(featNum);
			for (int i = 0; i < featNum; ++i) {
				printWriter.println(indexer.getFeatContext(i) + "|||" + modelWeights[i]);
			}
			printWriter.close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	public Set<String> getCanddidates() {
		return candStates;
	}
}
