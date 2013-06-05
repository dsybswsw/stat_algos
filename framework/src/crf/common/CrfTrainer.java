// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import crf.utils.CrfTrainData;
import inference.GradientComputer;
import inference.GradientOptimizer;

import java.util.List;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/20/13
 */
public class CrfTrainer {
	public static void train(List<TaggedSentence> trainData, List<List<String>> tagList, String modelFile) {
		FeatExtractor featExtractor = new FeatExtractor();
		CrfTrainData crfTrainData = new CrfTrainData(trainData, tagList, featExtractor);
		GradientComputer gradientComputer = new CRFGradientComputer(crfTrainData, featExtractor);
		double[] weights = new double[crfTrainData.getCRFIndexer().getFeatLength()];
		GradientOptimizer optimizer = new GradientOptimizer(crfTrainData.getCRFIndexer().getFeatLength(), weights, gradientComputer);
		optimizer.run();
		double[] finalWeights = optimizer.getWeightVectors();
		for (int i = 0; i < crfTrainData.getCRFIndexer().getFeatLength(); ++i) {
			System.out.println(crfTrainData.getCRFIndexer().getFeatContext(i) + ":" + finalWeights[i]);
		}
		CRFModel crfModel = new CRFModel(finalWeights, crfTrainData.getCRFIndexer());
		crfModel.persist(modelFile);
	}
}
