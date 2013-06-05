// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import utils.common.LineReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class MaxEntModel {
	private int featNum;
	private int categories;
	private double[][] modelWeights;

	private Indexer indexer;

	private final static Logger logger = Logger.getLogger(MaxEntModel.class.getName());

	public MaxEntModel(double[] weights, Indexer indexer) {
		this.featNum = indexer.getFeatLength();
		this.categories = indexer.getOutputLength();
		if (weights.length != featNum * categories)
			logger.info("Failed to initialize a max entropy model");
		modelWeights = new double[featNum][categories];
		for (int i = 0; i < featNum; ++i) {
			for (int j = 0; j < categories; ++j) {
				modelWeights[i][j] = weights[i * categories + j];
			}
		}
		this.indexer = indexer;
	}

	public MaxEntModel(String modelFile) throws Exception {
		LineReader lineReader = new LineReader(modelFile);
		int featNum = Integer.parseInt(lineReader.next());
		this.featNum = featNum;
		List<String> feats = new ArrayList<String>();
		for (int i = 0; i < featNum; ++i) {
			String feat = lineReader.next();
			feats.add(feat);
		}

		int cateNum = Integer.parseInt(lineReader.next());
		this.categories = cateNum;
		List<String> cates = new ArrayList<String>();
		for (int i = 0; i < cateNum; ++i) {
			String cate = lineReader.next();
			cates.add(cate);
		}
		this.indexer = new Indexer(feats, cates);
		modelWeights = new double[featNum][categories];
		for (int i = 0; i < featNum; ++i) {
			for (int j = 0; j < categories; ++j) {
				String modelLine = lineReader.next();
				String[] tokens = modelLine.split("\\|\\|\\|");
				double weight = Double.parseDouble(tokens[2]);
				modelWeights[i][j] = weight;
			}
		}
		lineReader.close();
	}

	private Map<Integer, Double> normToIntFeatTbl(Map<String, Double> featTbl) {
		Map<Integer, Double> intFeatTbl = new HashMap<Integer, Double>();
		for (String feat : featTbl.keySet()) {
			int featIndex = indexer.getFeatIndex(feat);
			double value = featTbl.get(feat);
			intFeatTbl.put(featIndex, value);
		}
		return intFeatTbl;
	}

	public double getProb(MaxEntUnit unit) {
		Map<Integer, Double> intFeatTbl = new HashMap<Integer, Double>();
		for (String feat : unit.getFeatSet()) {
			int featIndex = indexer.getFeatIndex(feat);
			double value = unit.getValue(feat);
			intFeatTbl.put(featIndex, value);
		}
		int outIdx = indexer.getOutputIndex(unit.getCategory());
		return getProb(outIdx, intFeatTbl);
	}

	public double getExpLinearVal(MaxEntUnit unit) {
		Map<Integer, Double> intFeatTbl = new HashMap<Integer, Double>();
		for (String feat : unit.getFeatSet()) {
			int featIndex = indexer.getFeatIndex(feat);
			double value = unit.getValue(feat);
			intFeatTbl.put(featIndex, value);
		}
		int outIdx = indexer.getOutputIndex(unit.getCategory());
		return computeLinearComb(outIdx, intFeatTbl);
	}

	private double getProb(int outIdx, Map<Integer, Double> intFeatTbl) {
		double linearComb = computeLinearComb(outIdx, intFeatTbl);
		double normValue = 0;
		for (int i = 0; i < categories; ++i) {
			if (i == outIdx) {
				normValue += linearComb;
			} else {
				normValue += computeLinearComb(i, intFeatTbl);
			}
		}
		return linearComb / normValue;
	}

	public double computeLinearComb(int outIdx, Map<Integer, Double> intFeatTbl) {
		double linearComb = 0;
		for (int featIdx : intFeatTbl.keySet()) {
			if (featIdx >= featNum)
				logger.info("Feat index out of bounds");
			double value = intFeatTbl.get(featIdx);
			double weight = modelWeights[featIdx][outIdx];
			linearComb += value * weight;
		}
		return Math.exp(linearComb);
	}

	public String getMaxLabel(Map<String, Double> featTbl) {
		// TODO (Shiwei Wu) : to be implemented.
		double maxProb = 0;
		int maxOutIdx = -1;
		Map<Integer, Double> intFeatTbl = normToIntFeatTbl(featTbl);
		for (int outIdx = 0; outIdx < categories; ++outIdx) {
			double tmpProb = getProb(outIdx, intFeatTbl);
			if (tmpProb > maxProb) {
				maxOutIdx = outIdx;
				maxProb = tmpProb;
			}
		}
		return indexer.getOutput(maxOutIdx);
	}

	public void persist(String modelFile) {
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter(new File(modelFile)));

			// First line, write the number of features.
			buff.write(featNum + "\n");

			// write the feature contexts.
			for (int i = 0; i < featNum; ++i) {
				buff.write(indexer.getFeatContext(i) + "\n");
			}

			buff.write(categories + "\n");
			for (int i = 0; i < categories; ++i) {
				buff.write(indexer.getOutput(i) + "\n");
			}

			// write weights.
			for (int i = 0; i < featNum; ++i) {
				for (int j = 0; j < categories; ++j) {
					StringBuilder modelLine = new StringBuilder();
					modelLine.append(i).append("|||").append(j).append("|||").append(modelWeights[i][j]).append("\n");
					buff.write(modelLine.toString());
				}
			}
			buff.close();
		} catch (IOException e) {
			logger.info("Failed to persist the max entropy model");
		}
	}
}
