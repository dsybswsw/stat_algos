// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.features.FeatExtractor;
import crf.utils.CrfTrainData;
import crf.utils.TrainDataCalculator;
import inference.GradientComputer;

/**
 * @author Shiwei Wu
 * @Date May 19, 2013
 */
public class CRFGradientComputer extends GradientComputer {
	private TrainDataCalculator trainDataCalc;

	public CRFGradientComputer(CrfTrainData trainData, FeatExtractor featExtractor) {
		super(trainData.getCRFIndexer().getFeatLength());
		this.trainDataCalc = new TrainDataCalculator(trainData, featExtractor);
	}

	@Override
	public void printLastestStatistics() {
		// TODO Auto-generated method stub
	}

	@Override
	public void reComputeFuncValandGradients(double[] weights) {
		functionValue = trainDataCalc.computeFuncValandGradients(weights, gradients);
	}
}
