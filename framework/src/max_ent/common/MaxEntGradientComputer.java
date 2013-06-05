// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import inference.GradientComputer;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Date May 8, 2013
 */
public class MaxEntGradientComputer extends GradientComputer {
	private List<MaxEntUnit> trainData;

	private Indexer indexer;

	private final static Logger logger = Logger.getLogger(MaxEntGradientComputer.class.getName());

	public MaxEntGradientComputer(Indexer indexer, List<MaxEntUnit> trainData) {
		super(indexer.getFeatLength() * indexer.getOutputLength());
		this.trainData = trainData;
		this.indexer = indexer;
	}

	@Override
	public void printLastestStatistics() {
	}

	public void reComputeFuncVal(double[] weights) {
		for (int i = 0; i < weights.length; ++i) {
			logger.info("weight " + i + " is " + weights[i]);
		}
		MaxEntModel model = new MaxEntModel(weights, indexer);
		functionValue = 0;
		for (MaxEntUnit unit : trainData) {
			double prob = model.getProb(unit);
			logger.info("prob : " + prob);
			functionValue += Math.log(prob);
		}
		functionValue = -functionValue;
		logger.info("function value : " + functionValue);
	}

	public void reComputeGradients(double[] weights) {
		MaxEntModel model = new MaxEntModel(weights, indexer);
		for (int i = 0; i < indexer.getFeatLength(); ++i) {
			for (int j = 0; j < indexer.getOutputLength(); ++j) {
				int k = i * indexer.getOutputLength() + j;
				gradients[k] = 0;
				for (MaxEntUnit unit : trainData) {
					String context = indexer.getFeatContext(i);
					double value = 0;
					if (j == indexer.getOutputIndex(unit.getCategory()))
						value = unit.getValue(context);
					double normValue = 0;
					for (int h = 0; h < indexer.getOutputLength(); ++h) {
						MaxEntUnit newUnit = unit.clone();
						newUnit.setCategory(indexer.getOutput(h));
						double prob = model.getProb(newUnit);
						normValue += prob;
					}
					double iGrad = value - normValue;
					gradients[k] += iGrad;
				}
				gradients[k] = -gradients[k];
				logger.info("gradient " + k + ":" + gradients[k]);
			}
		}
	}

	@Override
	public void reComputeFuncValandGradients(double[] weights) {
		reComputeFuncVal(weights);
		reComputeGradients(weights);
	}
}
