// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import inference.GradientComputer;
import inference.GradientOptimizer;

import java.util.List;

/**
 * @author Shiwei Wu
 * @Date May 9, 2013
 */
public class MaxEntTrainer {
	public void train(String trainFilePath, String modelFile) {
		List<MaxEntUnit> trainData = MaxEntFileHandler.transFileToDataStruct(trainFilePath);
		Indexer indexer = new Indexer(trainData);
		GradientComputer maxGradientComputer = new MaxEntGradientComputer(indexer, trainData);
		int numParams = indexer.getFeatLength() * indexer.getOutputLength();
		System.out.println(numParams + ":" + indexer.getFeatLength() + ":" + indexer.getOutputLength());
		double[] weights = null;
		GradientOptimizer optimizer = new GradientOptimizer(numParams, weights, maxGradientComputer);
		optimizer.run();
		double[] finalWeights = optimizer.getWeightVectors();
		MaxEntModel finalModel = new MaxEntModel(finalWeights, indexer);
		finalModel.persist(modelFile);
		for (int i = 0; i < numParams; ++i) {
			System.out.println(finalWeights[i]);
		}
	}
}
