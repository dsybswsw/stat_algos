// Copyright (c) 2013. Shiwei Wu reserved.
package crf.utils;

import crf.common.CRFModel;
import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import forward_backward.FBStateCalculator;

import java.util.List;
import java.util.Map;

/**
 * @author Shiwei Wu
 * @Date May 17, 2013
 */
public class TrainDataCalculator {
	private CrfTrainData trainData;
	private CrfIndexer indexer;
	private FeatExtractor featExtractor;

	private double lambda = 1.0;

	public TrainDataCalculator(CrfTrainData trainData, FeatExtractor featExtractor) {
		this.trainData = trainData;
		this.indexer = trainData.getCRFIndexer();
		this.featExtractor = featExtractor;
	}

	/**
	 * Expected results are function value and gradients.
	 */
	public double computeFuncValandGradients(double[] weights, double[] gradients) {
		double logProb = 0;
		for (int i = 0; i < trainData.size(); ++i) {
			TaggedSentence taggedSentence = trainData.getTrainData().get(i);
			List<String> tags = trainData.getTagList(i);
			CRFModel crfModel = new CRFModel(weights, indexer);
			StateTransformer crfStateTrans = new CRFStateTransformer(trainData.getFeatList(i), tags,
							taggedSentence, featExtractor, crfModel);
			int m = taggedSentence.size();
			FBStateCalculator fbCalc = new FBStateCalculator(m, trainData.getTagSet(), crfStateTrans);

			// Calculate the function value.
			double calc = 0;
			List<Map<String, Double>> featSeq = trainData.getFeatSeq(i);
			for (int j = 1; j <= m; ++j) {
				calc += crfModel.getLinearValue(featSeq.get(j));
			}
			double sentProb = Math.exp(calc) / fbCalc.getNorm();
			logProb += Math.log(sentProb);

			// Calculate the gradients.
			int featNum = gradients.length;
			for (int k = 0; k < featNum; ++k) {
				String featContext = indexer.getFeatContext(k);
				double featVal = trainData.getSeqFeatValue(i, featContext);
				double tmpGrad = featVal;
				for (int j = 1; j <= m; ++j) {
					String a = tags.get(j - 1);
					String b = tags.get(j);
					double q = fbCalc.getProb(j, a, b);
					double featValinK = 0;
					if (featSeq.get(m).containsKey(featContext)) {
						featValinK = featSeq.get(m).get(featContext);
					}
					tmpGrad -= featValinK * q;
					gradients[k] += tmpGrad;
				}
			}
		}

		// Do L2 normalization.
		for (int i = 0; i < weights.length; ++i) {
			gradients[i] -= lambda * weights[i];
		}
		logProb -= computeL2(weights);
		return logProb;
	}

	private double computeL2(double[] weights) {
		int len = weights.length;
		double l2 = 0;
		for (int i = 0; i < len; ++i) {
			l2 += weights[i] * weights[i];
		}
		l2 = lambda * l2 / 2;
		return l2;
	}
}
